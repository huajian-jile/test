package org.example.test.bootstrap;

import org.example.test.domain.charity.BizDonationRecord;
import org.example.test.domain.charity.BizIndividualDonor;
import org.example.test.domain.nation.BizAppropriationBatch;
import org.example.test.domain.nation.BizFundAllocation;
import org.example.test.domain.nation.BizFundSource;
import org.example.test.domain.sys.SysOrg;
import org.example.test.domain.sys.SysPermission;
import org.example.test.domain.sys.SysRole;
import org.example.test.domain.sys.SysRolePermission;
import org.example.test.domain.sys.SysUser;
import org.example.test.domain.sys.SysUserRole;
import org.example.test.repository.BizAppropriationBatchRepository;
import org.example.test.repository.BizDonationRecordRepository;
import org.example.test.repository.BizFundAllocationRepository;
import org.example.test.repository.BizFundSourceRepository;
import org.example.test.repository.BizIndividualDonorRepository;
import org.example.test.repository.SysOrgRepository;
import org.example.test.repository.SysPermissionRepository;
import org.example.test.repository.SysRolePermissionRepository;
import org.example.test.repository.SysRoleRepository;
import org.example.test.repository.SysUserRepository;
import org.example.test.repository.SysUserRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {

    private final SysUserRepository userRepository;
    private final SysRoleRepository roleRepository;
    private final SysPermissionRepository permissionRepository;
    private final SysUserRoleRepository userRoleRepository;
    private final SysRolePermissionRepository rolePermissionRepository;
    private final SysOrgRepository orgRepository;
    private final BizFundSourceRepository fundSourceRepository;
    private final BizAppropriationBatchRepository batchRepository;
    private final BizFundAllocationRepository allocationRepository;
    private final BizIndividualDonorRepository donorRepository;
    private final BizDonationRecordRepository donationRecordRepository;

    public DataInitializer(
            SysUserRepository userRepository,
            SysRoleRepository roleRepository,
            SysPermissionRepository permissionRepository,
            SysUserRoleRepository userRoleRepository,
            SysRolePermissionRepository rolePermissionRepository,
            SysOrgRepository orgRepository,
            BizFundSourceRepository fundSourceRepository,
            BizAppropriationBatchRepository batchRepository,
            BizFundAllocationRepository allocationRepository,
            BizIndividualDonorRepository donorRepository,
            BizDonationRecordRepository donationRecordRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRoleRepository = userRoleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.orgRepository = orgRepository;
        this.fundSourceRepository = fundSourceRepository;
        this.batchRepository = batchRepository;
        this.allocationRepository = allocationRepository;
        this.donorRepository = donorRepository;
        this.donationRecordRepository = donationRecordRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        LocalDateTime n = LocalDateTime.now();
        SysOrg root = new SysOrg();
        root.setParentId(null);
        root.setName("中国");
        root.setLevelType(1);
        root.setRegionCode("000000");
        root.setSortOrder(0);
        root.setCreatedAt(n);
        root.setUpdatedAt(n);
        root.setDeleted(false);
        orgRepository.save(root);

        Map<String, Long> permIds = new HashMap<>();
        permIds.put("m_nation", savePerm(null, 1, "m_nation", "国家层面", "/nation", null, 10, n));
        permIds.put("m_charity", savePerm(null, 1, "m_charity", "个人慈善", "/charity", null, 20, n));
        permIds.put("m_beneficiary", savePerm(null, 1, "m_beneficiary", "被扶贫对象", "/beneficiary", null, 30, n));
        permIds.put("m_admin", savePerm(null, 1, "m_admin", "系统管理", "/admin", null, 40, n));

        permIds.put("nation_sources", savePerm(permIds.get("m_nation"), 2, "nation:fund-source:view", "资金来源", "/nation/fund-sources", "/api/nation/fund-sources**", 11, n));
        permIds.put("nation_batches", savePerm(permIds.get("m_nation"), 2, "nation:batch:view", "拨款批次", "/nation/batches", "/api/nation/batches**", 12, n));
        permIds.put("nation_alloc", savePerm(permIds.get("m_nation"), 2, "nation:alloc:view", "划拨流水", "/nation/allocations", "/api/nation/allocations**", 13, n));

        permIds.put("charity_donors", savePerm(permIds.get("m_charity"), 2, "charity:donor:view", "捐赠人", "/charity/donors", "/api/charity/donors**", 21, n));
        permIds.put("charity_donations", savePerm(permIds.get("m_charity"), 2, "charity:donation:view", "捐赠记录", "/charity/donations", "/api/charity/donations**", 22, n));

        permIds.put("ben_hh", savePerm(permIds.get("m_beneficiary"), 2, "beneficiary:household:view", "户档案", "/beneficiary/households", "/api/beneficiary/households**", 31, n));
        permIds.put("ben_rec", savePerm(permIds.get("m_beneficiary"), 2, "beneficiary:receipt:view", "到户资金", "/beneficiary/receipts", "/api/beneficiary/receipts**", 32, n));
        permIds.put("ben_need", savePerm(permIds.get("m_beneficiary"), 2, "beneficiary:need:view", "贫困户需求", "/beneficiary/needs", "/api/beneficiary/needs**", 33, n));

        permIds.put("admin_users", savePerm(permIds.get("m_admin"), 2, "admin:user:view", "用户管理", "/admin/users", "/api/admin/users**", 41, n));
        permIds.put("admin_roles", savePerm(permIds.get("m_admin"), 2, "admin:role:view", "角色管理", "/admin/roles", "/api/admin/roles**", 42, n));
        permIds.put("admin_perms", savePerm(permIds.get("m_admin"), 2, "admin:perm:view", "菜单权限", "/admin/permissions", "/api/admin/permissions**", 43, n));
        permIds.put("admin_orgs", savePerm(permIds.get("m_admin"), 2, "admin:org:view", "组织机构", "/admin/orgs", "/api/admin/orgs**", 44, n));
        permIds.put("admin_pwd", savePerm(permIds.get("m_admin"), 3, "admin:user:view-password", "查看用户密码", null, null, 45, n));

        SysRole adminRole = new SysRole();
        adminRole.setCode("SUPER_ADMIN");
        adminRole.setName("超级管理员");
        adminRole.setRemark("初始化");
        adminRole.setCreatedAt(n);
        adminRole.setUpdatedAt(n);
        adminRole.setDeleted(false);
        roleRepository.save(adminRole);

        for (Long pid : permIds.values()) {
            SysRolePermission rp = new SysRolePermission();
            rp.setRoleId(adminRole.getId());
            rp.setPermissionId(pid);
            rp.setCreatedAt(n);
            rp.setUpdatedAt(n);
            rp.setDeleted(false);
            rolePermissionRepository.save(rp);
        }

        SysUser admin = new SysUser();
        admin.setUsername("admin");
        admin.setPassword("admin123");
        admin.setRealName("管理员");
        admin.setOrgId(root.getId());
        admin.setStatus(1);
        admin.setCreatedAt(n);
        admin.setUpdatedAt(n);
        admin.setDeleted(false);
        userRepository.save(admin);

        SysUserRole ur = new SysUserRole();
        ur.setUserId(admin.getId());
        ur.setRoleId(adminRole.getId());
        ur.setCreatedAt(n);
        ur.setUpdatedAt(n);
        ur.setDeleted(false);
        userRoleRepository.save(ur);

        seedBizDemo(root.getId(), admin.getId(), n);
    }

    private Long savePerm(Long parentId, int type, String code, String name, String path, String api, int sort, LocalDateTime n) {
        SysPermission p = new SysPermission();
        p.setParentId(parentId);
        p.setPermType(type);
        p.setCode(code);
        p.setName(name);
        p.setPath(path);
        p.setApiPattern(api);
        p.setSortOrder(sort);
        p.setCreatedAt(n);
        p.setUpdatedAt(n);
        p.setDeleted(false);
        permissionRepository.save(p);
        return p.getId();
    }

    private void seedBizDemo(Long orgId, Long userId, LocalDateTime n) {
        BizFundSource fs = new BizFundSource();
        fs.setCode("GOV_CENTRAL");
        fs.setName("中央财政");
        fs.setRemark("演示");
        fs.setCreatedAt(n);
        fs.setUpdatedAt(n);
        fs.setCreatedBy(userId);
        fs.setUpdatedBy(userId);
        fs.setDeleted(false);
        fundSourceRepository.save(fs);

        BizAppropriationBatch b = new BizAppropriationBatch();
        b.setBatchNo("BATCH-DEMO-001");
        b.setFundSourceId(fs.getId());
        b.setTotalAmount(new BigDecimal("1000000.00"));
        b.setCurrency("CNY");
        b.setIssuedAt(LocalDate.now());
        b.setTitle("演示批次");
        b.setStatus("ISSUED");
        b.setCreatedAt(n);
        b.setUpdatedAt(n);
        b.setCreatedBy(userId);
        b.setUpdatedBy(userId);
        b.setDeleted(false);
        batchRepository.save(b);

        BizFundAllocation a = new BizFundAllocation();
        a.setBatchId(b.getId());
        a.setFromOrgId(null);
        a.setToOrgId(orgId);
        a.setAmount(new BigDecimal("500000.00"));
        a.setTransferAt(n);
        a.setBizNo("ALLOC-DEMO-001");
        a.setStatus("NORMAL");
        a.setCreatedAt(n);
        a.setUpdatedAt(n);
        a.setCreatedBy(userId);
        a.setUpdatedBy(userId);
        a.setDeleted(false);
        allocationRepository.save(a);

        BizIndividualDonor d = new BizIndividualDonor();
        d.setDonorType("PERSON");
        d.setDonorName("李善");
        d.setGender("女");
        d.setDisplayName("李善");
        d.setPhone("13800000000");
        d.setRemark("首批演示捐赠人");
        d.setCreatedAt(n);
        d.setUpdatedAt(n);
        d.setCreatedBy(userId);
        d.setUpdatedBy(userId);
        d.setDeleted(false);
        donorRepository.save(d);

        BizDonationRecord dr = new BizDonationRecord();
        dr.setDonorId(d.getId());
        dr.setAmount(new BigDecimal("500.00"));
        dr.setDonatedAt(n);
        dr.setDonationLocation("某省某市某县希望小学");
        dr.setRecipientName("王受助");
        dr.setRecipientAddress("某乡某村一组 18 号");
        dr.setRecipientFamilySituation("五口人，父母务农，两名学龄儿童，家庭人均收入低于当地低保线");
        dr.setGroupPhotoUrl("/uploads/demo/donation-001.jpg");
        dr.setRemark("现场捐赠并合影");
        dr.setCreatedAt(n);
        dr.setUpdatedAt(n);
        dr.setCreatedBy(userId);
        dr.setUpdatedBy(userId);
        dr.setDeleted(false);
        donationRecordRepository.save(dr);
    }
}
