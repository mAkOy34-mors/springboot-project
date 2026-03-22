package com.homeservices.app.controller;

import com.homeservices.app.dto.request.ClientDetailsRequest;
import com.homeservices.app.dto.request.SearchWorkerRequest;
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
import com.homeservices.app.dto.response.ClientDetailsResponse;
import com.homeservices.app.dto.response.SearchWorkerResponse;
import com.homeservices.app.dto.response.WorkerDetailsResponse;
import com.homeservices.app.entity.UserTable;
import com.homeservices.app.repository.UserRepository;
import com.homeservices.app.security.JwtUtil;
import com.homeservices.app.service.impl.ClientService;
import com.homeservices.app.service.impl.WorkerService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final ClientService clientService;       // ← add this
    private final WorkerService workerService;       // ← add this
    private final UserRepository userRepository;     // ← add this
    private final JwtUtil jwtUtil; 
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
 // POST /api/v1/users/setup
    @PostMapping(value = "/setup", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<UserResponse>> setupProfile(
            @RequestParam("name") String name,
            @RequestParam("lastName") String lastName,
            @RequestParam("address") String address,
            @RequestParam(value = "profilePic", required = false) MultipartFile profilePic,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtUtil.extractUsername(token);
        UserTable user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setName(name);
        request.setLastName(lastName);
        request.setAddress(address);
        UserResponse response = userService.updateProfile(user.getId(), request);

        if (profilePic != null && !profilePic.isEmpty()) {
            String fileName = fileStorageService.storeFile(profilePic);
            String fileUrl = "http://localhost:8080/api/files/download/" + fileName;
            response = userService.updateProfilePic(user.getId(), fileUrl);
        }

        return ResponseEntity.ok(ApiResponse.ok("Profile setup complete", response));
    }

    // POST /api/v1/users/client  — submit job request
    @PostMapping("/client")
    public ResponseEntity<ApiResponse<ClientDetailsResponse>> submitJob(
            @RequestBody ClientDetailsRequest request,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtUtil.extractUsername(token);
        UserTable user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ClientDetailsResponse response = clientService.createOrUpdateClientDetails(user.getId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Job submitted", response));
    }

    // POST /api/v1/users/client/search — search workers by address + workType
 // POST /api/v1/users/client/search
    @PostMapping("/client/search")
    public ResponseEntity<List<SearchWorkerResponse>> searchWorkers(
            @RequestBody SearchWorkerRequest request) {
        List<SearchWorkerResponse> workers = 
                workerService.searchWorkersForClient(request.getWorkType());
        return ResponseEntity.ok(workers);
    }
}
