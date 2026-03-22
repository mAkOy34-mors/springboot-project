package com.homeservices.app.controller;

import com.homeservices.app.dto.request.ClientDetailsRequest;
import com.homeservices.app.dto.response.ApiResponse;
import com.homeservices.app.dto.response.ClientDetailsResponse;
import com.homeservices.app.service.impl.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    // GET /api/v1/clients  — personnel only
    @GetMapping
    @PreAuthorize("hasRole('PERSONNEL')")
    public ResponseEntity<ApiResponse<List<ClientDetailsResponse>>> getAllClients() {
        return ResponseEntity.ok(ApiResponse.ok("Clients fetched", clientService.getAllClients()));
    }

    // GET /api/v1/clients/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<ClientDetailsResponse>> getClientByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.ok("Client fetched", clientService.getClientByUserId(userId)));
    }

    // POST /api/v1/clients/{userId}/details
    @PostMapping("/{userId}/details")
    public ResponseEntity<ApiResponse<ClientDetailsResponse>> saveClientDetails(
            @PathVariable Long userId,
            @RequestBody ClientDetailsRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Client details saved",
                clientService.createOrUpdateClientDetails(userId, request)));
    }
}
