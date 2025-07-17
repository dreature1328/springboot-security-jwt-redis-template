package xyz.dreature.ssjrt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.dreature.ssjrt.common.entity.UserRole;
import xyz.dreature.ssjrt.common.vo.Result;
import xyz.dreature.ssjrt.service.UserRoleService;

import java.io.Serializable;
import java.util.List;

@RestController
@RequestMapping("/user-role")
public class UserRoleController extends BaseController<UserRole, Serializable, UserRoleService> {
    // ===== 业务扩展操作 =====
    // 按用户 ID 查询
    @RequestMapping("/select-by-user-id")
    public ResponseEntity<Result<List<UserRole>>> selectByUserId(
            @RequestParam Long userId) {
        List<UserRole> result = service.selectByUserId(userId);
        return ResponseEntity.ok(Result.success(
                String.format("找到用户 %d 的 %d 条角色关联", userId, result.size()),
                result
        ));
    }

    // 按角色 ID 查询
    @RequestMapping("/select-by-role-id")
    public ResponseEntity<Result<List<UserRole>>> selectByRoleId(
            @RequestParam Short roleId) {
        List<UserRole> result = service.selectByRoleId(roleId);
        return ResponseEntity.ok(Result.success(
                String.format("找到角色 %d 的 %d 条用户关联", roleId, result.size()),
                result
        ));
    }

    // 按用户 ID 和角色 ID 查询
    @RequestMapping("/select-by-user-id-and-role-id")
    public ResponseEntity<Result<List<UserRole>>> selectByUserIdAndRoleId(
            @RequestParam Long userId,
            @RequestParam Short roleId) {
        List<UserRole> result = service.selectByUserIdAndRoleId(userId, roleId);
        return ResponseEntity.ok(Result.success(
                String.format("找到 %d 条匹配记录", result.size()),
                result
        ));
    }

    // 按角色 ID 查询用户 ID
    @RequestMapping("/select-user-ids-by-role-id")
    public ResponseEntity<Result<List<Long>>> selectUserIdsByRoleId(
            @RequestParam Short roleId) {
        List<Long> result = service.selectUserIdsByRoleId(roleId);
        return ResponseEntity.ok(Result.success(
                String.format("角色 %d 关联了 %d 个用户", roleId, result.size()),
                result
        ));
    }

    // 按用户 ID 查询角色 ID
    @RequestMapping("/select-role-ids-by-user-id")
    public ResponseEntity<Result<List<Short>>> selectRoleIdsByUserId(
            @RequestParam Long userId) {
        List<Short> result = service.selectRoleIdsByUserId(userId);
        return ResponseEntity.ok(Result.success(
                String.format("用户 %d 拥有 %d 个角色", userId, result.size()),
                result
        ));
    }

    // 按用户 ID 删除
    @RequestMapping("/delete-by-user-id")
    public ResponseEntity<Result<Integer>> deleteByUserId(
            @RequestParam Long userId) {
        int affectedRows = service.deleteByUserId(userId);
        return ResponseEntity.ok(Result.success(
                String.format("删除用户 %d 的 %d 条角色关联", userId, affectedRows),
                null
        ));
    }

    // 按角色 ID 删除
    @RequestMapping("/delete-by-role-id")
    public ResponseEntity<Result<Integer>> deleteByRoleId(
            @RequestParam Short roleId) {
        int affectedRows = service.deleteByRoleId(roleId);
        return ResponseEntity.ok(Result.success(
                String.format("删除角色 %d 的 %d 条用户关联", roleId, affectedRows),
                null
        ));
    }

    // 按用户 ID 和角色 ID 删除
    @RequestMapping("/delete-by-user-id-and-role-id")
    public ResponseEntity<Result<Integer>> deleteByUserIdAndRoleId(
            @RequestParam Long userId,
            @RequestParam Short roleId) {
        int affectedRows = service.deleteByUserIdAndRoleId(userId, roleId);
        return ResponseEntity.ok(Result.success(
                String.format("删除 %d 条关联记录", affectedRows),
                null
        ));
    }
}
