-- MySQL 8+ / utf8mb4

CREATE TABLE sys_org (
    id BIGINT NOT NULL AUTO_INCREMENT,
    parent_id BIGINT,
    name VARCHAR(128) NOT NULL,
    level_type INT NOT NULL,
    region_code VARCHAR(32),
    sort_order INT DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    login_at TIMESTAMP NULL,
    created_by BIGINT,
    updated_by BIGINT,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE sys_user (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL,
    password VARCHAR(255) NOT NULL,
    real_name VARCHAR(64),
    phone VARCHAR(20),
    org_id BIGINT,
    status INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    login_at TIMESTAMP NULL,
    created_by BIGINT,
    updated_by BIGINT,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT uk_sys_user_username UNIQUE (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE sys_role (
    id BIGINT NOT NULL AUTO_INCREMENT,
    code VARCHAR(64) NOT NULL,
    name VARCHAR(64) NOT NULL,
    remark VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    login_at TIMESTAMP NULL,
    created_by BIGINT,
    updated_by BIGINT,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT uk_sys_role_code UNIQUE (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE sys_permission (
    id BIGINT NOT NULL AUTO_INCREMENT,
    parent_id BIGINT,
    perm_type INT NOT NULL,
    code VARCHAR(128) NOT NULL,
    name VARCHAR(64) NOT NULL,
    path VARCHAR(255),
    api_pattern VARCHAR(255),
    sort_order INT DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    login_at TIMESTAMP NULL,
    created_by BIGINT,
    updated_by BIGINT,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT uk_sys_permission_code UNIQUE (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE sys_user_role (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    login_at TIMESTAMP NULL,
    created_by BIGINT,
    updated_by BIGINT,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES sys_user(id),
    CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES sys_role(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE sys_role_permission (
    id BIGINT NOT NULL AUTO_INCREMENT,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    login_at TIMESTAMP NULL,
    created_by BIGINT,
    updated_by BIGINT,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_rp_role FOREIGN KEY (role_id) REFERENCES sys_role(id),
    CONSTRAINT fk_rp_perm FOREIGN KEY (permission_id) REFERENCES sys_permission(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE biz_fund_source (
    id BIGINT NOT NULL AUTO_INCREMENT,
    code VARCHAR(64) NOT NULL,
    name VARCHAR(128) NOT NULL,
    remark VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    login_at TIMESTAMP NULL,
    created_by BIGINT,
    updated_by BIGINT,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT uk_biz_fund_source_code UNIQUE (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE biz_appropriation_batch (
    id BIGINT NOT NULL AUTO_INCREMENT,
    batch_no VARCHAR(64) NOT NULL,
    fund_source_id BIGINT NOT NULL,
    total_amount DECIMAL(18,2) NOT NULL,
    currency VARCHAR(8) NOT NULL DEFAULT 'CNY',
    issued_at DATE,
    title VARCHAR(255),
    remark VARCHAR(500),
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    login_at TIMESTAMP NULL,
    created_by BIGINT,
    updated_by BIGINT,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT uk_batch_no UNIQUE (batch_no),
    CONSTRAINT fk_batch_fund FOREIGN KEY (fund_source_id) REFERENCES biz_fund_source(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE biz_fund_allocation (
    id BIGINT NOT NULL AUTO_INCREMENT,
    batch_id BIGINT NOT NULL,
    from_org_id BIGINT,
    to_org_id BIGINT NOT NULL,
    amount DECIMAL(18,2) NOT NULL,
    transfer_at TIMESTAMP NOT NULL,
    biz_no VARCHAR(64) NOT NULL,
    status VARCHAR(32) NOT NULL,
    replaced_by_id BIGINT,
    remark VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    login_at TIMESTAMP NULL,
    created_by BIGINT,
    updated_by BIGINT,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT uk_biz_no UNIQUE (biz_no),
    CONSTRAINT fk_alloc_batch FOREIGN KEY (batch_id) REFERENCES biz_appropriation_batch(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE biz_household (
    id BIGINT NOT NULL AUTO_INCREMENT,
    household_no VARCHAR(64) NOT NULL,
    head_name VARCHAR(64) NOT NULL,
    org_id BIGINT,
    address VARCHAR(255),
    poverty_flag TINYINT(1) DEFAULT 0,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    login_at TIMESTAMP NULL,
    created_by BIGINT,
    updated_by BIGINT,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT uk_household_no UNIQUE (household_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE biz_household_fund_receipt (
    id BIGINT NOT NULL AUTO_INCREMENT,
    household_id BIGINT NOT NULL,
    allocation_id BIGINT,
    amount DECIMAL(18,2) NOT NULL,
    received_at TIMESTAMP NOT NULL,
    purpose VARCHAR(255),
    remark VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    login_at TIMESTAMP NULL,
    created_by BIGINT,
    updated_by BIGINT,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_receipt_hh FOREIGN KEY (household_id) REFERENCES biz_household(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE biz_need_request (
    id BIGINT NOT NULL AUTO_INCREMENT,
    household_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    category VARCHAR(64) NOT NULL,
    description VARCHAR(2000),
    urgency VARCHAR(32),
    status VARCHAR(32) NOT NULL,
    audited_by BIGINT,
    audited_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    login_at TIMESTAMP NULL,
    created_by BIGINT,
    updated_by BIGINT,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_need_hh FOREIGN KEY (household_id) REFERENCES biz_household(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE biz_individual_donor (
    id BIGINT NOT NULL AUTO_INCREMENT,
    donor_type VARCHAR(32) NOT NULL,
    display_name VARCHAR(128) NOT NULL,
    phone VARCHAR(20),
    user_id BIGINT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    login_at TIMESTAMP NULL,
    created_by BIGINT,
    updated_by BIGINT,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE biz_donation_record (
    id BIGINT NOT NULL AUTO_INCREMENT,
    donor_id BIGINT NOT NULL,
    amount DECIMAL(18,2) NOT NULL,
    donated_at TIMESTAMP NOT NULL,
    channel VARCHAR(32),
    target_type VARCHAR(32),
    target_id BIGINT,
    certificate_no VARCHAR(64),
    public_visible TINYINT(1) DEFAULT 1,
    remark VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    login_at TIMESTAMP NULL,
    created_by BIGINT,
    updated_by BIGINT,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_donation_donor FOREIGN KEY (donor_id) REFERENCES biz_individual_donor(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_sys_user_org ON sys_user(org_id);
CREATE INDEX idx_sys_user_deleted ON sys_user(deleted);
CREATE INDEX idx_perm_parent ON sys_permission(parent_id);
CREATE INDEX idx_alloc_batch ON biz_fund_allocation(batch_id);
CREATE INDEX idx_need_household ON biz_need_request(household_id);
