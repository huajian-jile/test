package org.example.test.repository;

import org.example.test.domain.nation.BizFundSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BizFundSourceRepository extends JpaRepository<BizFundSource, Long> {

    long countByDeletedFalse();

    List<BizFundSource> findByDeletedFalseOrderByIdAsc();
}
