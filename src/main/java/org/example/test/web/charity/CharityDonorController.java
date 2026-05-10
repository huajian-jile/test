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

    public record CreateReq(
            String donorType,
            String donorName,
            String gender,
            String phone,
            String remark,
            Long userId) {
    }

    @PostMapping
    public ApiResult<Map<String, Long>> create(@RequestBody CreateReq req) {
        BizIndividualDonor d = new BizIndividualDonor();
        d.setDonorType(req.donorType() == null ? "PERSON" : req.donorType());
        String name = req.donorName() == null || req.donorName().isBlank() ? "未登记" : req.donorName();
        d.setDonorName(name);
        d.setGender(req.gender());
        d.setDisplayName(name);
        d.setPhone(req.phone());
        d.setRemark(req.remark());
        d.setUserId(req.userId());
        AuditHelper.onCreate(d);
        repository.save(d);
        return ApiResult.ok(Map.of("id", d.getId()));
    }
}
