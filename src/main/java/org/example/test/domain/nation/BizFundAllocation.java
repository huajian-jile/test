package org.example.test.domain.nation;

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
@Table(name = "biz_fund_allocation")
public class BizFundAllocation extends BaseEntity {

    @Column(name = "batch_id", nullable = false)
    private Long batchId;

    @Column(name = "from_org_id")
    private Long fromOrgId;

    @Column(name = "to_org_id", nullable = false)
    private Long toOrgId;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "transfer_at", nullable = false)
    private LocalDateTime transferAt;

    @Column(name = "biz_no", nullable = false, unique = true, length = 64)
    private String bizNo;

    @Column(nullable = false, length = 32)
    private String status;

    @Column(name = "replaced_by_id")
    private Long replacedById;

    @Column(length = 500)
    private String remark;
}
