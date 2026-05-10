package org.example.test.domain.beneficiary;

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
@Table(name = "biz_household_fund_receipt")
public class BizHouseholdFundReceipt extends BaseEntity {

    @Column(name = "household_id", nullable = false)
    private Long householdId;

    @Column(name = "allocation_id")
    private Long allocationId;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt;

    @Column(length = 255)
    private String purpose;

    @Column(length = 500)
    private String remark;
}
