package com.app.userservice.controller;

import com.app.userservice.dto.RegisterRequest;
import com.app.userservice.dto.UserResponse;
import com.app.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserProfileById(@PathVariable String userId)
    {
        return ResponseEntity.ok(userService.getUserProfileById(userId));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest registerRequest)
    {
        return ResponseEntity.ok(userService.register(registerRequest));
    }
    @GetMapping("/{userId}/validate")
    public ResponseEntity<Boolean> validateuser(@PathVariable String userId)
    {
        return ResponseEntity.ok(userService.existsByUserId(userId));
    }
}
