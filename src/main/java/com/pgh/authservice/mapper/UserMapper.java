package com.pgh.authservice.mapper;

import com.pgh.authservice.dto.UserCreateRequest;
import com.pgh.authservice.dto.UserRespone;
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

    public UserRespone toResponse(User user) {
        return UserRespone.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .isActive(user.getIsActive())
                .build();
    }
}
