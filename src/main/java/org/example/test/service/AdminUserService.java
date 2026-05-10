package org.example.test.service;

import org.example.test.common.exception.ApiException;
import org.example.test.domain.sys.SysUser;
import org.example.test.repository.SysUserRepository;
import org.example.test.security.SecurityUtils;
import org.example.test.web.dto.UserAdminDto;
import org.example.test.web.dto.UserCreateRequest;
import org.example.test.web.dto.UserUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminUserService {

    private final SysUserRepository userRepository;

    public AdminUserService(SysUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserAdminDto> listAll() {
        return userRepository.findAll().stream()
                .filter(u -> !u.isDeleted())
                .map(this::toDto)
                .toList();
    }

    public UserAdminDto get(Long id) {
        return userRepository.findById(id)
                .filter(u -> !u.isDeleted())
                .map(this::toDto)
                .orElseThrow(() -> new ApiException(404, "用户不存在"));
    }

    public String getPassword(Long id) {
        SysUser u = userRepository.findById(id).filter(x -> !x.isDeleted())
                .orElseThrow(() -> new ApiException(404, "用户不存在"));
        return u.getPassword();
    }

    @Transactional
    public Long create(UserCreateRequest req) {
        userRepository.findByUsernameAndDeletedFalse(req.username()).ifPresent(x -> {
            throw new ApiException(400, "用户名已存在");
        });
        SysUser u = new SysUser();
        u.setUsername(req.username());
        u.setPassword(req.password());
        u.setRealName(req.realName());
        u.setPhone(req.phone());
        u.setOrgId(req.orgId());
        u.setStatus(req.status());
        AuditHelper.onCreate(u);
        userRepository.save(u);
        return u.getId();
    }

    @Transactional
    public void update(Long id, UserUpdateRequest req) {
        SysUser u = userRepository.findById(id).filter(x -> !x.isDeleted())
                .orElseThrow(() -> new ApiException(404, "用户不存在"));
        if (req.password() != null && !req.password().isBlank()) {
            u.setPassword(req.password());
        }
        u.setRealName(req.realName());
        u.setPhone(req.phone());
        u.setOrgId(req.orgId());
        u.setStatus(req.status());
        AuditHelper.onUpdate(u);
        userRepository.save(u);
    }

    @Transactional
    public void softDelete(Long id) {
        SysUser u = userRepository.findById(id).orElseThrow(() -> new ApiException(404, "用户不存在"));
        Long cur = SecurityUtils.currentUserIdOrNull();
        if (cur != null && cur.equals(id)) {
            throw new ApiException(400, "不能删除当前登录用户");
        }
        u.setDeleted(true);
        AuditHelper.onUpdate(u);
        userRepository.save(u);
    }

    private UserAdminDto toDto(SysUser u) {
        return new UserAdminDto(
                u.getId(),
                u.getUsername(),
                u.getPassword(),
                u.getRealName(),
                u.getPhone(),
                u.getOrgId(),
                u.getStatus(),
                u.getLoginAt(),
                u.getCreatedAt(),
                u.getUpdatedAt(),
                u.getCreatedBy(),
                u.getUpdatedBy(),
                u.isDeleted());
    }
}
