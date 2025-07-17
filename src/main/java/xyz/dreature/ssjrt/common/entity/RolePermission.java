package xyz.dreature.ssjrt.common.entity;

import java.util.Date;

// 角色-权限实体
public class RolePermission {
    // ===== 属性 =====
    private Short roleId;
    private Short permissionId;
    private Date grantedAt;

    // ===== 构造方法 =====
    // 无参构造器
    public RolePermission() {
    }

    // 全参构造器
    public RolePermission(Short roleId, Short permissionId, Date grantedAt) {
        this.roleId = roleId;
        this.permissionId = permissionId;
        this.grantedAt = grantedAt;
    }

    // 复制构造器
    public RolePermission(RolePermission rolePermission) {
        this.roleId = rolePermission.getRoleId();
        this.permissionId = rolePermission.getPermissionId();
        this.grantedAt = rolePermission.getGrantedAt();
    }

    // ===== Getter 与 Setter 方法 =====
    public Short getRoleId() {
        return roleId;
    }

    public void setRoleId(Short roleId) {
        this.roleId = roleId;
    }

    public Short getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Short permissionId) {
        this.permissionId = permissionId;
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
        return "RolePermission{" +
                "roleId=" + roleId +
                ", permissionId=" + permissionId +
                ", grantedAt=" + grantedAt +
                '}';
    }
}
