package xyz.dreature.ssjrt.common.dto;

// 注册数据传输对象
public class RegisterRequest {
    private String username;
    private String password;

    // 默认构造函数
    public RegisterRequest() {
    }

    // 全参数构造函数
    public RegisterRequest(String username, String password) {
        this.username = username;
        this.password = password;
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
        return "RegisterRequest{" +
                "username='" + username + '\'' +
                ", password='[PROTECTED]'" + // 安全考虑，不打印密码
                '}';
    }
}
