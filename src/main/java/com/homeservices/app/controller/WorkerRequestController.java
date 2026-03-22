package com.homeservices.app.controller;

import com.homeservices.app.dto.request.WorkerRequestDto;
import com.homeservices.app.dto.response.ApiResponse;
import com.homeservices.app.dto.response.WorkerRequestResponse;
import com.homeservices.app.service.impl.WorkerRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/requests")
@RequiredArgsConstructor
public class WorkerRequestController {

    private final WorkerRequestService workerRequestService;

    // POST /api/v1/requests
    @PostMapping
    public ResponseEntity<ApiResponse<WorkerRequestResponse>> createRequest(
            @Valid @RequestBody WorkerRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Request created",
                workerRequestService.createRequest(dto)));
    }

    // GET /api/v1/requests/track/{trackingId}  (public)
    @GetMapping("/track/{trackingId}")
    public ResponseEntity<ApiResponse<WorkerRequestResponse>> trackRequest(
            @PathVariable String trackingId) {
        return ResponseEntity.ok(ApiResponse.ok("Request found",
                workerRequestService.getByTrackingId(trackingId)));
    }

    // GET /api/v1/requests  — personnel only
    @GetMapping
    @PreAuthorize("hasRole('PERSONNEL')")
    public ResponseEntity<ApiResponse<List<WorkerRequestResponse>>> getAllRequests(
            @RequestParam(required = false) String status) {
        List<WorkerRequestResponse> requests = (status != null && !status.isBlank())
                ? workerRequestService.getRequestsByStatus(status)
                : workerRequestService.getAllRequests();
        return ResponseEntity.ok(ApiResponse.ok("Requests fetched", requests));
    }

    // GET /api/v1/requests/worker/{workerId}
    @GetMapping("/worker/{workerId}")
    public ResponseEntity<ApiResponse<List<WorkerRequestResponse>>> getRequestsByWorker(
            @PathVariable Long workerId) {
        return ResponseEntity.ok(ApiResponse.ok("Requests fetched",
                workerRequestService.getRequestsByWorker(workerId)));
    }

    // PATCH /api/v1/requests/{id}/status  — personnel only
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('PERSONNEL')")
    public ResponseEntity<ApiResponse<WorkerRequestResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.ok("Status updated",
                workerRequestService.updateStatus(id, status)));
    }
}
