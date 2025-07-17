package xyz.dreature.ssjrt.mapper;

import xyz.dreature.ssjrt.common.entity.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission, Short> {
    // ===== 业务扩展操作 =====
    // 按码检查
    boolean existsByCode(String code);

    // 按码查询
    Permission selectByCode(String code);

    // 按名查询
    Permission selectByName(String name);

    // 按类查询
    List<Permission> selectByCategory(String category);

    // ===== 多表关联操作 =====
    // 按用户 ID 查询
    List<Permission> selectByUserId(Long userId);

    // 按角色 ID 查询
    List<Permission> selectByRoleId(Short roleId);
}
