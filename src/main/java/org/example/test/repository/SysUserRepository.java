package org.example.test.repository;

import org.example.test.domain.sys.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SysUserRepository extends JpaRepository<SysUser, Long> {

    Optional<SysUser> findByUsernameAndDeletedFalse(String username);
}
