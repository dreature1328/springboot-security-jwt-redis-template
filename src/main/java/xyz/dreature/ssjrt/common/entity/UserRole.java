package xyz.dreature.ssjrt.common.entity;

import java.util.Date;

// 用户-角色实体
public class UserRole {
    // ===== 属性 =====
    private Long userId;
    private Short roleId;
    private Date grantedAt;

    // ===== 构造方法 =====
    // 无参构造器
    public UserRole() {
    }

    // 全参构造器
    public UserRole(Long userId, Short roleId, Date grantedAt) {
        this.userId = userId;
        this.roleId = roleId;
        this.grantedAt = grantedAt;
    }

    // 复制构造器
    public UserRole(UserRole userRole) {
        this.userId = userRole.getUserId();
        this.roleId = userRole.getRoleId();
        this.grantedAt = userRole.getGrantedAt();
    }

    // ===== Getter 与 Setter 方法 =====
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Short getRoleId() {
        return roleId;
    }

    public void setRoleId(Short roleId) {
        this.roleId = roleId;
    }

    public Date getGrantedAt() {
        return grantedAt;
    }

    public void setGrantedAt(Date grantedAt) {
        this.grantedAt = grantedAt;
    }

    // ===== 其他 =====
    // 字符串表示
    @Override
    public String toString() {
        return "UserRole{" +
                "userId=" + userId +
                ", roleId=" + roleId +
                ", grantedAt=" + grantedAt +
                '}';
    }
}

