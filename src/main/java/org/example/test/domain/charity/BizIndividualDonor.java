package org.example.test.domain.charity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.test.domain.base.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "biz_individual_donor")
public class BizIndividualDonor extends BaseEntity {

    /** 个人 / 企业，保留兼容 */
    @Column(name = "donor_type", nullable = false, length = 32)
    private String donorType;

    @Column(name = "display_name", length = 128)
    private String displayName;

    /** 姓名 */
    @Column(name = "donor_name", nullable = false, length = 64)
    private String donorName;

    /** 性别 */
    @Column(length = 16)
    private String gender;

    @Column(length = 20)
    private String phone;

    /** 备注 */
    @Column(length = 500)
    private String remark;

    @Column(name = "user_id")
    private Long userId;
}
