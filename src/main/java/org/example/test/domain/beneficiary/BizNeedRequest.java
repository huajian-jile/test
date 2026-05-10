package org.example.test.domain.beneficiary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.test.domain.base.BaseEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "biz_need_request")
public class BizNeedRequest extends BaseEntity {

    @Column(name = "household_id", nullable = false)
    private Long householdId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 64)
    private String category;

    @Column(length = 2000)
    private String description;

    @Column(length = 32)
    private String urgency;

    @Column(nullable = false, length = 32)
    private String status;

    @Column(name = "audited_by")
    private Long auditedBy;

    @Column(name = "audited_at")
    private LocalDateTime auditedAt;
}
