package org.example.test.repository;

import org.example.test.domain.charity.BizIndividualDonor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BizIndividualDonorRepository extends JpaRepository<BizIndividualDonor, Long> {

    long countByDeletedFalse();

    List<BizIndividualDonor> findByDeletedFalseOrderByIdDesc();
}
