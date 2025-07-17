package xyz.dreature.ssjrt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.dreature.ssjrt.common.entity.RolePermission;
import xyz.dreature.ssjrt.common.vo.Result;
import xyz.dreature.ssjrt.service.RolePermissionService;

import java.io.Serializable;
import java.util.List;

@RestController
@RequestMapping("/role-permission")
public class RolePermissionController extends BaseController<RolePermission, Serializable, RolePermissionService> {
    // ===== 业务扩展操作 =====
    // 按角色 ID 查询
    @RequestMapping("/select-by-role-id")
    public ResponseEntity<Result<List<RolePermission>>> selectByRoleId(
            @RequestParam Short roleId) {
        List<RolePermission> result = service.selectByRoleId(roleId);
        return ResponseEntity.ok(Result.success(
                String.format("找到角色 %d 的 %d 条权限关联", roleId, result.size()),
                result
        ));
    }

    // 按权限 ID 查询
    @RequestMapping("/select-by-permission-id")
    public ResponseEntity<Result<List<RolePermission>>> selectByPermissionId(
            @RequestParam Short permissionId) {
        List<RolePermission> result = service.selectByPermissionId(permissionId);
        return ResponseEntity.ok(Result.success(
                String.format("找到权限 %d 的 %d 条角色关联", permissionId, result.size()),
                result
        ));
    }

    // 按角色 ID 和权限 ID 查询
    @RequestMapping("/select-by-role-id-and-permission-id")
    public ResponseEntity<Result<List<RolePermission>>> selectByRoleIdAndPermissionId(
            @RequestParam Short roleId,
            @RequestParam Short permissionId) {
        List<RolePermission> result = service.selectByRoleIdAndPermissionId(roleId, permissionId);
        return ResponseEntity.ok(Result.success(
                String.format("找到 %d 条匹配记录", result.size()),
                result
        ));
    }

    // 按权限 ID 查询角色 ID
    @RequestMapping("/select-role-ids-by-permission-id")
    public ResponseEntity<Result<List<Short>>> selectRoleIdsByPermissionId(
            @RequestParam Short permissionId) {
        List<Short> result = service.selectRoleIdsByPermissionId(permissionId);
        return ResponseEntity.ok(Result.success(
                String.format("权限 %d 关联了 %d 个角色", permissionId, result.size()),
                result
        ));
    }

    // 按角色 ID 查询权限 ID
    @RequestMapping("/select-permission-ids-by-role-id")
    public ResponseEntity<Result<List<Short>>> selectPermissionIdsByRoleId(
            @RequestParam Short roleId) {
        List<Short> result = service.selectPermissionIdsByRoleId(roleId);
        return ResponseEntity.ok(Result.success(
                String.format("角色 %d 拥有 %d 项权限", roleId, result.size()),
                result
        ));
    }

    // 按角色 ID 删除
    @RequestMapping("/delete-by-role-id")
    public ResponseEntity<Result<Integer>> deleteByRoleId(
            @RequestParam Short roleId) {
        int affectedRows = service.deleteByRoleId(roleId);
        return ResponseEntity.ok(Result.success(
                String.format("删除角色 %d 的 %d 条权限关联", roleId, affectedRows),
                null
        ));
    }

    // 按权限 ID 删除
    @RequestMapping("/delete-by-permission-id")
    public ResponseEntity<Result<Integer>> deleteByPermissionId(
            @RequestParam Short permissionId) {
        int affectedRows = service.deleteByPermissionId(permissionId);
        return ResponseEntity.ok(Result.success(
                String.format("删除权限 %d 的 %d 条角色关联", permissionId, affectedRows),
                null
        ));
    }

    // 按权限 ID 和角色 ID 删除
    @RequestMapping("/delete-by-role-id-and-permission-id")
    public ResponseEntity<Result<Integer>> deleteByRoleIdAndPermissionId(
            @RequestParam Short roleId,
            @RequestParam Short permissionId) {
        int affectedRows = service.deleteByRoleIdAndPermissionId(roleId, permissionId);
        return ResponseEntity.ok(Result.success(
                String.format("删除 %d 条关联记录", affectedRows),
                null
        ));
    }
}
