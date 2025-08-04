package xyz.dreature.ssjrt.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import xyz.dreature.ssjrt.security.jwt.JwtAuthFilter;
import xyz.dreature.ssjrt.security.jwt.JwtService;

// 安全配置
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(JwtService jwtService,
                          UserDetailsService userDetailsService) { // 修改
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService; // 新增
    }

    // 配置 URL 访问权限
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/register", "/login").permitAll() // 允许注册 / 登录 URL
                .anyRequest().permitAll() // 允许所有 URL（测试用）
//                .anyRequest().authenticated() // 禁止所有 URL
                .and()
                .httpBasic().disable() // 禁用基本登录
                .formLogin().disable() // 禁用表单登录
                .addFilterBefore( // 添加一个过滤器来验证 Token
                        new JwtAuthFilter(jwtService, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class
                );
    }

    // 密码编码器（BCrypt 哈希加密）
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
