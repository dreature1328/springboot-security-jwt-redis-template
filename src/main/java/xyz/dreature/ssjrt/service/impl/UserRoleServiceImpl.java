package xyz.dreature.ssjrt.service.impl;

import org.springframework.stereotype.Service;
import xyz.dreature.ssjrt.common.entity.UserRole;
import xyz.dreature.ssjrt.mapper.UserRoleMapper;
import xyz.dreature.ssjrt.service.UserRoleService;

import java.io.Serializable;
import java.util.List;

@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole, Serializable, UserRoleMapper> implements UserRoleService {
    // ===== 业务扩展操作 =====
    // 按用户 ID 查询
    public List<UserRole> selectByUserId(Long userId) {
        return mapper.selectByUserId(userId);
    }

    // 按角色 ID 查询
    public List<UserRole> selectByRoleId(Short roleId) {
        return mapper.selectByRoleId(roleId);
    }

    // 按用户 ID 和 角色 ID 查询
    public List<UserRole> selectByUserIdAndRoleId(Long userId, Short roleId) {
        return mapper.selectByUserIdAndRoleId(userId, roleId);
    }

    // 按角色 ID 查询用户 ID
    public List<Long> selectUserIdsByRoleId(Short roleId) {
        return mapper.selectUserIdsByRoleId(roleId);
    }

    // 按用户 ID 查询角色 ID
    public List<Short> selectRoleIdsByUserId(Long userId) {
        return mapper.selectRoleIdsByUserId(userId);
    }


    // 按角色 ID 删除
    public int deleteByRoleId(Short roleId) {
        return mapper.deleteByRoleId(roleId);
    }

    // 按用户 ID 删除
    public int deleteByUserId(Long userId) {
        return mapper.deleteByUserId(userId);
    }

    // 按角色 ID 和用户 ID 删除
    public int deleteByUserIdAndRoleId(Long userId, Short roleId) {
        return mapper.deleteByUserIdAndRoleId(userId, roleId);
    }
}
