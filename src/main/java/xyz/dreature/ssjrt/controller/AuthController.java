package xyz.dreature.ssjrt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.dreature.ssjrt.common.dto.LoginRequest;
import xyz.dreature.ssjrt.common.dto.RegisterRequest;
import xyz.dreature.ssjrt.common.entity.User;
import xyz.dreature.ssjrt.common.vo.Result;
import xyz.dreature.ssjrt.mapper.UserMapper;
import xyz.dreature.ssjrt.security.jwt.JwtTokenService;

import javax.servlet.http.HttpServletRequest;

// 接口（安全认证）
@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
            // 1. 创建 Authentication 对象
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

            // 2. 认证
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 3. 将认证信息存入SecurityContext（可选，根据需求）
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 4. 生成Token
            String token = jwtTokenService.generateToken(authentication);

            // 5. 返回Token
            return ResponseEntity.ok(Result.success("登录成功", null));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Result.error("ERROR", "用户名或密码错误"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        // 1. 检查用户名是否已存在
        User existingUser = userMapper.selectByUsername(registerRequest.getUsername());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.error("ERROR", "用户名或密码错误"));
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
}
