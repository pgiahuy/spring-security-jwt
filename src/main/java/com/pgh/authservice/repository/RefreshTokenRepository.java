package com.pgh.authservice.repository;

import com.pgh.authservice.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String token);
    Boolean existsByToken(String token0);
    void deleteByToken(String token);
}
