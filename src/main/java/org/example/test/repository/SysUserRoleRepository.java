package org.example.test.repository;

import org.example.test.domain.sys.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysUserRoleRepository extends JpaRepository<SysUserRole, Long> {

    List<SysUserRole> findByUserIdAndDeletedFalse(Long userId);
}
