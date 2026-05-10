package org.example.test.bootstrap;

import org.example.test.domain.base.BaseEntity;
import org.example.test.domain.beneficiary.BizHousehold;
import org.example.test.domain.beneficiary.BizHouseholdFundReceipt;
import org.example.test.domain.beneficiary.BizNeedRequest;
import org.example.test.domain.charity.BizDonationRecord;
import org.example.test.domain.charity.BizIndividualDonor;
import org.example.test.domain.nation.BizAppropriationBatch;
import org.example.test.domain.nation.BizFundAllocation;
import org.example.test.domain.nation.BizFundSource;
import org.example.test.domain.sys.SysOrg;
import org.example.test.domain.sys.SysRole;
import org.example.test.domain.sys.SysUser;
import org.example.test.domain.sys.SysUserRole;
import org.example.test.repository.BizAppropriationBatchRepository;
import org.example.test.repository.BizDonationRecordRepository;
import org.example.test.repository.BizFundAllocationRepository;
import org.example.test.repository.BizFundSourceRepository;
import org.example.test.repository.BizHouseholdFundReceiptRepository;
import org.example.test.repository.BizHouseholdRepository;
import org.example.test.repository.BizIndividualDonorRepository;
import org.example.test.repository.BizNeedRequestRepository;
import org.example.test.repository.SysOrgRepository;
import org.example.test.repository.SysRoleRepository;
import org.example.test.repository.SysUserRepository;
import org.example.test.repository.SysUserRoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * 启动时把各业务演示表补足到 {@code app.demo-seed-rows} 条（默认 100，幂等）。
 */
@Component
@Order(2)
public class DemoBulkDataSeeder implements CommandLineRunner {

    private final SysOrgRepository orgRepository;
    private final SysUserRepository userRepository;
    private final SysRoleRepository roleRepository;
    private final SysUserRoleRepository userRoleRepository;
    private final BizFundSourceRepository fundSourceRepository;
    private final BizAppropriationBatchRepository batchRepository;
    private final BizFundAllocationRepository allocationRepository;
    private final BizHouseholdRepository householdRepository;
    private final BizHouseholdFundReceiptRepository receiptRepository;
    private final BizNeedRequestRepository needRepository;
    private final BizIndividualDonorRepository donorRepository;
    private final BizDonationRecordRepository donationRecordRepository;

    @Value("${app.demo-seed-enabled:true}")
    private boolean demoSeedEnabled;

    @Value("${app.demo-seed-rows:100}")
    private int rows;

    public DemoBulkDataSeeder(
            SysOrgRepository orgRepository,
            SysUserRepository userRepository,
            SysRoleRepository roleRepository,
            SysUserRoleRepository userRoleRepository,
            BizFundSourceRepository fundSourceRepository,
            BizAppropriationBatchRepository batchRepository,
            BizFundAllocationRepository allocationRepository,
            BizHouseholdRepository householdRepository,
            BizHouseholdFundReceiptRepository receiptRepository,
            BizNeedRequestRepository needRepository,
            BizIndividualDonorRepository donorRepository,
            BizDonationRecordRepository donationRecordRepository) {
        this.orgRepository = orgRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.fundSourceRepository = fundSourceRepository;
        this.batchRepository = batchRepository;
        this.allocationRepository = allocationRepository;
        this.householdRepository = householdRepository;
        this.receiptRepository = receiptRepository;
        this.needRepository = needRepository;
        this.donorRepository = donorRepository;
        this.donationRecordRepository = donationRecordRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!demoSeedEnabled || rows <= 0) {
            return;
        }
        if (userRepository.countByDeletedFalse() == 0) {
            return;
        }

        long stamp = System.currentTimeMillis();
        Long rootOrgId = resolveRootOrgId();
        Long actorId = resolveActorUserId();
        Long adminRoleId = roleRepository.findByCodeAndDeletedFalse("SUPER_ADMIN").map(SysRole::getId).orElse(null);

        seedOrgs(rootOrgId, actorId, stamp);
        seedFundSources(actorId, stamp);

        List<Long> fundSourceIds = fundSourceRepository.findByDeletedFalseOrderByIdAsc().stream()
                .map(BizFundSource::getId)
                .toList();
        if (fundSourceIds.isEmpty()) {
            return;
        }

        seedBatches(fundSourceIds, actorId, stamp);
        List<Long> batchIds = batchRepository.findByDeletedFalseOrderByIdDesc().stream()
                .map(BizAppropriationBatch::getId)
                .toList();
        seedAllocations(batchIds, rootOrgId, actorId, stamp);

        seedHouseholds(rootOrgId, actorId, stamp);
        List<Long> householdIds = householdRepository.findByDeletedFalseOrderByIdDesc().stream()
                .map(BizHousehold::getId)
                .toList();
        seedReceipts(householdIds, actorId, stamp);
        seedNeeds(householdIds, actorId, stamp);

        seedDonors(actorId, stamp);
        List<Long> donorIds = donorRepository.findByDeletedFalseOrderByIdDesc().stream()
                .map(BizIndividualDonor::getId)
                .toList();
        seedDonations(donorIds, actorId, stamp);

        seedUsers(rootOrgId, adminRoleId, actorId, stamp);
    }

    private static void touch(BaseEntity e, Long actorId, LocalDateTime n) {
        e.setCreatedAt(n);
        e.setUpdatedAt(n);
        e.setCreatedBy(actorId);
        e.setUpdatedBy(actorId);
        e.setDeleted(false);
    }

    private Long resolveRootOrgId() {
        return orgRepository.findByDeletedFalseOrderBySortOrderAscIdAsc().stream()
                .filter(o -> o.getParentId() == null)
                .min(Comparator.comparing(SysOrg::getId))
                .map(SysOrg::getId)
                .orElse(null);
    }

    private Long resolveActorUserId() {
        return userRepository.findByUsernameAndDeletedFalse("admin").map(SysUser::getId).orElse(null);
    }

    private void seedOrgs(Long rootOrgId, Long actorId, long stamp) {
        if (rootOrgId == null) {
            return;
        }
        int need = rows - (int) orgRepository.countByDeletedFalse();
        LocalDateTime n = LocalDateTime.now();
        for (int i = 0; i < need; i++) {
            SysOrg o = new SysOrg();
            o.setParentId(rootOrgId);
            o.setName("演示组织-" + stamp + "-" + String.format("%03d", i));
            o.setLevelType(5);
            o.setRegionCode(String.format("99%05d", (stamp + i) % 100000));
            o.setSortOrder(100 + i);
            touch(o, actorId, n);
            orgRepository.save(o);
        }
    }

    private void seedFundSources(Long actorId, long stamp) {
        int need = rows - (int) fundSourceRepository.countByDeletedFalse();
        LocalDateTime n = LocalDateTime.now();
        for (int i = 0; i < need; i++) {
            BizFundSource e = new BizFundSource();
            e.setCode("DEMO-FS-" + stamp + "-" + String.format("%04d", i));
            e.setName("演示资金来源-" + i);
            e.setRemark("批量演示");
            touch(e, actorId, n);
            fundSourceRepository.save(e);
        }
    }

    private void seedBatches(List<Long> fundSourceIds, Long actorId, long stamp) {
        int need = rows - (int) batchRepository.countByDeletedFalse();
        LocalDateTime n = LocalDateTime.now();
        for (int i = 0; i < need; i++) {
            Long fsId = fundSourceIds.get(i % fundSourceIds.size());
            BizAppropriationBatch b = new BizAppropriationBatch();
            b.setBatchNo("BATCH-" + stamp + "-" + String.format("%04d", i));
            b.setFundSourceId(fsId);
            b.setTotalAmount(BigDecimal.valueOf(100_000L + i * 1000L).setScale(2, RoundingMode.HALF_UP));
            b.setCurrency("CNY");
            b.setIssuedAt(LocalDate.now().minusDays(i % 30));
            b.setTitle("演示批次-" + i);
            b.setRemark("批量演示");
            b.setStatus(i % 3 == 0 ? "DRAFT" : (i % 3 == 1 ? "ISSUED" : "CLOSED"));
            touch(b, actorId, n);
            batchRepository.save(b);
        }
    }

    private void seedAllocations(List<Long> batchIds, Long rootOrgId, Long actorId, long stamp) {
        if (batchIds.isEmpty() || rootOrgId == null) {
            return;
        }
        int need = rows - (int) allocationRepository.countByDeletedFalse();
        LocalDateTime n = LocalDateTime.now();
        for (int i = 0; i < need; i++) {
            Long bid = batchIds.get(i % batchIds.size());
            BizFundAllocation a = new BizFundAllocation();
            a.setBatchId(bid);
            a.setFromOrgId(null);
            a.setToOrgId(rootOrgId);
            a.setAmount(BigDecimal.valueOf(10_000L + i * 100L).setScale(2, RoundingMode.HALF_UP));
            a.setTransferAt(n.minusHours(i));
            a.setBizNo("ALLOC-" + stamp + "-" + String.format("%05d", i));
            a.setStatus("NORMAL");
            a.setRemark("批量演示划拨");
            touch(a, actorId, n);
            allocationRepository.save(a);
        }
    }

    private void seedHouseholds(Long rootOrgId, Long actorId, long stamp) {
        int need = rows - (int) householdRepository.countByDeletedFalse();
        LocalDateTime n = LocalDateTime.now();
        for (int i = 0; i < need; i++) {
            BizHousehold h = new BizHousehold();
            h.setHouseholdNo("HH-" + stamp + "-" + String.format("%05d", i));
            h.setHeadName("户主-" + i);
            h.setOrgId(rootOrgId);
            h.setAddress("演示地址第" + i + "号");
            h.setPovertyFlag(i % 2 == 0);
            h.setStatus("NORMAL");
            touch(h, actorId, n);
            householdRepository.save(h);
        }
    }

    private void seedReceipts(List<Long> householdIds, Long actorId, long stamp) {
        if (householdIds.isEmpty()) {
            return;
        }
        int need = rows - (int) receiptRepository.countByDeletedFalse();
        LocalDateTime n = LocalDateTime.now();
        for (int i = 0; i < need; i++) {
            Long hid = householdIds.get(i % householdIds.size());
            BizHouseholdFundReceipt r = new BizHouseholdFundReceipt();
            r.setHouseholdId(hid);
            r.setAllocationId(null);
            r.setAmount(BigDecimal.valueOf(3000L + i * 50L).setScale(2, RoundingMode.HALF_UP));
            r.setReceivedAt(n.minusDays(i % 60));
            r.setPurpose("演示到户用途-" + i);
            r.setRemark("批量演示");
            touch(r, actorId, n);
            receiptRepository.save(r);
        }
    }

    private void seedNeeds(List<Long> householdIds, Long actorId, long stamp) {
        if (householdIds.isEmpty()) {
            return;
        }
        String[] cats = {"医疗", "教育", "住房", "就业"};
        String[] sts = {"DRAFT", "SUBMITTED", "APPROVED", "CLOSED"};
        int need = rows - (int) needRepository.countByDeletedFalse();
        LocalDateTime n = LocalDateTime.now();
        for (int i = 0; i < need; i++) {
            Long hid = householdIds.get(i % householdIds.size());
            BizNeedRequest r = new BizNeedRequest();
            r.setHouseholdId(hid);
            r.setTitle("演示需求-" + stamp + "-" + i);
            r.setCategory(cats[i % cats.length]);
            r.setDescription("批量演示需求说明，序号 " + i);
            r.setUrgency(i % 2 == 0 ? "高" : "一般");
            r.setStatus(sts[i % sts.length]);
            touch(r, actorId, n);
            needRepository.save(r);
        }
    }

    private void seedDonors(Long actorId, long stamp) {
        int need = rows - (int) donorRepository.countByDeletedFalse();
        LocalDateTime n = LocalDateTime.now();
        for (int i = 0; i < need; i++) {
            BizIndividualDonor d = new BizIndividualDonor();
            d.setDonorType(i % 5 == 0 ? "企业" : "PERSON");
            d.setDisplayName("演示捐赠人-" + stamp + "-" + i);
            d.setPhone(String.format("138%08d", Math.floorMod(i, 100_000_000)));
            d.setUserId(null);
            touch(d, actorId, n);
            donorRepository.save(d);
        }
    }

    private void seedDonations(List<Long> donorIds, Long actorId, long stamp) {
        if (donorIds.isEmpty()) {
            return;
        }
        int need = rows - (int) donationRecordRepository.countByDeletedFalse();
        LocalDateTime n = LocalDateTime.now();
        for (int i = 0; i < need; i++) {
            Long did = donorIds.get(i % donorIds.size());
            BizDonationRecord r = new BizDonationRecord();
            r.setDonorId(did);
            r.setAmount(BigDecimal.valueOf(100L + i * 10L).setScale(2, RoundingMode.HALF_UP));
            r.setDonatedAt(n.minusDays(i % 90));
            r.setChannel(i % 2 == 0 ? "线上" : "线下");
            r.setTargetType("REGION");
            r.setTargetId(null);
            r.setCertificateNo("CERT-" + stamp + "-" + String.format("%05d", i));
            r.setPublicVisible(i % 3 != 0);
            r.setRemark("批量演示捐赠");
            touch(r, actorId, n);
            donationRecordRepository.save(r);
        }
    }

    private void seedUsers(Long rootOrgId, Long adminRoleId, Long actorId, long stamp) {
        int need = rows - (int) userRepository.countByDeletedFalse();
        LocalDateTime n = LocalDateTime.now();
        for (int i = 0; i < need; i++) {
            String username = "demo_u_" + stamp + "_" + String.format("%04d", i);
            if (userRepository.findByUsernameAndDeletedFalse(username).isPresent()) {
                continue;
            }
            SysUser u = new SysUser();
            u.setUsername(username);
            u.setPassword("demo123");
            u.setRealName("演示用户-" + i);
            u.setPhone(String.format("139%08d", Math.floorMod(i + 1, 100_000_000)));
            u.setOrgId(rootOrgId);
            u.setStatus(1);
            touch(u, actorId, n);
            userRepository.save(u);
            if (adminRoleId != null) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(u.getId());
                ur.setRoleId(adminRoleId);
                touch(ur, actorId, n);
                userRoleRepository.save(ur);
            }
        }
    }
}
