package com.pgh.authservice.controller;

import com.pgh.authservice.dto.UserCreateRequest;
import com.pgh.authservice.dto.UserRespone;
import com.pgh.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserRespone>> findAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRespone> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findByIdOrThrow(id));
    }

    @PostMapping
    public ResponseEntity<UserRespone> registor(@RequestBody UserCreateRequest req){
        UserRespone userRespone = userService.createUser(req);
        return ResponseEntity.created(URI.create("/api/admin/users/"+userRespone.getId())).body(userRespone);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<UserRespone> deleteById(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
