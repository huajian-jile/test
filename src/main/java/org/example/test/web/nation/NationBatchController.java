package org.example.test.web.nation;

import org.example.test.common.api.ApiResult;
import org.example.test.domain.nation.BizAppropriationBatch;
import org.example.test.repository.BizAppropriationBatchRepository;
import org.example.test.service.AuditHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nation/batches")
public class NationBatchController {

    private final BizAppropriationBatchRepository repository;

    public NationBatchController(BizAppropriationBatchRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ApiResult<List<BizAppropriationBatch>> list() {
        return ApiResult.ok(repository.findByDeletedFalseOrderByIdDesc());
    }

    public record CreateReq(
            String batchNo,
            Long fundSourceId,
            BigDecimal totalAmount,
            LocalDate issuedAt,
            String title,
            String remark,
            String status) {
    }

    @PostMapping
    public ApiResult<Map<String, Long>> create(@RequestBody CreateReq req) {
        BizAppropriationBatch b = new BizAppropriationBatch();
        b.setBatchNo(req.batchNo());
        b.setFundSourceId(req.fundSourceId());
        b.setTotalAmount(req.totalAmount());
        b.setIssuedAt(req.issuedAt());
        b.setTitle(req.title());
        b.setRemark(req.remark());
        b.setStatus(req.status() == null ? "DRAFT" : req.status());
        AuditHelper.onCreate(b);
        repository.save(b);
        return ApiResult.ok(Map.of("id", b.getId()));
    }
}
