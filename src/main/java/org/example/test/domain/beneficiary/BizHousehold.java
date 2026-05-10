package org.example.test.domain.beneficiary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.test.domain.base.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "biz_household")
public class BizHousehold extends BaseEntity {

    @Column(name = "household_no", nullable = false, unique = true, length = 64)
    private String householdNo;

    @Column(name = "head_name", nullable = false, length = 64)
    private String headName;

    @Column(name = "org_id")
    private Long orgId;

    @Column(length = 255)
    private String address;

    @Column(name = "poverty_flag")
    private Boolean povertyFlag;

    @Column(nullable = false, length = 32)
    private String status;
}
