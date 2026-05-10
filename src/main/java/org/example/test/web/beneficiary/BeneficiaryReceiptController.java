package org.example.test.web.beneficiary;

import org.example.test.common.api.ApiResult;
import org.example.test.domain.beneficiary.BizHouseholdFundReceipt;
import org.example.test.repository.BizHouseholdFundReceiptRepository;
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
@RequestMapping("/api/beneficiary/receipts")
public class BeneficiaryReceiptController {

    private final BizHouseholdFundReceiptRepository repository;

    public BeneficiaryReceiptController(BizHouseholdFundReceiptRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ApiResult<List<BizHouseholdFundReceipt>> list() {
        return ApiResult.ok(repository.findByDeletedFalseOrderByIdDesc());
    }

    public record CreateReq(
            Long householdId,
            Long allocationId,
            BigDecimal amount,
            LocalDateTime receivedAt,
            String purpose,
            String remark) {
    }

    @PostMapping
    public ApiResult<Map<String, Long>> create(@RequestBody CreateReq req) {
        BizHouseholdFundReceipt r = new BizHouseholdFundReceipt();
        r.setHouseholdId(req.householdId());
        r.setAllocationId(req.allocationId());
        r.setAmount(req.amount());
        r.setReceivedAt(req.receivedAt() == null ? LocalDateTime.now() : req.receivedAt());
        r.setPurpose(req.purpose());
        r.setRemark(req.remark());
        AuditHelper.onCreate(r);
        repository.save(r);
        return ApiResult.ok(Map.of("id", r.getId()));
    }
}
