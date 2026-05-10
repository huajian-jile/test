-- 个人慈善：捐赠人（姓名、性别、手机、备注）；捐赠记录（地点、对方信息、合照等）

ALTER TABLE biz_individual_donor
    ADD COLUMN donor_name VARCHAR(64) NOT NULL DEFAULT '' COMMENT '姓名',
    ADD COLUMN gender VARCHAR(16) NULL COMMENT '性别',
    ADD COLUMN remark VARCHAR(500) NULL COMMENT '备注';

UPDATE biz_individual_donor SET donor_name = display_name WHERE (donor_name = '' OR donor_name IS NULL) AND display_name IS NOT NULL;

UPDATE biz_individual_donor SET donor_name = '未登记' WHERE donor_name = '' OR donor_name IS NULL;

ALTER TABLE biz_individual_donor MODIFY display_name VARCHAR(128) NULL;

ALTER TABLE biz_donation_record
    ADD COLUMN donation_location VARCHAR(255) NULL COMMENT '捐赠地点',
    ADD COLUMN recipient_name VARCHAR(128) NULL COMMENT '对方名称',
    ADD COLUMN recipient_address VARCHAR(500) NULL COMMENT '对方地址',
    ADD COLUMN recipient_family_situation VARCHAR(2000) NULL COMMENT '对方家庭状况',
    ADD COLUMN group_photo_url VARCHAR(512) NULL COMMENT '合照照片URL';
