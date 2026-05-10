package org.example.test.web.charity;

import org.example.test.common.api.ApiResult;
import org.example.test.domain.charity.BizDonationRecord;
import org.example.test.repository.BizDonationRecordRepository;
import org.example.test.service.AuditHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/charity/donations")
public class CharityDonationController {

    private final BizDonationRecordRepository repository;

    public CharityDonationController(BizDonationRecordRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ApiResult<List<BizDonationRecord>> list() {
        return ApiResult.ok(repository.findByDeletedFalseOrderByIdDesc());
    }

    public record CreateReq(
            Long donorId,
            LocalDateTime donatedAt,
            String donationLocation,
            String recipientName,
            String recipientAddress,
            String recipientFamilySituation,
            BigDecimal amount,
            String groupPhotoUrl,
            String remark) {
    }

    @PostMapping
    public ApiResult<Map<String, Long>> create(@RequestBody CreateReq req) {
        BizDonationRecord r = new BizDonationRecord();
        r.setDonorId(req.donorId());
        r.setAmount(req.amount());
        r.setDonatedAt(req.donatedAt() == null ? LocalDateTime.now() : req.donatedAt());
        r.setDonationLocation(req.donationLocation());
        r.setRecipientName(req.recipientName());
        r.setRecipientAddress(req.recipientAddress());
        r.setRecipientFamilySituation(req.recipientFamilySituation());
        r.setGroupPhotoUrl(req.groupPhotoUrl());
        r.setRemark(req.remark());
        AuditHelper.onCreate(r);
        repository.save(r);
        return ApiResult.ok(Map.of("id", r.getId()));
    }
}
