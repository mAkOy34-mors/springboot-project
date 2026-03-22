package com.homeservices.app.service.impl;

import com.homeservices.app.dto.request.LoginRequest;
import com.homeservices.app.dto.request.RegisterRequest;
import com.homeservices.app.dto.response.AuthResponse;
import com.homeservices.app.entity.UserProfile;
import com.homeservices.app.entity.UserRole;
import com.homeservices.app.entity.UserTable;
import com.homeservices.app.repository.UserProfileRepository;
import com.homeservices.app.repository.UserRepository;
import com.homeservices.app.repository.UserRoleRepository;
import com.homeservices.app.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Transactional
    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already registered.");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken.");
        }

        // Validate role
        String role = request.getRole().toUpperCase();
        if (!List.of("CLIENT", "WORKER", "PERSONNEL").contains(role)) {
            throw new RuntimeException("Invalid role. Must be CLIENT, WORKER, or PERSONNEL.");
        }

        // Save user
        UserTable user = UserTable.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isAvailable(true)
                .build();
        userRepository.save(user);

     // Save role
        UserRole userRole = UserRole.builder()
                .userId(user.getId())
                .roleName(role)
                .build();
        userRoleRepository.save(userRole);


        return "User registered successfully.";
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        UserTable user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found."));

        // Check if user has a profile set up
        boolean hasProfile = user.getUserProfile() != null
                && user.getUserProfile().getName() != null
                && !user.getUserProfile().getName().isBlank();

        // Roles with ROLE_ prefix to match frontend
        List<String> roles = user.getRoles().stream()
                .map(role -> "ROLE_" + role.getRoleName())
                .collect(Collectors.toList());

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(roles)            // ← role not roles
                .hasProfile(hasProfile) // ← hasProfile flag
                .build();
    }
}
