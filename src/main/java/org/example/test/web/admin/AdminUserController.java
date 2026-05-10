package org.example.test.web.admin;

import jakarta.validation.Valid;
import org.example.test.common.api.ApiResult;
import org.example.test.service.AdminUserService;
import org.example.test.web.dto.UserAdminDto;
import org.example.test.web.dto.UserCreateRequest;
import org.example.test.web.dto.UserUpdateRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public ApiResult<List<UserAdminDto>> list() {
        return ApiResult.ok(adminUserService.listAll());
    }

    @GetMapping("/{id}")
    public ApiResult<UserAdminDto> get(@PathVariable Long id) {
        return ApiResult.ok(adminUserService.get(id));
    }

    @GetMapping("/{id}/password")
    @PreAuthorize("hasAuthority('admin:user:view-password')")
    public ApiResult<Map<String, String>> password(@PathVariable Long id) {
        return ApiResult.ok(Map.of("password", adminUserService.getPassword(id)));
    }

    @PostMapping
    public ApiResult<Map<String, Long>> create(@Valid @RequestBody UserCreateRequest req) {
        return ApiResult.ok(Map.of("id", adminUserService.create(req)));
    }

    @PutMapping("/{id}")
    public ApiResult<Void> update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest req) {
        adminUserService.update(id, req);
        return ApiResult.ok();
    }

    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        adminUserService.softDelete(id);
        return ApiResult.ok();
    }
}
