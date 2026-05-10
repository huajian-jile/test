package org.example.test.domain.nation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.test.domain.base.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "biz_appropriation_batch")
public class BizAppropriationBatch extends BaseEntity {

    @Column(name = "batch_no", nullable = false, unique = true, length = 64)
    private String batchNo;

    @Column(name = "fund_source_id", nullable = false)
    private Long fundSourceId;

    @Column(name = "total_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false, length = 8)
    private String currency = "CNY";

    @Column(name = "issued_at")
    private LocalDate issuedAt;

    @Column(length = 255)
    private String title;

    @Column(length = 500)
    private String remark;

    @Column(nullable = false, length = 32)
    private String status;
}
