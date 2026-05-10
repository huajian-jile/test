package org.example.test.repository;

import org.example.test.domain.sys.SysRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysRolePermissionRepository extends JpaRepository<SysRolePermission, Long> {

    List<SysRolePermission> findByRoleIdAndDeletedFalse(Long roleId);
}
