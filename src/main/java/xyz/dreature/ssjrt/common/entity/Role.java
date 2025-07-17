package xyz.dreature.ssjrt.common.entity;

import java.util.Date;

// 角色实体
public class Role {
    // ===== 属性 =====
    private Short id;
    private String code;
    private String name;
    private Boolean system;
    private Date createdAt;

    // ===== 构造方法 =====
    // 无参构造器
    public Role() {
    }

    // 全参构造器
    public Role(Short id, String code, String name, Boolean system, Date createdAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.system = system;
        this.createdAt = createdAt;
    }

    // 复制构造器
    public Role(Role role) {
        this.id = role.getId();
        this.code = role.getCode();
        this.name = role.getName();
        this.system = role.getSystem();
        this.createdAt = role.getCreatedAt();
    }

    // ===== Getter 与 Setter 方法 =====
    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSystem() {
        return system;
    }

    public void setSystem(Boolean system) {
        this.system = system;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    // ===== 其他 =====
    // 字符串表示
    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", system=" + system +
                ", createdAt=" + createdAt +
                '}';
    }
}
