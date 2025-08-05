package xyz.dreature.ssjrt.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import xyz.dreature.ssjrt.common.constant.CacheConstants;
import xyz.dreature.ssjrt.security.userdetails.UserPrincipal;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtService {
    // 常量
    public static final String TOKEN_TYPE = "Bearer"; // 请求头前缀
    public static final long REFRESH_THRESHOLD_MS = 5 * 60 * 1000;  // 令牌刷新阈值（提前刷新时间）
    // JWT 配置
    private final SecretKey secretKey;
    private final long tokenValidityMs;
    // Redis 模板
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public JwtService(
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
        // 1. 获取用户主体
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        // 2. 创建 JWT 声明
        Claims claims = Jwts.claims().setSubject(principal.getId().toString());

        // 3. 添加自定义声明
        claims.put("username", principal.getUsername());
        claims.put("authorities", principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        // 4. 生成令牌
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidityMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 刷新令牌
    public String refreshToken(String token) {
        try {
            // 1. 检查黑名单
            if (isTokenBlacklisted(token)) {
                log.debug("令牌在黑名单中，拒绝刷新: {}", token);
                return null;
            }

            // 2. 解析原令牌（不验证过期）
            Claims claims = parseTokenWithoutExpiration(token);

            // 3. 获取用户 ID
            String userId = claims.getSubject();

            // 4. 检查认证信息缓存
            String authCacheKey = CacheConstants.USER_AUTH_CACHE_PREFIX + userId;
            UserPrincipal principal = (UserPrincipal) redisTemplate.opsForValue().get(authCacheKey);

            if (principal == null) {
                log.debug("用户认证缓存失效，无法刷新令牌");
                return null; // 缓存失效，无法刷新
            }

            // 5. 检查令牌是否即将过期（在刷新阈值内）
            Date expiration = claims.getExpiration();
            long remainingTime = expiration.getTime() - System.currentTimeMillis();

            if (remainingTime > REFRESH_THRESHOLD_MS) {
                log.debug("令牌未到刷新时间，返回原令牌");
                return token; // 未到刷新时间，返回原令牌
            }

            // 6. 生成新令牌
            log.debug("刷新令牌: userId={}", userId);
            return generateToken(
                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            principal.getAuthorities()
                    )
            );
        } catch (Exception e) {
            log.error("令牌刷新失败", e);
            return null;
        }
    }

    // 验证令牌有效性
    public boolean validateToken(String token) {
        try {
            // 1. 检查黑名单
            if (isTokenBlacklisted(token)) {
                log.debug("令牌在黑名单中");
                return false;
            }

            // 2. 解析令牌（会验证签名和过期时间）
            parseToken(token);

            return true;
        } catch (Exception e) {
            log.debug("令牌验证失败: {}", e.getMessage());
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
                String blacklistKey = CacheConstants.TOKEN_BLACKLIST_PREFIX + token;

                redisTemplate.opsForValue().set(
                        blacklistKey,
                        "invalid",
                        remainingTime,
                        TimeUnit.MILLISECONDS
                );
            }
        } catch (ExpiredJwtException ignored) {
            log.debug("令牌已过期，无需加入黑名单");
        } catch (Exception e) {
            log.error("令牌失效操作失败", e);
            throw new SecurityException("令牌失效操作失败", e);
        }
    }

    // 从请求中提取令牌
    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!StringUtils.hasText(header)) {
            return null;
        }

        String prefix = TOKEN_TYPE + " ";
        if (header.startsWith(prefix)) {
            return header.substring(prefix.length()).trim();
        }

        return null;
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
        String key = CacheConstants.TOKEN_BLACKLIST_PREFIX + token;
        return redisTemplate.hasKey(key);
    }

    // 从令牌中获取用户 ID
    public Long getUserId(String token) {
        Claims claims = parseToken(token); // 验证并解析令牌
        return Long.parseLong(claims.getSubject()); // 主题就是用户 ID
    }

    // 从令牌中获取用户名
    public String getUsername(String token) {
        Claims claims = parseToken(token);
        return claims.get("username", String.class); // 从自定义声明获取
    }

    // 从令牌中获取认证
    public Authentication getAuthentication(String token) {
        // 1. 解析令牌
        Claims claims = parseToken(token);

        // 2. 获取用户名
        String userId = claims.getSubject();

        // 3. 检查认证信息缓存
        String authCacheKey = CacheConstants.USER_AUTH_CACHE_PREFIX + userId;
        UserPrincipal principal = (UserPrincipal) redisTemplate.opsForValue().get(authCacheKey);

        // 4. 创建认证对象
        return new UsernamePasswordAuthenticationToken(
                principal,
                null,
                principal.getAuthorities()
        );
    }
}
