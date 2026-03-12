package com.pgh.authservice.dto;

import com.pgh.authservice.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRespone {

    private Long id;
    private String username;
    private String email;
    private Boolean isActive;
    private Role role;
}
