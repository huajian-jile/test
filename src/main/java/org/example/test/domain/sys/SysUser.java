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
@Table(name = "sys_user")
public class SysUser extends BaseEntity {

    @Column(nullable = false, unique = true, length = 64)
    private String username;

    /** 明文密码（按需求存储，生产环境请评估风险） */
    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "real_name", length = 64)
    private String realName;

    @Column(length = 20)
    private String phone;

    @Column(name = "org_id")
    private Long orgId;

    @Column(nullable = false)
    private Integer status;
}
