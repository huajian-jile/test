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
@Table(name = "sys_org")
public class SysOrg extends BaseEntity {

    @Column(name = "parent_id")
    private Long parentId;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(name = "level_type", nullable = false)
    private Integer levelType;

    @Column(name = "region_code", length = 32)
    private String regionCode;

    @Column(name = "sort_order")
    private Integer sortOrder;
}
