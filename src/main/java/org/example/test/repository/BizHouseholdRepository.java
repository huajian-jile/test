package org.example.test.repository;

import org.example.test.domain.beneficiary.BizHousehold;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BizHouseholdRepository extends JpaRepository<BizHousehold, Long> {

    long countByDeletedFalse();

    List<BizHousehold> findByDeletedFalseOrderByIdDesc();
}
