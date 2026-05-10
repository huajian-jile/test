package org.example.test.service;

import org.example.test.common.exception.ApiException;
import org.example.test.domain.sys.SysPermission;
import org.example.test.domain.sys.SysRolePermission;
import org.example.test.domain.sys.SysUser;
import org.example.test.domain.sys.SysUserRole;
import org.example.test.repository.SysPermissionRepository;
import org.example.test.repository.SysRolePermissionRepository;
import org.example.test.repository.SysRoleRepository;
import org.example.test.repository.SysUserRepository;
import org.example.test.repository.SysUserRoleRepository;
import org.example.test.security.JwtService;
import org.example.test.web.dto.LoginRequest;
import org.example.test.web.dto.LoginResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final SysUserRepository userRepository;
    private final SysUserRoleRepository userRoleRepository;
    private final SysRoleRepository roleRepository;
    private final SysRolePermissionRepository rolePermissionRepository;
    private final SysPermissionRepository permissionRepository;
    private final JwtService jwtService;

    public AuthService(
            AuthenticationManager authenticationManager,
            SysUserRepository userRepository,
            SysUserRoleRepository userRoleRepository,
            SysRoleRepository roleRepository,
            SysRolePermissionRepository rolePermissionRepository,
            SysPermissionRepository permissionRepository,
            JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.permissionRepository = permissionRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public LoginResponse login(LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        SysUser user = userRepository.findByUsernameAndDeletedFalse(req.username())
                .orElseThrow(() -> new ApiException(401, "用户不存在"));
        user.setLoginAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        Set<String> roles = new LinkedHashSet<>();
        Set<String> perms = new LinkedHashSet<>();
        List<SysUserRole> urs = userRoleRepository.findByUserIdAndDeletedFalse(user.getId());
        for (SysUserRole ur : urs) {
            roleRepository.findById(ur.getRoleId()).ifPresent(r -> {
                if (!r.isDeleted()) {
                    roles.add(r.getCode());
                    List<SysRolePermission> rps = rolePermissionRepository.findByRoleIdAndDeletedFalse(r.getId());
                    for (SysRolePermission rp : rps) {
                        permissionRepository.findById(rp.getPermissionId()).ifPresent(p -> {
                            if (!p.isDeleted()) {
                                perms.add(p.getCode());
                            }
                        });
                    }
                }
            });
        }

        String token = jwtService.generateToken(user.getUsername());
        return new LoginResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getRealName(),
                new ArrayList<>(roles),
                new ArrayList<>(perms));
    }

    public List<SysPermission> allPermissionsForMenus() {
        return permissionRepository.findByDeletedFalseOrderBySortOrderAscIdAsc();
    }
}
