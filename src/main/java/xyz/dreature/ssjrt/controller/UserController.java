package xyz.dreature.ssjrt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.dreature.ssjrt.common.entity.User;
import xyz.dreature.ssjrt.common.util.IdUtils;
import xyz.dreature.ssjrt.common.vo.Result;
import xyz.dreature.ssjrt.service.UserService;

import java.util.Date;
import java.util.List;

// 测试接口（用户数据）
@RestController
@RequestMapping("/user")
public class UserController extends BaseController<User, Long, UserService> {
    // ===== 检查扩展操作 =====
    // 按 UUID 检查
    @RequestMapping("/exists-by-uuid")
    public ResponseEntity<Result<Boolean>> existsByUuid(
            @RequestParam String uuid
    ) {
        boolean exists = service.existsByUuid(uuid);
        String message = exists ?
                "UUID " + uuid + " 已存在" :
                "UUID " + uuid + " 不存在";
        return ResponseEntity.ok().body(Result.success(message, exists));
    }

    // 按用户名检查
    @RequestMapping("/exists-by-username")
    public ResponseEntity<Result<Boolean>> existsByUsername(
            @RequestParam String username
    ) {
        boolean exists = service.existsByUsername(username);
        String message = exists ?
                "用户名 " + username + " 已存在" :
                "用户名 " + username + " 不存在";
        return ResponseEntity.ok().body(Result.success(message, exists));
    }

    // 按电子邮件查询
    @RequestMapping("/exists-by-email")
    public ResponseEntity<Result<Boolean>> existsByEmail(
            @RequestParam String email
    ) {
        boolean exists = service.existsByEmail(email);
        String message = exists ?
                "邮箱 " + email + " 已存在" :
                "邮箱 " + email + " 不存在";
        return ResponseEntity.ok().body(Result.success(message, exists));
    }

    // 按电话号码检查
    @RequestMapping("/exists-by-phone")
    public ResponseEntity<Result<Boolean>> existsByPhone(
            @RequestParam String phone
    ) {
        boolean exists = service.existsByPhone(phone);
        String message = exists ?
                "电话 " + phone + " 已存在" :
                "电话 " + phone + " 不存在";
        return ResponseEntity.ok().body(Result.success(message, exists));
    }

    // ===== 查询扩展操作 =====
    // 逐项查询
    @RequestMapping("/select-by-ids")
    public ResponseEntity<Result<List<User>>> selectByIds(
            @RequestParam(name = "ids") String ids
    ) {
        List<User> result = service.selectByIds(IdUtils.parseLongIds(ids).toArray(new Long[0]));
        int resultCount = result.size();
        String message = String.format("逐项查询 %d 条数据", resultCount);
        return ResponseEntity.ok().body(Result.success(message, result));
    }

    // 按 UUID 查询
    @RequestMapping("/select-by-uuid")
    public ResponseEntity<Result<User>> selectByUuid(
            @RequestParam String uuid
    ) {
        User result = service.selectByUuid(uuid);
        return ResponseEntity.ok().body(Result.success("用户查询成功", result));
    }

    // 按用户名查询
    @RequestMapping("/select-by-username")
    public ResponseEntity<Result<User>> selectByUsername(
            @RequestParam String username
    ) {
        User result = service.selectByUsername(username);
        return ResponseEntity.ok().body(Result.success("用户查询成功", result));
    }

    // 按电子邮箱查询
    @RequestMapping("/select-by-email")
    public ResponseEntity<Result<User>> selectByEmail(
            @RequestParam String email
    ) {
        User result = service.selectByEmail(email);
        return ResponseEntity.ok().body(Result.success("用户查询成功", result));
    }

    // 按电话号码查询
    @RequestMapping("/select-by-phone")
    public ResponseEntity<Result<User>> selectByPhone(
            @RequestParam String phone
    ) {
        User result = service.selectByPhone(phone);
        return ResponseEntity.ok().body(Result.success("用户查询成功", result));
    }

    // ===== 更新扩展操作 =====
    // 更新状态
    @RequestMapping("/update-status")
    public ResponseEntity<Result<Integer>> updateStatus(
            @RequestParam Long id,
            @RequestParam String status
    ) {
        int affectedRows = service.updateStatus(id, status);
        return ResponseEntity.ok().body(Result.success("用户状态更新成功", affectedRows));
    }

    // 更新登录时间
    @RequestMapping("/update-last-login")
    public ResponseEntity<Result<Integer>> updateLastLogin(
            @RequestParam Long id,
            @RequestParam Date loginTime
    ) {
        int affectedRows = service.updateLastLogin(id, loginTime);
        return ResponseEntity.ok().body(Result.success("登录时间更新成功", affectedRows));
    }

    // ===== 业务扩展操作 =====
    // 按角色 ID 查询
    @RequestMapping("/select-by-role-id")
    public ResponseEntity<Result<List<User>>> selectByRoleId(
            @RequestParam Short roleId
    ) {
        List<User> result = service.selectByRoleId(roleId);
        int resultCount = result.size();
        String message = String.format("查询 %d 条数据", resultCount);
        return ResponseEntity.ok().body(Result.success(message, result));
    }

    // 按权限 ID 查询
    @RequestMapping("/select-by-permission-id")
    public ResponseEntity<Result<List<User>>> selectByPermissionId(
            @RequestParam Short permissionId
    ) {
        List<User> result = service.selectByPermissionId(permissionId);
        int resultCount = result.size();
        String message = String.format("查询 %d 条数据", resultCount);
        return ResponseEntity.ok().body(Result.success(message, result));
    }
}
