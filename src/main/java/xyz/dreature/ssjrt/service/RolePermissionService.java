package xyz.dreature.ssjrt.service;

import xyz.dreature.ssjrt.common.entity.RolePermission;

import java.io.Serializable;
import java.util.List;

public interface RolePermissionService extends BaseService<RolePermission, Serializable> {
    // ===== 业务扩展操作 =====
    // 按角色 ID 查询
    List<RolePermission> selectByRoleId(Short roleId);

    // 按权限 ID 查询
    List<RolePermission> selectByPermissionId(Short permissionId);

    // 按角色 ID 和权限 ID 查询
    List<RolePermission> selectByRoleIdAndPermissionId(Short roleId, Short permissionId);

    // 按权限 ID 查询角色 ID
    List<Short> selectRoleIdsByPermissionId(Short permissionId);

    // 按角色 ID 查询权限 ID
    List<Short> selectPermissionIdsByRoleId(Short roleId);

    // 按角色 ID 删除
    int deleteByRoleId(Short roleId);

    // 按权限 ID 删除
    int deleteByPermissionId(Short permissionId);

    // 按角色 ID 和 权限 ID删除
    int deleteByRoleIdAndPermissionId(Short roleId, Short PermissionId);
}
