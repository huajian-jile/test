package org.example.test.repository;

import org.example.test.domain.beneficiary.BizNeedRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BizNeedRequestRepository extends JpaRepository<BizNeedRequest, Long> {

    long countByDeletedFalse();

    List<BizNeedRequest> findByDeletedFalseOrderByIdDesc();
}
