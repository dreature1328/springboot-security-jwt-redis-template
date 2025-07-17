package xyz.dreature.ssjrt.mapper;

import xyz.dreature.ssjrt.common.entity.User;

import java.util.Date;
import java.util.List;

public interface UserMapper extends BaseMapper<User, Long> {
    // ===== 检查扩展操作 =====
    // 按 UUID 检查
    boolean existsByUuid(String uuid);

    // 按用户名检查
    boolean existsByUsername(String username);

    // 按电子邮箱检查
    boolean existsByEmail(String email);

    // 按电话号码检查
    boolean existsByPhone(String phone);

    // ===== 查询扩展操作 =====
    // 按 UUID 查询
    User selectByUuid(String uuid);

    // 按用户名查询
    User selectByUsername(String username);

    // 按电子邮箱查询
    User selectByEmail(String email);

    // 按电话号码查询
    User selectByPhone(String phone);

    // ===== 更新扩展操作 =====
    // 更新状态
    int updateStatus(Long id, String status);

    // 更新登录时间
    int updateLastLogin(Long id, Date loginTime);

    // ===== 多表关联操作 =====
    // 按角色 ID 查询
    List<User> selectByRoleId(Short roleId);

    // 按权限 ID 查询
    List<User> selectByPermissionId(Short permissionId);
}
