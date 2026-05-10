package org.example.test.repository;

import org.example.test.domain.sys.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SysRoleRepository extends JpaRepository<SysRole, Long> {

    Optional<SysRole> findByCodeAndDeletedFalse(String code);
}
