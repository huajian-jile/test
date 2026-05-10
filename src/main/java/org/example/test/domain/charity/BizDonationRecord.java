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

    /** 捐赠日期时间 */
    @Column(name = "donated_at", nullable = false)
    private LocalDateTime donatedAt;

    /** 捐赠地点 */
    @Column(name = "donation_location", length = 255)
    private String donationLocation;

    /** 对方名称 */
    @Column(name = "recipient_name", length = 128)
    private String recipientName;

    /** 对方地址 */
    @Column(name = "recipient_address", length = 500)
    private String recipientAddress;

    /** 对方家庭状况 */
    @Column(name = "recipient_family_situation", length = 2000)
    private String recipientFamilySituation;

    /** 合照照片（URL 或存储路径） */
    @Column(name = "group_photo_url", length = 512)
    private String groupPhotoUrl;

    /** 备注 */
    @Column(length = 500)
    private String remark;

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
}
