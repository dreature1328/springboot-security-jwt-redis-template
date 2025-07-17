package xyz.dreature.ssjrt.common.dto;

// 登录数据传输对象
public class LoginRequest {
    // ===== 属性 =====
    private String username;
    private String password;

    // ===== 构造方法 =====
    // 无参构造器
    public LoginRequest() {
    }

    // 全参构造器
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // 复制构造器
    public LoginRequest(LoginRequest loginRequest) {
        this.username = loginRequest.getUsername();
        this.password = loginRequest.getPassword();
    }

    // ===== Getter 与 Setter 方法 =====
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // ===== 其他 =====
    // 字符串表示
    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                ", password='[PROTECTED]'" + // 安全考虑，不打印密码
                '}';
    }
}
