package org.example.test.service;

import org.example.test.domain.sys.SysPermission;
import org.example.test.repository.SysPermissionRepository;
import org.example.test.security.AppPrincipal;
import org.example.test.web.dto.MenuNodeResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final SysPermissionRepository permissionRepository;

    public MenuService(SysPermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<MenuNodeResponse> menuTreeForCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Set<String> granted = new HashSet<>();
        if (auth != null) {
            for (GrantedAuthority ga : auth.getAuthorities()) {
                granted.add(ga.getAuthority());
            }
        }
        boolean superAdmin = granted.contains("ROLE_SUPER_ADMIN");

        List<SysPermission> all = permissionRepository.findByDeletedFalseOrderBySortOrderAscIdAsc();
        List<SysPermission> visible = all.stream()
                .filter(p -> p.getPermType() == 1 || p.getPermType() == 2)
                .filter(p -> superAdmin || granted.contains(p.getCode()))
                .collect(Collectors.toList());

        Map<Long, MenuNodeResponse> nodes = new HashMap<>();
        for (SysPermission p : visible) {
            nodes.put(p.getId(), new MenuNodeResponse(
                    p.getId(),
                    p.getParentId(),
                    p.getPermType(),
                    p.getCode(),
                    p.getName(),
                    p.getPath(),
                    p.getSortOrder(),
                    new ArrayList<>()));
        }
        List<MenuNodeResponse> roots = new ArrayList<>();
        for (MenuNodeResponse node : nodes.values()) {
            if (node.parentId() == null || !nodes.containsKey(node.parentId())) {
                roots.add(node);
            } else {
                nodes.get(node.parentId()).children().add(node);
            }
        }
        roots.sort((a, b) -> Integer.compare(nz(a.sort()), nz(b.sort())));
        for (MenuNodeResponse r : roots) {
            sortChildren(r);
        }
        return roots;
    }

    private void sortChildren(MenuNodeResponse n) {
        n.children().sort((a, b) -> Integer.compare(nz(a.sort()), nz(b.sort())));
        for (MenuNodeResponse c : n.children()) {
            sortChildren(c);
        }
    }

    private static int nz(Integer s) {
        return s == null ? 0 : s;
    }

    public AppPrincipal currentPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof AppPrincipal ap) {
            return ap;
        }
        return null;
    }
}
