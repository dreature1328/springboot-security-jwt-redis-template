package xyz.dreature.ssjrt.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class JwtTokenService {

    // 常量
    private static final String TOKEN_TYPE = "Bearer";
    private static final String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";
    private static final String TOKEN_CACHE_PREFIX = "token:cache:";
    // 令牌刷新阈值（提前刷新时间）
    private static final long REFRESH_THRESHOLD_MS = 5 * 60 * 1000; // 5分钟
    // JWT配置
    private final SecretKey secretKey;
    private final long tokenValidityMs;
    // Redis模板
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public JwtTokenService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration}") long tokenValidityMs,
            RedisTemplate<String, Object> redisTemplate) {

        // 将字符串密钥转换为安全的 SecretKey
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.tokenValidityMs = tokenValidityMs;
        this.redisTemplate = redisTemplate;
    }

    // 为认证用户生成 JWT 令牌
    public String generateToken(Authentication authentication) {
        // 1. 获取用户详情
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 2. 创建JWT声明
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());

        // 3. 添加自定义声明
        claims.put("authorities", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        // 4. 设置用户详情缓存键
        String userCacheKey = TOKEN_CACHE_PREFIX + userDetails.getUsername();

        // 5. 生成令牌
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidityMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        // 6. 缓存用户详情（用于快速刷新）
        redisTemplate.opsForValue().set(
                userCacheKey,
                userDetails,
                tokenValidityMs + REFRESH_THRESHOLD_MS, // 稍长于令牌有效期
                TimeUnit.MILLISECONDS
        );

        return token;
    }

    // 刷新令牌
    public String refreshToken(String token) {
        try {
            // 1. 解析原令牌（不验证过期）
            Claims claims = parseTokenWithoutExpiration(token);

            // 2. 获取用户名
            String username = claims.getSubject();

            // 3. 尝试从缓存获取用户详情
            String cacheKey = TOKEN_CACHE_PREFIX + username;
            UserDetails userDetails = (UserDetails) redisTemplate.opsForValue().get(cacheKey);

            if (userDetails == null) {
                return null; // 缓存失效，无法刷新
            }

            // 4. 检查令牌是否即将过期（在刷新阈值内）
            Date expiration = claims.getExpiration();
            long remainingTime = expiration.getTime() - System.currentTimeMillis();

            if (remainingTime > REFRESH_THRESHOLD_MS) {
                return token; // 未到刷新时间，返回原令牌
            }

            // 5. 生成新令牌
            return generateToken(
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    )
            );
        } catch (Exception e) {
            return null;
        }
    }

    // 验证令牌有效性
    public boolean validateToken(String token) {
        try {
            // 1. 检查黑名单
            if (isTokenBlacklisted(token)) {
                return false;
            }

            // 2. 解析令牌（会验证签名和过期时间）
            parseToken(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 使令牌失效（加入黑名单）
    public void invalidateToken(String token) {
        try {
            // 1. 解析令牌获取过期时间
            Claims claims = parseTokenWithoutExpiration(token);
            Date expiration = claims.getExpiration();

            // 2. 计算剩余有效时间
            long remainingTime = expiration.getTime() - System.currentTimeMillis();

            // 3. 仅在有效期内加入黑名单
            if (remainingTime > 0) {
                String blacklistKey = TOKEN_BLACKLIST_PREFIX + token;

                redisTemplate.opsForValue().set(
                        blacklistKey,
                        "invalid",
                        remainingTime,
                        TimeUnit.MILLISECONDS
                );
            }
        } catch (ExpiredJwtException ignored) {
            // 令牌已过期，无需加入黑名单
        } catch (Exception e) {
            throw new SecurityException("令牌失效操作失败", e);
        }
    }

    // 从令牌中解析认证信息
    public Authentication getAuthentication(String token) {
        // 1. 解析令牌
        Claims claims = parseToken(token);

        // 2. 获取用户名
        String username = claims.getSubject();

        // 3. 尝试从缓存获取用户详情
        String cacheKey = TOKEN_CACHE_PREFIX + username;
        UserDetails userDetails = (UserDetails) redisTemplate.opsForValue().get(cacheKey);

        // 4. 创建认证对象
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }

    // 获取令牌类型
    public String getTokenType() {
        return TOKEN_TYPE;
    }

    // 获取令牌有效期（毫秒）
    public long getTokenValidityMs() {
        return tokenValidityMs;
    }

    // 解析令牌（带过期验证）
    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 解析令牌（忽略过期）
    private Claims parseTokenWithoutExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 检查令牌是否在黑名单中
    private boolean isTokenBlacklisted(String token) {
        String key = TOKEN_BLACKLIST_PREFIX + token;
        return redisTemplate.hasKey(key);
    }

    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token); // 这个方法会验证令牌并抛出异常（如果无效）
        return claims.getSubject(); // 主题就是用户名
    }
}
