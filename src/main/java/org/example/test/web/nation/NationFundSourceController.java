package org.example.test.web.nation;

import org.example.test.common.api.ApiResult;
import org.example.test.common.exception.ApiException;
import org.example.test.domain.nation.BizFundSource;
import org.example.test.repository.BizFundSourceRepository;
import org.example.test.service.AuditHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nation/fund-sources")
public class NationFundSourceController {

    private final BizFundSourceRepository repository;

    public NationFundSourceController(BizFundSourceRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ApiResult<List<BizFundSource>> list() {
        return ApiResult.ok(repository.findByDeletedFalseOrderByIdAsc());
    }

    public record Upsert(String code, String name, String remark) {
    }

    @PostMapping
    public ApiResult<Map<String, Long>> create(@RequestBody Upsert req) {
        repository.findAll().stream()
                .filter(x -> !x.isDeleted() && req.code().equals(x.getCode()))
                .findAny()
                .ifPresent(x -> {
                    throw new ApiException(400, "编码已存在");
                });
        BizFundSource e = new BizFundSource();
        e.setCode(req.code());
        e.setName(req.name());
        e.setRemark(req.remark());
        AuditHelper.onCreate(e);
        repository.save(e);
        return ApiResult.ok(Map.of("id", e.getId()));
    }
}
