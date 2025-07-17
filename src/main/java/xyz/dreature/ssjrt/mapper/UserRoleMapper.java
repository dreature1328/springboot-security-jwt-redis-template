package xyz.dreature.ssjrt.mapper;

import xyz.dreature.ssjrt.common.entity.UserRole;

import java.io.Serializable;
import java.util.List;

public interface UserRoleMapper extends BaseMapper<UserRole, Serializable> {
    // ===== 业务扩展操作 =====
    // 按用户 ID 查询
    List<UserRole> selectByUserId(Long userId);

    // 按角色 ID 查询
    List<UserRole> selectByRoleId(Short roleId);

    // 按用户 ID 和角色 ID 查询
    List<UserRole> selectByUserIdAndRoleId(Long userId, Short roleId);

    // 按角色 ID 查询用户 ID
    List<Long> selectUserIdsByRoleId(Short roleId);

    // 按用户 ID 查询角色 ID
    List<Short> selectRoleIdsByUserId(Long userId);

    // 按用户 ID 删除
    int deleteByUserId(Long userId);

    // 按角色 ID 删除
    int deleteByRoleId(Short roleId);

    // 按用户 ID 和角色 ID 删除
    int deleteByUserIdAndRoleId(Long userId, Short roleId);
}
