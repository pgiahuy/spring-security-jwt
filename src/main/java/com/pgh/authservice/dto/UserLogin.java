package com.pgh.authservice.dto;

import lombok.Data;

@Data
public class UserLogin {
    private String username;
    private String password;
}
