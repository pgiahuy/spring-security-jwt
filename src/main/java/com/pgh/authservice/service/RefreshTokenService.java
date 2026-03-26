package com.pgh.authservice.service;


import com.pgh.authservice.entity.RefreshToken;
import com.pgh.authservice.repository.RefreshTokenRepository;
import com.pgh.authservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repo;
    private final JwtService  jwtService;
    private final UserDetailsService userDetailsService;

    @Transactional
    public String createRefreshToken(String username) {
        String refreshToken = jwtService.generateRefreshToken(userDetailsService.loadUserByUsername(username));
        Date expiration = jwtService.extractRefreshTokenExpiration(refreshToken);
        RefreshToken rt = RefreshToken.builder()
                .token(refreshToken)
                .username(username)
                .expiryDate(expiration)
                .build();

        repo.save(rt);
        return refreshToken;
    }

    public boolean validateRefreshToken(String token) {
        Optional<RefreshToken> rt = repo.findByToken(token);
        if (rt.isEmpty()) return false;
        return rt.get().getExpiryDate().after(new Date());
    }

    @Transactional
    public void deleteRefreshToken(String token) {
        if (repo.existsByToken(token)) {
            repo.deleteByToken(token);
        }
    }
}
