package com.pgh.authservice.service;

import com.pgh.authservice.dto.UserCreateRequest;
import com.pgh.authservice.dto.UserRespone;
import com.pgh.authservice.entity.User;
import com.pgh.authservice.enums.Role;
import com.pgh.authservice.mapper.UserMapper;
import com.pgh.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserRespone createUser(UserCreateRequest req) {
        User user = userMapper.toEntity(req);

        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
        return userMapper.toResponse(user);
    }
}
