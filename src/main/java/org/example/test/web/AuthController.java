package org.example.test.web;

import jakarta.validation.Valid;
import org.example.test.common.api.ApiResult;
import org.example.test.security.AppPrincipal;
import org.example.test.service.AuthService;
import org.example.test.service.MenuService;
import org.example.test.web.dto.LoginRequest;
import org.example.test.web.dto.LoginResponse;
import org.example.test.web.dto.MenuNodeResponse;
import org.example.test.web.dto.UserMeResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final MenuService menuService;

    public AuthController(AuthService authService, MenuService menuService) {
        this.authService = authService;
        this.menuService = menuService;
    }

    @PostMapping("/login")
    public ApiResult<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        return ApiResult.ok(authService.login(req));
    }

    @GetMapping("/me")
    public ApiResult<UserMeResponse> me(Authentication authentication) {
        AppPrincipal p = menuService.currentPrincipal();
        if (p == null) {
            return ApiResult.fail(401, "未登录");
        }
        List<String> auths = new ArrayList<>();
        for (GrantedAuthority ga : authentication.getAuthorities()) {
            auths.add(ga.getAuthority());
        }
        return ApiResult.ok(new UserMeResponse(p.getUserId(), p.getUsername(), auths));
    }

    @GetMapping("/menus")
    public ApiResult<List<MenuNodeResponse>> menus() {
        return ApiResult.ok(menuService.menuTreeForCurrentUser());
    }
}
