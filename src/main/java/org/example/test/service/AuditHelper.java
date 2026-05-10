package org.example.test.service;

import org.example.test.domain.base.BaseEntity;
import org.example.test.security.SecurityUtils;

import java.time.LocalDateTime;

public final class AuditHelper {

    private AuditHelper() {
    }

    public static void onCreate(BaseEntity e) {
        Long uid = SecurityUtils.currentUserIdOrNull();
        LocalDateTime n = LocalDateTime.now();
        e.setCreatedAt(n);
        e.setUpdatedAt(n);
        e.setCreatedBy(uid);
        e.setUpdatedBy(uid);
        e.setDeleted(false);
    }

    public static void onUpdate(BaseEntity e) {
        Long uid = SecurityUtils.currentUserIdOrNull();
        e.setUpdatedAt(LocalDateTime.now());
        e.setUpdatedBy(uid);
    }
}
