package com.spring.otmanagement.controller;

import com.spring.otmanagement.dto.*;
import com.spring.otmanagement.entity.User;
import com.spring.otmanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/register")
    public UserResponse createUser(@RequestBody @Validated UserCreationRequest user) {
        return userService.createUser(user);
    }

    @GetMapping("/api/v1/users")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PatchMapping("/api/v1/users/{id}")
    public UserResponse updateUser(@PathVariable Long id, @Validated @RequestBody UserUpdateRequest user) {
        return this.userService.updateUser(id, user);
    }

    @DeleteMapping("/api/v1/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        this.userService.deleteUser(id);
    }

    @PostMapping("/auth/login")
    public AuthResponse login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }
}
