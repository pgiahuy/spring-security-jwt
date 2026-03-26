package com.pgh.authservice.service;

import com.pgh.authservice.dto.AuthResponse;
import com.pgh.authservice.dto.UserCreateRequest;
import com.pgh.authservice.dto.UserLogin;
import com.pgh.authservice.dto.UserResponse;
import com.pgh.authservice.entity.User;
import com.pgh.authservice.enums.Role;
import com.pgh.authservice.mapper.UserMapper;
import com.pgh.authservice.repository.UserRepository;
import com.pgh.authservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public UserResponse createUser(UserCreateRequest req) {
        User user = userMapper.toEntity(req);

        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(Role.USER);
        user.setIsActive(true);

        userRepository.save(user);
        return userMapper.toResponse(user);
    }


    public AuthResponse login(UserLogin req) {

        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow();

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        String token = jwtService.generateAccessToken(

                new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
                )
        );


        String refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return AuthResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}
