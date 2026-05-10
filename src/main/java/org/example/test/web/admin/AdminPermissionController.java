package org.example.test.web.admin;

import org.example.test.common.api.ApiResult;
import org.example.test.domain.sys.SysPermission;
import org.example.test.repository.SysPermissionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/permissions")
public class AdminPermissionController {

    private final SysPermissionRepository permissionRepository;

    public AdminPermissionController(SysPermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @GetMapping
    public ApiResult<List<SysPermission>> list() {
        return ApiResult.ok(permissionRepository.findByDeletedFalseOrderBySortOrderAscIdAsc());
    }
}
