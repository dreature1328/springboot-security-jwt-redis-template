package xyz.dreature.ssjrt.service.impl;

import org.springframework.stereotype.Service;
import xyz.dreature.ssjrt.common.entity.Role;
import xyz.dreature.ssjrt.mapper.RoleMapper;
import xyz.dreature.ssjrt.service.RoleService;

import java.util.List;

@Service
public class RoleServiceImpl extends BaseServiceImpl<Role, Short, RoleMapper> implements RoleService {
    // ===== 业务扩展操作 =====
    // 按码检查
    public boolean existsByCode(String code) {
        return mapper.existsByCode(code);
    }

    // 按码查询
    public Role selectByCode(String code) {
        return mapper.selectByCode(code);
    }

    // 按名查询
    public Role selectByName(String name) {
        return mapper.selectByName(name);
    }

    // 按类查询
    public List<Role> selectBySystem(Boolean system) {
        return mapper.selectBySystem(system);
    }

    // ===== 多表关联操作 =====
    // 按用户 ID 查询
    public List<Role> selectByUserId(Long userId) {
        return mapper.selectByUserId(userId);
    }

    // 按角色 ID 查询
    public List<Role> selectByPermissionId(Short permissionId) {
        return mapper.selectByPermissionId(permissionId);
    }
}
