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

    @Column(name = "donor_type", nullable = false, length = 32)
    private String donorType;

    @Column(name = "display_name", nullable = false, length = 128)
    private String displayName;

    @Column(length = 20)
    private String phone;

    @Column(name = "user_id")
    private Long userId;
}
