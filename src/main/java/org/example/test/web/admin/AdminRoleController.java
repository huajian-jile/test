package org.example.test.web.admin;

import org.example.test.common.api.ApiResult;
import org.example.test.domain.sys.SysRole;
import org.example.test.repository.SysRoleRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/roles")
public class AdminRoleController {

    private final SysRoleRepository roleRepository;

    public AdminRoleController(SysRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public ApiResult<List<SysRole>> list() {
        return ApiResult.ok(roleRepository.findAll().stream().filter(r -> !r.isDeleted()).toList());
    }
}
