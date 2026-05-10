package org.example.test.domain.charity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.test.domain.base.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "biz_donation_record")
public class BizDonationRecord extends BaseEntity {

    @Column(name = "donor_id", nullable = false)
    private Long donorId;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "donated_at", nullable = false)
    private LocalDateTime donatedAt;

    @Column(length = 32)
    private String channel;

    @Column(name = "target_type", length = 32)
    private String targetType;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "certificate_no", length = 64)
    private String certificateNo;

    @Column(name = "public_visible")
    private Boolean publicVisible;

    @Column(length = 500)
    private String remark;
}
