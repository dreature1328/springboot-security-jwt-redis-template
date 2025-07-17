package xyz.dreature.ssjrt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.dreature.ssjrt.common.entity.Permission;
import xyz.dreature.ssjrt.common.util.IdUtils;
import xyz.dreature.ssjrt.common.vo.Result;
import xyz.dreature.ssjrt.service.PermissionService;

import java.util.List;

// 测试接口（权限数据）
@RestController
@RequestMapping("/permission")
public class PermissionController extends BaseController<Permission, Short, PermissionService> {
    // ===== 业务扩展操作 =====
    // 逐项查询
    @RequestMapping("/select-ids")
    public ResponseEntity<Result<List<Permission>>> selectByIds(
            @RequestParam(name = "ids") String ids
    ) {
        List<Permission> result = service.selectByIds(IdUtils.parseShortIds(ids).toArray(new Short[0]));
        int resultCount = result.size();
        String message = String.format("逐项查询 %d 条数据", resultCount);
        return ResponseEntity.ok().body(Result.success(message, result));
    }

    // 按码查询
    @RequestMapping("/select-by-code")
    public ResponseEntity<Result<Permission>> selectByCode(
            @RequestParam String code
    ) {
        Permission result = service.selectByCode(code);
        return ResponseEntity.ok().body(Result.success("权限查询成功", result));
    }

    // 按名查询
    @RequestMapping("/select-by-name")
    public ResponseEntity<Result<Permission>> selectByName(
            @RequestParam String name
    ) {
        Permission result = service.selectByName(name);
        return ResponseEntity.ok().body(Result.success("权限查询成功", result));
    }

    // 按类查询
    @RequestMapping("/select-by-category")
    public ResponseEntity<Result<List<Permission>>> selectByCategory(
            @RequestParam String category
    ) {
        List<Permission> result = service.selectByCategory(category);
        int resultCount = result.size();
        String message = String.format("在分类 '%s' 下找到 %d 条权限", category, resultCount);
        return ResponseEntity.ok().body(Result.success(message, result));
    }

    // ===== 多表关联操作 =====
    // 按用户 ID 查询
    @RequestMapping("/select-by-user-id")
    public ResponseEntity<Result<List<Permission>>> selectByUserId(
            @RequestParam Long userId
    ) {
        List<Permission> result = service.selectByUserId(userId);
        int resultCount = result.size();
        String message = String.format("查询 %d 条数据", resultCount);
        return ResponseEntity.ok().body(Result.success(message, result));
    }

    // 按角色 ID 查询
    @RequestMapping("/select-by-role-id")
    public ResponseEntity<Result<List<Permission>>> selectByRoleId(
            @RequestParam Short roleId
    ) {
        List<Permission> result = service.selectByRoleId(roleId);
        int resultCount = result.size();
        String message = String.format("查询 %d 条数据", resultCount);
        return ResponseEntity.ok().body(Result.success(message, result));
    }
}
