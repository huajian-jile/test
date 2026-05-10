package org.example.test.repository;

import org.example.test.domain.charity.BizDonationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BizDonationRecordRepository extends JpaRepository<BizDonationRecord, Long> {

    long countByDeletedFalse();

    List<BizDonationRecord> findByDeletedFalseOrderByIdDesc();
}
