package org.example.test.web.beneficiary;

import org.example.test.common.api.ApiResult;
import org.example.test.domain.beneficiary.BizHousehold;
import org.example.test.repository.BizHouseholdRepository;
import org.example.test.service.AuditHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/beneficiary/households")
public class BeneficiaryHouseholdController {

    private final BizHouseholdRepository repository;

    public BeneficiaryHouseholdController(BizHouseholdRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ApiResult<List<BizHousehold>> list() {
        return ApiResult.ok(repository.findByDeletedFalseOrderByIdDesc());
    }

    public record CreateReq(String householdNo, String headName, Long orgId, String address, Boolean povertyFlag, String status) {
    }

    @PostMapping
    public ApiResult<Map<String, Long>> create(@RequestBody CreateReq req) {
        BizHousehold h = new BizHousehold();
        h.setHouseholdNo(req.householdNo());
        h.setHeadName(req.headName());
        h.setOrgId(req.orgId());
        h.setAddress(req.address());
        h.setPovertyFlag(req.povertyFlag() != null && req.povertyFlag());
        h.setStatus(req.status() == null ? "NORMAL" : req.status());
        AuditHelper.onCreate(h);
        repository.save(h);
        return ApiResult.ok(Map.of("id", h.getId()));
    }
}
