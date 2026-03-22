package com.homeservices.app.controller;

import com.homeservices.app.dto.request.UpdateProfileRequest;
import com.homeservices.app.dto.response.ApiResponse;
import com.homeservices.app.dto.response.UserResponse;
import com.homeservices.app.service.impl.FileStorageService;
import com.homeservices.app.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FileStorageService fileStorageService;

    // GET /api/v1/users  — admin only
    @GetMapping
    @PreAuthorize("hasRole('PERSONNEL')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.ok("Users fetched", userService.getAllUsers()));
    }

    // GET /api/v1/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("User fetched", userService.getUserById(id)));
    }

    // PUT /api/v1/users/{id}/profile
    @PutMapping("/{id}/profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @PathVariable Long id,
            @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Profile updated", userService.updateProfile(id, request)));
    }

    // POST /api/v1/users/{id}/profile-pic
    @PostMapping("/{id}/profile-pic")
    public ResponseEntity<ApiResponse<UserResponse>> uploadProfilePic(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        String fileUrl = "http://localhost:8080/api/files/download/" + fileName;
        UserResponse response = userService.updateProfilePic(id, fileUrl);
        return ResponseEntity.ok(ApiResponse.ok("Profile picture updated", response));
    }

    // PATCH /api/v1/users/{id}/availability
    @PatchMapping("/{id}/availability")
    public ResponseEntity<ApiResponse<String>> updateAvailability(
            @PathVariable Long id,
            @RequestParam boolean available) {
        userService.updateAvailability(id, available);
        return ResponseEntity.ok(ApiResponse.ok("Availability updated"));
    }

    // POST /api/v1/users/{id}/gov-id
    @PostMapping("/{id}/gov-id")
    public ResponseEntity<ApiResponse<String>> uploadGovId(
            @PathVariable Long id,
            @RequestParam("govIdFile") MultipartFile govIdFile,
            @RequestParam("selfieFile") MultipartFile selfieFile,
            @RequestParam("govIdNumber") String govIdNumber) {

        String govIdFileName = fileStorageService.storeFile(govIdFile);
        String selfieFileName = fileStorageService.storeFile(selfieFile);
        String govIdUrl = "http://localhost:8080/api/files/download/" + govIdFileName;
        String selfieUrl = "http://localhost:8080/api/files/download/" + selfieFileName;

        userService.updateGovId(id, govIdUrl, govIdNumber, selfieUrl);
        return ResponseEntity.ok(ApiResponse.ok("Government ID uploaded successfully"));
    }
}
