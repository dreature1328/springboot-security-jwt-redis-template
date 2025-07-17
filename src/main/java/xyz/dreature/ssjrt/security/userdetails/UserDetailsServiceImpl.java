package xyz.dreature.ssjrt.security.userdetails;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xyz.dreature.ssjrt.common.entity.Permission;
import xyz.dreature.ssjrt.common.entity.User;
import xyz.dreature.ssjrt.mapper.PermissionMapper;
import xyz.dreature.ssjrt.mapper.UserMapper;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserMapper userMapper;
    private final PermissionMapper permissionMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    public UserDetailsServiceImpl(UserMapper userMapper,
                                  PermissionMapper permissionMapper,
                                  RedisTemplate<String, Object> redisTemplate) {
        this.userMapper = userMapper;
        this.permissionMapper = permissionMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. 检查 Redis 缓存
        String cacheKey = "user:" + username;
        User user = (User) redisTemplate.opsForValue().get(cacheKey);

        // 2. 数据库查询（缓存未命中）
        if (user == null) {
            user = userMapper.selectByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("用户不存在: " + username);
            }
            // 缓存 30 分钟
            redisTemplate.opsForValue().set(cacheKey, user, 30, TimeUnit.MINUTES);
        }

        // 3. 加载权限
        List<Permission> permissions = permissionMapper.selectByUserId(user.getId());

        Collection<GrantedAuthority> authorities = permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getCode()))
                .collect(Collectors.toList());

        // 4. 状态验证
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new DisabledException("用户状态异常: " + user.getStatus());
        }

        return new SecurityUser(user, authorities);
    }
}
