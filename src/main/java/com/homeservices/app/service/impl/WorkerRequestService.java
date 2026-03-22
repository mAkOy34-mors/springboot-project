package com.homeservices.app.service.impl;

import com.homeservices.app.dto.request.WorkerRequestDto;
import com.homeservices.app.dto.response.WorkerRequestResponse;
import com.homeservices.app.entity.UserTable;
import com.homeservices.app.entity.WorkerRequest;
import com.homeservices.app.repository.UserRepository;
import com.homeservices.app.repository.WorkerRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkerRequestService {

    private final WorkerRequestRepository workerRequestRepository;
    private final UserRepository userRepository;

    @Transactional
    public WorkerRequestResponse createRequest(WorkerRequestDto dto) {
        UserTable worker = userRepository.findById(dto.getWorkerId())
                .orElseThrow(() -> new RuntimeException("Worker not found: " + dto.getWorkerId()));

        String trackingId = "HS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        WorkerRequest request = WorkerRequest.builder()
                .fullname(dto.getFullname())
                .address(dto.getAddress())
                .contactNumber(dto.getContactNumber())
                .purpose(dto.getPurpose())
                .type(dto.getType())
                .status("PENDING")
                .trackingId(trackingId)
                .claimDate(dto.getClaimDate())
                .worker(worker)
                .certificateTypes(dto.getCertificateTypes() != null ? dto.getCertificateTypes() : List.of())
                .build();

        workerRequestRepository.save(request);
        return toResponse(request);
    }

    public WorkerRequestResponse getByTrackingId(String trackingId) {
        WorkerRequest request = workerRequestRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RuntimeException("Request not found with tracking ID: " + trackingId));
        return toResponse(request);
    }

    public List<WorkerRequestResponse> getRequestsByWorker(Long workerId) {
        return workerRequestRepository.findByWorkerId(workerId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<WorkerRequestResponse> getAllRequests() {
        return workerRequestRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<WorkerRequestResponse> getRequestsByStatus(String status) {
        return workerRequestRepository.findByStatus(status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public WorkerRequestResponse updateStatus(Long id, String status) {
        WorkerRequest request = workerRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found: " + id));
        request.setStatus(status);
        workerRequestRepository.save(request);
        return toResponse(request);
    }

    // ── Mapper ─────────────────────────────────────────────────
    private WorkerRequestResponse toResponse(WorkerRequest r) {
        return WorkerRequestResponse.builder()
                .id(r.getId())
                .trackingId(r.getTrackingId())
                .fullname(r.getFullname())
                .address(r.getAddress())
                .contactNumber(r.getContactNumber())
                .purpose(r.getPurpose())
                .type(r.getType())
                .status(r.getStatus())
                .claimDate(r.getClaimDate())
                .workerId(r.getWorker() != null ? r.getWorker().getId() : null)
                .workerName(r.getWorker() != null ? r.getWorker().getUsername() : null)
                .certificateTypes(r.getCertificateTypes())
                .build();
    }
}
