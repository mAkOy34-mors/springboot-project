package com.homeservices.app.controller;

import com.homeservices.app.dto.response.ApiResponse;
import com.homeservices.app.dto.response.UserResponse;
import com.homeservices.app.entity.UserTable;
import com.homeservices.app.repository.UserRepository;
import com.homeservices.app.security.JwtUtil;
import com.homeservices.app.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // GET /api/profile/get
    @GetMapping("/get")
    public ResponseEntity<UserResponse> getProfile(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtUtil.extractUsername(token);
        UserTable user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(userService.toUserResponse(user));
    }
}