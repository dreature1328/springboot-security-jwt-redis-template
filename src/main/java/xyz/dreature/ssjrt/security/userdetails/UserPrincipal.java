package xyz.dreature.ssjrt.security.userdetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import xyz.dreature.ssjrt.common.entity.User;

import java.util.Collection;

// 用户信息
public class UserPrincipal implements UserDetails {
    // ===== 属性 =====
    private Long id;
    private String username;
    private String password;
    private String status;
    private Collection<? extends GrantedAuthority> authorities;

    // ===== 构造方法 =====
    // 无参构造器
    public UserPrincipal() {
    }

    // 双参构造器
    public UserPrincipal(User user, Collection<? extends GrantedAuthority> authorities) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPasswordHash();
        this.status = user.getStatus();
        this.authorities = authorities;
    }

    // ===== Getter 与 Setter 方法 =====
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !"LOCKED".equals(status);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return "ACTIVE".equals(status);
    }

    public Long getId() {
        return id;
    }

    // ===== 其他 =====
    // 字符串表示
    @Override
    public String toString() {
        return "SecurityUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", status='" + status + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}
