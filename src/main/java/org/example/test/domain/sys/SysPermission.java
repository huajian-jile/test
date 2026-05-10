package org.example.test.domain.sys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.test.domain.base.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "sys_permission")
public class SysPermission extends BaseEntity {

    @Column(name = "parent_id")
    private Long parentId;

    /** 1目录 2菜单 3按钮 4接口 */
    @Column(name = "perm_type", nullable = false)
    private Integer permType;

    @Column(nullable = false, unique = true, length = 128)
    private String code;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(length = 255)
    private String path;

    @Column(name = "api_pattern", length = 255)
    private String apiPattern;

    @Column(name = "sort_order")
    private Integer sortOrder;
}
