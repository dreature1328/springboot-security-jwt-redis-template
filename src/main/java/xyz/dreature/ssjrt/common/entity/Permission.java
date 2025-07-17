package xyz.dreature.ssjrt.common.entity;

import java.util.Date;

// 权限实体
public class Permission {
    // ===== 属性 =====
    private Short id; // SMALLINT UNSIGNED
    private String code;
    private String name;
    private String category;
    private Date createdAt;

    // ===== 构造方法 =====
    // 无参构造器
    public Permission() {
    }

    // 全参构造器
    public Permission(Short id, String code, String name, String category, Date createdAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.category = category;
        this.createdAt = createdAt;
    }

    // 复制构造器
    public Permission(Permission permission) {
        this.id = permission.getId();
        this.code = permission.getCode();
        this.name = permission.getName();
        this.category = permission.getCategory();
        this.createdAt = permission.getCreatedAt();
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
        return "Permission{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
