package org.example.test.web.dto;

import java.util.List;

public record LoginResponse(
        String token,
        Long userId,
        String username,
        String realName,
        List<String> roles,
        List<String> permissions) {
}
