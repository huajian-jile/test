package org.example.test.web.dto;

import java.util.List;

public record UserMeResponse(Long userId, String username, List<String> authorities) {
}
