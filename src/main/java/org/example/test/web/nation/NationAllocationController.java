package org.example.test.web.nation;

import org.example.test.common.api.ApiResult;
import org.example.test.domain.nation.BizFundAllocation;
import org.example.test.repository.BizFundAllocationRepository;
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
@RequestMapping("/api/nation/allocations")
public class NationAllocationController {

    private final BizFundAllocationRepository repository;

    public NationAllocationController(BizFundAllocationRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ApiResult<List<BizFundAllocation>> list() {
        return ApiResult.ok(repository.findByDeletedFalseOrderByIdDesc());
    }

    public record CreateReq(
            Long batchId,
            Long fromOrgId,
            Long toOrgId,
            BigDecimal amount,
            LocalDateTime transferAt,
            String bizNo,
            String status,
            String remark) {
    }

    @PostMapping
    public ApiResult<Map<String, Long>> create(@RequestBody CreateReq req) {
        BizFundAllocation a = new BizFundAllocation();
        a.setBatchId(req.batchId());
        a.setFromOrgId(req.fromOrgId());
        a.setToOrgId(req.toOrgId());
        a.setAmount(req.amount());
        a.setTransferAt(req.transferAt() == null ? LocalDateTime.now() : req.transferAt());
        a.setBizNo(req.bizNo());
        a.setStatus(req.status() == null ? "NORMAL" : req.status());
        a.setRemark(req.remark());
        AuditHelper.onCreate(a);
        repository.save(a);
        return ApiResult.ok(Map.of("id", a.getId()));
    }
}
