package org.example.test.repository;

import org.example.test.domain.nation.BizAppropriationBatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BizAppropriationBatchRepository extends JpaRepository<BizAppropriationBatch, Long> {

    long countByDeletedFalse();

    List<BizAppropriationBatch> findByDeletedFalseOrderByIdDesc();
}
