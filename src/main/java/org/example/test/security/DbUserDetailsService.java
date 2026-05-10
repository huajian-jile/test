package org.example.test.security;

import org.example.test.domain.sys.SysPermission;
import org.example.test.domain.sys.SysRole;
import org.example.test.domain.sys.SysRolePermission;
import org.example.test.domain.sys.SysUser;
import org.example.test.domain.sys.SysUserRole;
import org.example.test.repository.SysPermissionRepository;
import org.example.test.repository.SysRolePermissionRepository;
import org.example.test.repository.SysRoleRepository;
import org.example.test.repository.SysUserRepository;
import org.example.test.repository.SysUserRoleRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DbUserDetailsService implements UserDetailsService {

    private final SysUserRepository userRepository;
    private final SysUserRoleRepository userRoleRepository;
    private final SysRoleRepository roleRepository;
    private final SysRolePermissionRepository rolePermissionRepository;
    private final SysPermissionRepository permissionRepository;

    public DbUserDetailsService(
            SysUserRepository userRepository,
            SysUserRoleRepository userRoleRepository,
            SysRoleRepository roleRepository,
            SysRolePermissionRepository rolePermissionRepository,
            SysPermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser u = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        Set<String> codes = new HashSet<>();
        List<SysUserRole> urs = userRoleRepository.findByUserIdAndDeletedFalse(u.getId());
        for (SysUserRole ur : urs) {
            roleRepository.findById(ur.getRoleId()).ifPresent(role -> {
                if (!role.isDeleted()) {
                    codes.add("ROLE_" + role.getCode());
                    List<SysRolePermission> rps = rolePermissionRepository.findByRoleIdAndDeletedFalse(role.getId());
                    for (SysRolePermission rp : rps) {
                        permissionRepository.findById(rp.getPermissionId()).ifPresent(p -> {
                            if (!p.isDeleted()) {
                                codes.add(p.getCode());
                            }
                        });
                    }
                }
            });
        }

        List<SimpleGrantedAuthority> authorities = codes.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        boolean enabled = u.getStatus() != null && u.getStatus() == 1;
        var auths = authorities.isEmpty() ? List.<SimpleGrantedAuthority>of(new SimpleGrantedAuthority("ROLE_USER")) : authorities;
        return new AppPrincipal(u.getId(), u.getUsername(), u.getPassword(), enabled, auths);
    }
}
