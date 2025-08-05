package xyz.dreature.ssjrt.security.userdetails;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xyz.dreature.ssjrt.common.constant.CacheConstants;
import xyz.dreature.ssjrt.common.entity.Permission;
import xyz.dreature.ssjrt.common.entity.User;
import xyz.dreature.ssjrt.mapper.PermissionMapper;
import xyz.dreature.ssjrt.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsServiceExt {
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 按用户名加载（框架接口实现）
    @Override
    public UserDetails loadUserByUsername(String username) {
        // 1. 检查 ID 映射缓存
        String idCacheKey = CacheConstants.USER_ID_CACHE_PREFIX + username;
        Object cachedId = redisTemplate.opsForValue().get(idCacheKey);
        if (cachedId != null) {
            // 序列化器会将取值较小的整数存为 Integer 类型，因而需要通用转换方式
            Long userId = ((Number) cachedId).longValue();
            log.debug("ID 缓存命中 [key={}, userId={}]", idCacheKey, userId);
            return loadUserById(userId);
        }

        log.debug("ID 缓存未命中 [key={}]", idCacheKey);

        // 2. 查询数据库（ID 缓存未命中）
        log.debug("查询数据库获取用户: {}", username);
        User user = userService.selectByUsername(username);

        if (user == null) {
            log.error("用户不存在: {}", username);
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        log.debug("数据库查询成功 [userId={}, username={}]", user.getId(), username);

        // 3. 缓存 ID 映射
        redisTemplate.opsForValue().set(idCacheKey, user.getId(), 30, TimeUnit.MINUTES);
        log.debug("缓存用户ID映射 [key={}, userId={}]", idCacheKey, user.getId());

        return buildUserPrincipal(user);
    }

    // 按 ID 加载（自定义扩展）
    @Override
    public UserDetails loadUserById(Long userId) {
        // 1. 检查认证信息缓存
        String authKey = CacheConstants.USER_AUTH_CACHE_PREFIX + userId;
        UserPrincipal principal = (UserPrincipal) redisTemplate.opsForValue().get(authKey);

        if (principal != null) {
            log.debug("认证缓存命中 [key={}]", authKey);
            System.out.println(principal);
            return principal;
        }
        log.debug("认证缓存未命中 [key={}]", authKey);

        // 2. 检查业务数据缓存
        String dataKey = CacheConstants.USER_DATA_CACHE_PREFIX + userId;
        User user = (User) redisTemplate.opsForValue().get(dataKey);

        if (user != null) {
            log.debug("业务数据缓存命中 [key={}]", dataKey);
            return buildUserPrincipal(user);
        }
        log.debug("业务数据缓存未命中 [key={}]", dataKey);

        // 3. 查询数据库（缓存未命中）
        log.debug("查询数据库获取用户ID: {}", userId);
        user = userService.selectById(userId);

        if (user == null) {
            log.error("用户不存在: {}", userId);
            throw new UsernameNotFoundException("用户不存在: " + userId);
        }
        log.debug("数据库查询成功 [userId={}]", userId);

        // 4. 缓存业务数据
        redisTemplate.opsForValue().set(dataKey, user, 30, TimeUnit.MINUTES);
        log.debug("缓存业务数据 [key={}]", dataKey);

        return buildUserPrincipal(user);
    }

    private UserDetails buildUserPrincipal(User user) {
        // 1. 状态验证
        if (!"ACTIVE".equals(user.getStatus())) {
            log.warn("用户状态异常: userId={}, status={}", user.getId(), user.getStatus());
            throw new DisabledException("用户状态异常: " + user.getStatus());
        }

        // 2. 加载权限
        log.debug("加载用户权限: userId={}", user.getId());
        List<Permission> permissions = permissionMapper.selectByUserId(user.getId());

        if (permissions.isEmpty()) {
            log.warn("用户没有分配权限: userId={}", user.getId());
        } else {
            log.debug("加载到 {} 条权限: userId={}", permissions.size(), user.getId());
        }

        Collection<GrantedAuthority> authorities = permissions.stream()
                .map(p -> new SimpleGrantedAuthority(p.getCode()))
                .collect(Collectors.toList());

        // 3. 构建用户主体
        UserPrincipal principal = new UserPrincipal(user, authorities);
        log.debug("用户主体构建完成: userId={}", user.getId());

        // 4. 缓存认证信息
        String cacheKey = CacheConstants.USER_AUTH_CACHE_PREFIX + user.getId();
        redisTemplate.opsForValue().set(cacheKey, principal, 30, TimeUnit.MINUTES);
        log.debug("缓存认证信息：key={}", cacheKey);

        return principal;
    }

}
