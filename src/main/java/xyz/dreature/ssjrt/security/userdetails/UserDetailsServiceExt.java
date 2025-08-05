package xyz.dreature.ssjrt.security.userdetails;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserDetailsServiceExt extends UserDetailsService {
    // 相比 Spring Security 基础 UserDetailsService 接口仅有用户名加载方法，扩展 ID 加载方法
    UserDetails loadUserById(Long userId);
}
