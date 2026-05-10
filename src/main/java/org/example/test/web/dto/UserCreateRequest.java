package org.example.test.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequest(
        @NotBlank String username,
        @NotBlank String password,
        String realName,
        String phone,
        Long orgId,
        @NotNull Integer status) {
}
