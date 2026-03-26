package com.pgh.authservice.controller;

import com.pgh.authservice.dto.AuthResponse;
import com.pgh.authservice.dto.UserCreateRequest;
import com.pgh.authservice.dto.UserLogin;
import com.pgh.authservice.dto.UserResponse;
import com.pgh.authservice.repository.UserRepository;
import com.pgh.authservice.security.JwtService;
import com.pgh.authservice.service.AuthService;
import com.pgh.authservice.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserCreateRequest req){
        UserResponse userResponse = authService.createUser(req);
        return ResponseEntity.created(URI.create("/api/me/" + userResponse.getId())).body(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserLogin req){
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> body) {
        Map<String,String> resp = new HashMap<>();
        String oldRefresh = body.get("refreshToken");


        if (oldRefresh == null || !refreshTokenService.validateRefreshToken(oldRefresh)) {
            resp.put("error", "Refresh token không hợp lệ hoặc hết hạn");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }

        try {
            resp = refreshTokenService.rotateRefreshToken(oldRefresh);
        } catch (RuntimeException e) {
            resp.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        refreshTokenService.deleteRefreshToken(refreshToken);
        Map<String, String> resp = new HashMap<>();
        resp.put("msg", "Đăng xuất thành công");
        return ResponseEntity.ok(resp);
    }
}
