package com.pgh.authservice.service;


import com.pgh.authservice.dto.AuthResponse;
import com.pgh.authservice.entity.RefreshToken;
import com.pgh.authservice.repository.RefreshTokenRepository;
import com.pgh.authservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService  jwtService;
    private final UserDetailsService userDetailsService;

    @Transactional
    public String createRefreshToken(String username) {
        String refreshToken = jwtService.generateRefreshToken(userDetailsService.loadUserByUsername(username));
        RefreshToken rt = buildRefreshToken(refreshToken, username);

        refreshTokenRepository.save(rt);
        return refreshToken;
    }

    @Transactional
    public Map<String,String> rotateRefreshToken(String oldRefresh) {

        Map<String, String> resp = new HashMap<>();

        RefreshToken token = refreshTokenRepository.findByToken(oldRefresh)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        String username = token.getUsername();
        UserDetails user = userDetailsService.loadUserByUsername(username);

        refreshTokenRepository.delete(token);

        String newAccess = jwtService.generateAccessToken(user);
        String newRefresh = jwtService.generateRefreshToken(user);

        RefreshToken rt = buildRefreshToken(newRefresh, username);
        refreshTokenRepository.save(rt);
        resp.put("access_token", newAccess);
        resp.put("refresh_token", newRefresh);
        return resp;
    }

    public boolean validateRefreshToken(String token) {
        Optional<RefreshToken> rt = refreshTokenRepository.findByToken(token);
        if (rt.isEmpty()) return false;
        return rt.get().getExpiryDate().after(new Date());
    }

    @Transactional
    public void deleteRefreshToken(String token) {
        if (refreshTokenRepository.existsByToken(token)) {
            refreshTokenRepository.deleteByToken(token);
        }
    }

    private RefreshToken buildRefreshToken(String token, String username) {
        return RefreshToken.builder()
                .token(token)
                .username(username)
                .expiryDate(jwtService.extractRefreshTokenExpiration(token))
                .build();
    }
}
