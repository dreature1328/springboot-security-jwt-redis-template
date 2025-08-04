package xyz.dreature.ssjrt.security.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService,
                         UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 1. 从请求中提取 JWT 令牌
            String jwt = jwtService.extractToken(request);

            if (jwt != null) {
                // 2. 验证令牌有效性
                if (jwtService.validateToken(jwt)) {
                    // 3. 从令牌中提取用户名
                    String username = jwtService.getUsername(jwt);

                    // 4. 检查用户名有效性
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        // 5. 加载用户详情
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                        // 6. 创建认证对象
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null, // 凭证（密码）不需要
                                        userDetails.getAuthorities()
                                );

                        // 7. 添加请求详情
                        authentication.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );

                        // 8. 设置安全上下文
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        } catch (Exception e) {
            // 记录错误但不中断请求
            logger.error("无法设置认证", e);
        }

        // 9. 继续过滤器链
        filterChain.doFilter(request, response);
    }
}
