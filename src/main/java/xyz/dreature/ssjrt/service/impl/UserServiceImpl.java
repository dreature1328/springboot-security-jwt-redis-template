package xyz.dreature.ssjrt.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import xyz.dreature.ssjrt.common.constant.CacheConstants;
import xyz.dreature.ssjrt.common.entity.User;
import xyz.dreature.ssjrt.mapper.UserMapper;
import xyz.dreature.ssjrt.service.UserService;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends BaseServiceImpl<User, Long, UserMapper> implements UserService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // ===== 检查扩展操作 =====
    // 按 UUID 检查
    public boolean existsByUuid(String uuid) {
        return mapper.existsByUuid(uuid);
    }

    // 按用户名检查
    public boolean existsByUsername(String username) {
        return mapper.existsByUsername(username);
    }

    // 按电子邮箱检查
    public boolean existsByEmail(String email) {
        return mapper.existsByEmail(email);
    }

    // 按电话号码检查
    public boolean existsByPhone(String phone) {
        return mapper.existsByPhone(phone);
    }

    // ===== 查询扩展操作 =====
    // 按 ID 查询
    public User selectById(Long id) {
        // 1. 检查用户业务数据缓存
        String dataKey = CacheConstants.USER_DATA_CACHE_PREFIX + id;
        User user = (User) redisTemplate.opsForValue().get(dataKey);

        // 2. 查询数据库（业务缓存未命中）
        if (user == null) {
            user = mapper.selectById(id);
            if (user != null) {
                redisTemplate.opsForValue().set(dataKey, user, 30, TimeUnit.MINUTES);
            } else {
                // 缓存空值防止穿透
                redisTemplate.opsForValue().set(dataKey, "NULL", 5, TimeUnit.MINUTES);
            }
        } else if ("NULL".equals(user)) {
            return null;
        }
        return user;
    }

    // 按 UUID 查询
    public User selectByUuid(String uuid) {
        return mapper.selectByUuid(uuid);
    }

    // 按用户名查询
    public User selectByUsername(String username) {
        return mapper.selectByUsername(username);
    }

    // 按电子邮箱查询
    public User selectByEmail(String email) {
        return mapper.selectByEmail(email);
    }

    // 按电话号码查询
    public User selectByPhone(String phone) {
        return mapper.selectByPhone(phone);
    }

    // ===== 更新扩展操作 =====
    // 按 ID 查询
    public int update(User user) {
        // 1. 更新数据库
        int affectedRows = mapper.update(user);

        if (affectedRows > 0) {
            // 2. 更新业务缓存
            String dataKey = CacheConstants.USER_DATA_CACHE_PREFIX + user.getId();
            redisTemplate.opsForValue().set(dataKey, user, 30, TimeUnit.MINUTES);

            // 3. 清除认证缓存
            String authKey = CacheConstants.USER_AUTH_CACHE_PREFIX + user.getId();
            redisTemplate.delete(authKey);
        }

        return affectedRows;
    }

    // 更新状态
    public int updateStatus(Long id, String status) {
        return mapper.updateStatus(id, status);
    }

    // 更新登录时间
    public int updateLastLogin(Long id, Date loginTime) {
        return mapper.updateLastLogin(id, loginTime);
    }

    // ===== 多表关联操作 =====
    // 按角色 ID 查询
    public List<User> selectByRoleId(Short roleId) {
        return mapper.selectByRoleId(roleId);
    }

    // 按权限 ID 查询
    public List<User> selectByPermissionId(Short permissionId) {
        return mapper.selectByPermissionId(permissionId);
    }
}
