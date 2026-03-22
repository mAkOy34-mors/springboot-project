package com.homeservices.app.controller;

import com.homeservices.app.dto.request.WorkerDetailsRequest;
import com.homeservices.app.dto.response.ApiResponse;
import com.homeservices.app.dto.response.WorkerDetailsResponse;
import com.homeservices.app.service.impl.FileStorageService;
import com.homeservices.app.service.impl.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workers")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerService workerService;
    private final FileStorageService fileStorageService;

    // GET /api/v1/workers
    @GetMapping
    public ResponseEntity<ApiResponse<List<WorkerDetailsResponse>>> getAllWorkers(
            @RequestParam(required = false) String workType) {
        List<WorkerDetailsResponse> workers = (workType != null && !workType.isBlank())
                ? workerService.getWorkersByType(workType)
                : workerService.getAllWorkers();
        return ResponseEntity.ok(ApiResponse.ok("Workers fetched", workers));
    }

    // GET /api/v1/workers/{id}  — by worker_details.id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkerDetailsResponse>> getWorkerById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Worker fetched", workerService.getWorkerById(id)));
    }

    // GET /api/v1/workers/user/{userId}  — by user_table.id
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<WorkerDetailsResponse>> getWorkerByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.ok("Worker fetched", workerService.getWorkerByUserId(userId)));
    }

    // POST /api/v1/workers/{userId}/details
    @PostMapping("/{userId}/details")
    public ResponseEntity<ApiResponse<WorkerDetailsResponse>> saveWorkerDetails(
            @PathVariable Long userId,
            @RequestBody WorkerDetailsRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Worker details saved",
                workerService.createOrUpdateWorkerDetails(userId, request)));
    }

    // POST /api/v1/workers/{userId}/documents
    @PostMapping("/{userId}/documents")
    public ResponseEntity<ApiResponse<WorkerDetailsResponse>> uploadDocument(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        String fileUrl = "http://localhost:8080/api/files/download/" + fileName;
        WorkerDetailsResponse response = workerService.addDocument(userId, file.getOriginalFilename(), fileUrl);
        return ResponseEntity.ok(ApiResponse.ok("Document uploaded", response));
    }

    // PATCH /api/v1/workers/{workerDetailsId}/verify  — personnel only
    @PatchMapping("/{workerDetailsId}/verify")
    @PreAuthorize("hasRole('PERSONNEL')")
    public ResponseEntity<ApiResponse<String>> verifyWorker(
            @PathVariable Long workerDetailsId,
            @RequestParam boolean verified) {
        workerService.verifyWorker(workerDetailsId, verified);
        return ResponseEntity.ok(ApiResponse.ok("Worker verification status updated"));
    }
}
