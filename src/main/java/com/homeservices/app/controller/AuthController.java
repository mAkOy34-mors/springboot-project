package com.homeservices.app.controller;

import com.homeservices.app.dto.request.LoginRequest;
import com.homeservices.app.dto.request.RegisterRequest;
import com.homeservices.app.dto.response.ApiResponse;
import com.homeservices.app.dto.response.AuthResponse;
import com.homeservices.app.service.impl.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/v1/users/register
     * Accepts multipart/form-data (matches the existing HTML form)
     */
    @PostMapping(value = "/register", consumes = {"multipart/form-data", "application/x-www-form-urlencoded"})
    public ResponseEntity<String> register(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("role") String role) {

        RegisterRequest request = new RegisterRequest();
        request.setUsername(username);
        request.setEmail(email);
        request.setPassword(password);
        request.setRole(role);

        String result = authService.register(request);
        return ResponseEntity.ok(result);
    }

    /**
     * POST /api/v1/users/register (JSON alternative)
     */
    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<ApiResponse<String>> registerJson(@Valid @RequestBody RegisterRequest request) {
        String result = authService.register(request);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    /**
     * POST /api/v1/users/login
     */
    @PostMapping("/login")
    public ResponseEntity <AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
