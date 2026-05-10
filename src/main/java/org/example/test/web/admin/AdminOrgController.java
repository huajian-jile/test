package org.example.test.web.admin;

import org.example.test.common.api.ApiResult;
import org.example.test.domain.sys.SysOrg;
import org.example.test.repository.SysOrgRepository;
import org.example.test.service.AuditHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/orgs")
public class AdminOrgController {

    private final SysOrgRepository orgRepository;

    public AdminOrgController(SysOrgRepository orgRepository) {
        this.orgRepository = orgRepository;
    }

    @GetMapping
    public ApiResult<List<SysOrg>> list() {
        return ApiResult.ok(orgRepository.findByDeletedFalseOrderBySortOrderAscIdAsc());
    }

    public record OrgCreateRequest(Long parentId, String name, Integer levelType, String regionCode, Integer sortOrder) {
    }

    @PostMapping
    public ApiResult<Map<String, Long>> create(@RequestBody OrgCreateRequest req) {
        SysOrg o = new SysOrg();
        o.setParentId(req.parentId());
        o.setName(req.name());
        o.setLevelType(req.levelType() == null ? 0 : req.levelType());
        o.setRegionCode(req.regionCode());
        o.setSortOrder(req.sortOrder() == null ? 0 : req.sortOrder());
        AuditHelper.onCreate(o);
        orgRepository.save(o);
        return ApiResult.ok(Map.of("id", o.getId()));
    }
}
