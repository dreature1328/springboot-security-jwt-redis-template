package xyz.dreature.ssjrt.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 1. 从请求中提取 JWT 令牌
            String jwt = jwtService.extractToken(request);
            // 2. 验证令牌有效性
            if (jwt != null && jwtService.validateToken(jwt)) {
                Authentication authentication = jwtService.getAuthentication(jwt);

                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            // 记录错误但不中断请求
            log.error("无法设置认证", e);
        }

        // 继续过滤器链
        filterChain.doFilter(request, response);
    }
}
