package xyz.dreature.ssjrt.service;

import xyz.dreature.ssjrt.common.entity.Role;

import java.util.List;

public interface RoleService extends BaseService<Role, Short> {
    // ===== 业务扩展操作 =====
    // 按码检查
    boolean existsByCode(String code);

    // 按码查询
    Role selectByCode(String code);

    // 按名查询
    Role selectByName(String name);

    // 按类查询
    List<Role> selectBySystem(Boolean system);

    // ===== 多表关联操作 =====
    // 按用户 ID 查询
    List<Role> selectByUserId(Long userId);

    // 按权限 ID 查询
    List<Role> selectByPermissionId(Short permissionId);
}
