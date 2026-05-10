package org.example.test.repository;

import org.example.test.domain.sys.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysPermissionRepository extends JpaRepository<SysPermission, Long> {

    List<SysPermission> findByDeletedFalseOrderBySortOrderAscIdAsc();
}
