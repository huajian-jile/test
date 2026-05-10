package org.example.test.web.dto;

import java.util.List;

public record MenuNodeResponse(
        Long id,
        Long parentId,
        Integer type,
        String code,
        String name,
        String path,
        Integer sort,
        List<MenuNodeResponse> children) {
}
