package com.pgh.authservice.mapper;

import com.pgh.authservice.dto.UserCreateRequest;
import com.pgh.authservice.dto.UserResponse;
import com.pgh.authservice.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(UserCreateRequest req) {
        return User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .isActive(false)
                .build();
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .build();
    }
}
