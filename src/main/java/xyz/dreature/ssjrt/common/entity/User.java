package xyz.dreature.ssjrt.common.entity;

import java.util.Date;
import java.util.UUID;

// 用户实体
public class User {
    // ===== 属性 =====
    private Long id;
    private String uuid;
    private String username;
    private String passwordHash;
    private String email;
    private String phone;
    private String nickname;
    private String avatarUrl;
    private String gender; // M / F / O / U
    private Date birthDate;
    private String status; // ACTIVE / LOCKED / DISABLED / PENDING
    private Date lastLoginAt;
    private Date createdAt;
    private Date updatedAt;

    // ===== 构造方法 =====
    // 无参构造器
    public User() {
    }

    // 全参构造器
    public User(Long id, String uuid, String username, String passwordHash, String email, String phone, String nickname, String avatarUrl, String gender, Date birthDate, String status, Date lastLoginAt, Date createdAt, Date updatedAt) {
        this.id = id;
        this.uuid = uuid;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.phone = phone;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.gender = gender;
        this.birthDate = birthDate;
        this.status = status;
        this.lastLoginAt = lastLoginAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 复制构造器
    public User(User user) {
        this.id = user.getId();
        this.uuid = user.getUuid();
        this.username = user.getUsername();
        this.passwordHash = user.getPasswordHash();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.nickname = user.getNickname();
        this.avatarUrl = user.getAvatarUrl();
        this.gender = user.getGender();
        this.birthDate = user.getBirthDate();
        this.status = user.getStatus();
        this.lastLoginAt = user.getLastLoginAt();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

    // 双参构造器
    public User(String username, String passwordHash) {
        this.uuid = UUID.randomUUID().toString();
        this.username = username;
        this.passwordHash = passwordHash;
        this.gender = "U";
        this.status = "ACTIVE"; // 默认状态
        Date date = new Date();
        this.createdAt = date; // 创建时间
        this.updatedAt = date; // 更新时间
    }

    // ===== Getter 与 Setter 方法 =====
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Date lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    // ===== 其他 =====
    // 字符串表示
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", gender='" + gender + '\'' +
                ", birthDate=" + birthDate +
                ", status='" + status + '\'' +
                ", lastLoginAt=" + lastLoginAt +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

