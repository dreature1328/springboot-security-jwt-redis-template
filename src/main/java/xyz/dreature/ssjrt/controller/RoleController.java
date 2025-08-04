package xyz.dreature.ssjrt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.dreature.ssjrt.common.entity.Role;
import xyz.dreature.ssjrt.common.util.IdUtils;
import xyz.dreature.ssjrt.common.vo.Result;
import xyz.dreature.ssjrt.service.RoleService;

import java.util.List;

// 测试接口（角色数据）
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController<Role, Short, RoleService> {
    // ===== 业务扩展操作 =====
    // 逐项查询
    @RequestMapping("/select-by-ids")
    public ResponseEntity<Result<List<Role>>> selectByIds(
            @RequestParam(name = "ids") String ids
    ) {
        List<Role> result = service.selectByIds(IdUtils.parseShortIds(ids).toArray(new Short[0]));
        int resultCount = result.size();
        String message = String.format("逐项查询 %d 条数据", resultCount);
        return ResponseEntity.ok().body(Result.success(message, result));
    }

    // 按码查询
    @RequestMapping("/select-by-code")
    public ResponseEntity<Result<Role>> selectByCode(
            @RequestParam String code
    ) {
        Role result = service.selectByCode(code);
        return ResponseEntity.ok().body(Result.success("角色查询成功", result));
    }

    // 按名查询
    @RequestMapping("/select-by-name")
    public ResponseEntity<Result<Role>> selectByName(
            @RequestParam String name
    ) {
        Role result = service.selectByName(name);
        return ResponseEntity.ok().body(Result.success("角色查询成功", result));
    }

    // 按类查询
    @RequestMapping("/select-by-system")
    public ResponseEntity<Result<List<Role>>> selectBySystem(
            @RequestParam Boolean system
    ) {
        List<Role> result = service.selectBySystem(system);
        int resultCount = result.size();
        String roleType = system ? "系统角色" : "非系统角色";
        String message = String.format("找到 %d 个%s", resultCount, roleType);
        return ResponseEntity.ok().body(Result.success(message, result));
    }

    // ===== 多表关联操作 =====
    // 按用户 ID 查询
    @RequestMapping("/select-by-user-id")
    public ResponseEntity<Result<List<Role>>> selectByUserId(
            @RequestParam Long userId
    ) {
        List<Role> result = service.selectByUserId(userId);
        int resultCount = result.size();
        String message = String.format("查询 %d 条数据", resultCount);
        return ResponseEntity.ok().body(Result.success(message, result));
    }

    // 按权限 ID 查询
    @RequestMapping("/select-by-permission-id")
    public ResponseEntity<Result<List<Role>>> selectByPermissionId(
            @RequestParam Short permissionId
    ) {
        List<Role> result = service.selectByPermissionId(permissionId);
        int resultCount = result.size();
        String message = String.format("查询 %d 条数据", resultCount);
        return ResponseEntity.ok().body(Result.success(message, result));
    }
}
