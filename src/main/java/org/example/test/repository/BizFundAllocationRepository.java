package org.example.test.repository;

import org.example.test.domain.nation.BizFundAllocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BizFundAllocationRepository extends JpaRepository<BizFundAllocation, Long> {

    List<BizFundAllocation> findByDeletedFalseOrderByIdDesc();
}
