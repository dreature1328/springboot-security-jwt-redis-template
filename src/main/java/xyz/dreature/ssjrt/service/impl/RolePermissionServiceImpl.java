package xyz.dreature.ssjrt.service.impl;

import org.springframework.stereotype.Service;
import xyz.dreature.ssjrt.common.entity.RolePermission;
import xyz.dreature.ssjrt.mapper.RolePermissionMapper;
import xyz.dreature.ssjrt.service.RolePermissionService;

import java.io.Serializable;
import java.util.List;

@Service
public class RolePermissionServiceImpl extends BaseServiceImpl<RolePermission, Serializable, RolePermissionMapper> implements RolePermissionService {
    // ===== 业务扩展操作 =====
    // 按角色 ID 查询
    public List<RolePermission> selectByRoleId(Short roleId) {
        return mapper.selectByRoleId(roleId);
    }

    // 按权限 ID 查询
    public List<RolePermission> selectByPermissionId(Short permissionId) {
        return mapper.selectByPermissionId(permissionId);
    }

    // 按角色 ID 和权限 ID 查询
    public List<RolePermission> selectByRoleIdAndPermissionId(Short roleId, Short permissionId) {
        return mapper.selectByRoleIdAndPermissionId(roleId, permissionId);
    }

    // 按权限 ID 查询角色 ID
    public List<Short> selectRoleIdsByPermissionId(Short permissionId) {
        return mapper.selectRoleIdsByPermissionId(permissionId);
    }

    // 按角色 ID 查询权限 ID
    public List<Short> selectPermissionIdsByRoleId(Short roleId) {
        return mapper.selectPermissionIdsByRoleId(roleId);
    }

    // 按角色 ID 删除
    public int deleteByRoleId(Short roleId) {
        return mapper.deleteByRoleId(roleId);
    }

    // 按权限 ID 删除
    public int deleteByPermissionId(Short permissionId) {
        return mapper.deleteByPermissionId(permissionId);
    }

    // 按角色 ID 和权限 ID 删除
    public int deleteByRoleIdAndPermissionId(Short roleId, Short permissionId) {
        return mapper.deleteByPermissionIdAndRoleId(roleId, permissionId);
    }
}
