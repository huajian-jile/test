package org.example.test.web.dto;

import java.time.LocalDateTime;

public record UserAdminDto(
        Long id,
        String username,
        String password,
        String realName,
        String phone,
        Long orgId,
        Integer status,
        LocalDateTime loginAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long createdBy,
        Long updatedBy,
        Boolean deleted) {
}
