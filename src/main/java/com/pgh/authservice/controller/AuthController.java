package com.pgh.authservice.controller;

import com.pgh.authservice.dto.UserCreateRequest;
import com.pgh.authservice.dto.UserLogin;
import com.pgh.authservice.dto.UserRespone;
import com.pgh.authservice.service.AuthService;
import com.pgh.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.RequiredTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserRespone> register(@RequestBody UserCreateRequest req){
        UserRespone userRespone = authService.createUser(req);
        return ResponseEntity.created(URI.create("/api/me/" + userRespone.getId())).body(userRespone);
    }

    @PostMapping("/login")
    public ResponseEntity<UserRespone> login(@RequestBody UserLogin req){

    }
}
