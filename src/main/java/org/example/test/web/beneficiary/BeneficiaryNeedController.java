package org.example.test.web.beneficiary;

import org.example.test.common.api.ApiResult;
import org.example.test.domain.beneficiary.BizNeedRequest;
import org.example.test.repository.BizNeedRequestRepository;
import org.example.test.service.AuditHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/beneficiary/needs")
public class BeneficiaryNeedController {

    private final BizNeedRequestRepository repository;

    public BeneficiaryNeedController(BizNeedRequestRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ApiResult<List<BizNeedRequest>> list() {
        return ApiResult.ok(repository.findByDeletedFalseOrderByIdDesc());
    }

    public record CreateReq(
            Long householdId,
            String title,
            String category,
            String description,
            String urgency,
            String status) {
    }

    @PostMapping
    public ApiResult<Map<String, Long>> create(@RequestBody CreateReq req) {
        BizNeedRequest n = new BizNeedRequest();
        n.setHouseholdId(req.householdId());
        n.setTitle(req.title());
        n.setCategory(req.category());
        n.setDescription(req.description());
        n.setUrgency(req.urgency());
        n.setStatus(req.status() == null ? "DRAFT" : req.status());
        AuditHelper.onCreate(n);
        repository.save(n);
        return ApiResult.ok(Map.of("id", n.getId()));
    }
}
