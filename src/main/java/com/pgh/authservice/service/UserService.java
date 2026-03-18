package com.pgh.authservice.service;

import com.pgh.authservice.dto.UserCreateRequest;
import com.pgh.authservice.dto.UserRespone;
import com.pgh.authservice.dto.UserUpdateRequest;
import com.pgh.authservice.entity.User;
import com.pgh.authservice.enums.Role;
import com.pgh.authservice.mapper.UserMapper;
import com.pgh.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper  userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserRespone findByIdOrThrow(Long id) {
        User user = userRepository.findById(id).orElseThrow(()
                -> new RuntimeException("User not found"));
        return userMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserRespone> getAll() {
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }



    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(()
                -> new RuntimeException("User not found"));
        user.setIsActive(false);
    }
}
