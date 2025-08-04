package xyz.dreature.ssjrt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.dreature.ssjrt.common.dto.LoginRequest;
import xyz.dreature.ssjrt.common.dto.RegisterRequest;
import xyz.dreature.ssjrt.common.entity.User;
import xyz.dreature.ssjrt.common.vo.Result;
import xyz.dreature.ssjrt.mapper.UserMapper;
import xyz.dreature.ssjrt.security.jwt.JwtService;
import xyz.dreature.ssjrt.security.userdetails.UserPrincipal;

import javax.servlet.http.HttpServletRequest;

// 接口（安全认证）
@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 登录用户
    @PostMapping("/login")
    public ResponseEntity<Result<String>> login(@RequestBody LoginRequest loginRequest) {
        try {
            // 1. 创建 Authentication 对象
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
            );

            // 2. 认证
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 3. 将认证信息存入安全上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 4. 生成令牌
            String token = jwtService.generateToken(authentication);

            // 5. 返回令牌
            return ResponseEntity.ok(Result.success("登录成功", token));

        } catch (BadCredentialsException e) {
            return ResponseEntity.ok(Result.error("ERROR", "用户名或密码错误"));
        }
    }

    // 注册用户
    @PostMapping("/register")
    public ResponseEntity<Result<Void>> register(@RequestBody RegisterRequest registerRequest) {
        // 1. 检查用户名是否已存在
        if (userMapper.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.ok(Result.error("ERROR_USER_EXISTS", "用户名已存在"));
        }

        // 2. 创建新用户
        userMapper.insert(new User(
                registerRequest.getUsername(),
                passwordEncoder.encode(registerRequest.getPassword())
        ));

        // 3. 清除缓存（如果存在）
        String cacheKey = "user:" + registerRequest.getUsername();
        redisTemplate.delete(cacheKey);

        // 4. 返回成功
        return ResponseEntity.ok(Result.success("注册成功", null));
    }

    // 获取当前用户
    @RequestMapping("/me")
    public ResponseEntity<Result<UserPrincipal>> getCurrentUser() {
        // 1. 从安全上下文中获取认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.ok(Result.error("ERROR_UNAUTHORIZED", "未认证"));
        }

        // 2. 获取用户详情
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // 3. 构建响应
        return ResponseEntity.ok(Result.success("获取成功", userPrincipal));
    }

    // 提取令牌
    @RequestMapping("/extract")
    public ResponseEntity<Result<String>> extract(HttpServletRequest request) {
        return ResponseEntity.ok(Result.success("提取成功", jwtService.extractToken(request)));
    }

    // 刷新令牌
    @RequestMapping("/refresh")
    public ResponseEntity<Result<String>> refreshToken(HttpServletRequest request) {
        String token = jwtService.extractToken(request);
        String newToken = jwtService.refreshToken(token);
        if (newToken == null) {
            return ResponseEntity.ok(Result.error("ERROR", "无法刷新令牌：令牌无效或未到刷新时间"));
        }
        return ResponseEntity.ok(Result.success("刷新成功", newToken));
    }

    // 获取用户名
    @RequestMapping("/get-username")
    public ResponseEntity<Result<String>> getUsernameFromToken(HttpServletRequest request) {
        String token = jwtService.extractToken(request);
        String username = jwtService.getUsername(token);
        return ResponseEntity.ok(Result.success("获取成功", username));
    }

    // 获取认证
    @RequestMapping("/get-auth")
    public ResponseEntity<Result<Authentication>> getAuthentication(HttpServletRequest request) {
        String token = jwtService.extractToken(request);
        Authentication authentication = jwtService.getAuthentication(token);
        return ResponseEntity.ok(Result.success("获取成功", authentication));
    }

    // 登出用户
    @RequestMapping("/logout")
    public ResponseEntity<Result<Void>> logout(HttpServletRequest request) {
        String token = jwtService.extractToken(request);
        // 1. 验证令牌有效性
        if (!jwtService.validateToken(token)) {
            return ResponseEntity.ok(Result.error("ERROR_INVALID_TOKEN", "无效令牌"));
        }

        // 2. 使令牌失效
        jwtService.invalidateToken(token);

        // 3. 清除安全上下文
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok(Result.success("登出成功", null));
    }
}
