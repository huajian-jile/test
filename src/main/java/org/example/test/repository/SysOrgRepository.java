package org.example.test.repository;

import org.example.test.domain.sys.SysOrg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysOrgRepository extends JpaRepository<SysOrg, Long> {

    List<SysOrg> findByDeletedFalseOrderBySortOrderAscIdAsc();
}
