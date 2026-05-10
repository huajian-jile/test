package org.example.test.repository;

import org.example.test.domain.beneficiary.BizHouseholdFundReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BizHouseholdFundReceiptRepository extends JpaRepository<BizHouseholdFundReceipt, Long> {

    List<BizHouseholdFundReceipt> findByDeletedFalseOrderByIdDesc();
}
