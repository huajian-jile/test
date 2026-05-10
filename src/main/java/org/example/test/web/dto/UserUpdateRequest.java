package org.example.test.web.dto;

import jakarta.validation.constraints.NotNull;

public record UserUpdateRequest(
        String password,
        String realName,
        String phone,
        Long orgId,
        @NotNull Integer status) {
}
