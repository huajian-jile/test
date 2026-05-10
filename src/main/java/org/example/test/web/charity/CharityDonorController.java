package org.example.test.web.charity;

import org.example.test.common.api.ApiResult;
import org.example.test.domain.charity.BizIndividualDonor;
import org.example.test.repository.BizIndividualDonorRepository;
import org.example.test.service.AuditHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/charity/donors")
public class CharityDonorController {

    private final BizIndividualDonorRepository repository;

    public CharityDonorController(BizIndividualDonorRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ApiResult<List<BizIndividualDonor>> list() {
        return ApiResult.ok(repository.findByDeletedFalseOrderByIdDesc());
    }

    public record CreateReq(String donorType, String displayName, String phone, Long userId) {
    }

    @PostMapping
    public ApiResult<Map<String, Long>> create(@RequestBody CreateReq req) {
        BizIndividualDonor d = new BizIndividualDonor();
        d.setDonorType(req.donorType() == null ? "PERSON" : req.donorType());
        d.setDisplayName(req.displayName());
        d.setPhone(req.phone());
        d.setUserId(req.userId());
        AuditHelper.onCreate(d);
        repository.save(d);
        return ApiResult.ok(Map.of("id", d.getId()));
    }
}
