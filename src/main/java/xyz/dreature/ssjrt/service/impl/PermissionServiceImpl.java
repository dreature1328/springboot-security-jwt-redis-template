package xyz.dreature.ssjrt.service.impl;

import org.springframework.stereotype.Service;
import xyz.dreature.ssjrt.common.entity.Permission;
import xyz.dreature.ssjrt.mapper.PermissionMapper;
import xyz.dreature.ssjrt.service.PermissionService;

import java.util.List;

@Service
public class PermissionServiceImpl extends BaseServiceImpl<Permission, Short, PermissionMapper> implements PermissionService {
    // ===== 业务扩展操作 =====
    // 按码检查
    public boolean existsByCode(String code) {
        return mapper.existsByCode(code);
    }

    // 按码查询
    public Permission selectByCode(String code) {
        return mapper.selectByCode(code);
    }

    // 按名查询
    public Permission selectByName(String name) {
        return mapper.selectByName(name);
    }

    // 按类查询
    public List<Permission> selectByCategory(String category) {
        return mapper.selectByCategory(category);
    }

    // ===== 多表关联操作 =====
    // 按用户 ID 查询
    public List<Permission> selectByUserId(Long userId) {
        return mapper.selectByUserId(userId);
    }

    // 按角色 ID 查询
    public List<Permission> selectByRoleId(Short roleId) {
        return mapper.selectByRoleId(roleId);
    }
}
