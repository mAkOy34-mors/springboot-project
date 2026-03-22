package com.homeservices.app.service.impl;

import com.homeservices.app.dto.request.RatingRequest;
import com.homeservices.app.dto.response.RatingResponse;
import com.homeservices.app.entity.*;
import com.homeservices.app.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final WorkerRatingRepository workerRatingRepository;
    private final UserRepository userRepository;
    private final WorkerDetailsRepository workerDetailsRepository;
    private final ClientDetailsRepository clientDetailsRepository;

    @Transactional
    public RatingResponse submitRating(RatingRequest request) {
        UserTable client = userRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found: " + request.getClientId()));
        UserTable worker = userRepository.findById(request.getWorkerId())
                .orElseThrow(() -> new RuntimeException("Worker not found: " + request.getWorkerId()));
        WorkerDetails workerDetails = workerDetailsRepository.findById(request.getWorkerDetailsId())
                .orElseThrow(() -> new RuntimeException("Worker details not found: " + request.getWorkerDetailsId()));
        ClientDetails clientDetails = clientDetailsRepository.findById(request.getClientDetailsId())
                .orElseThrow(() -> new RuntimeException("Client details not found: " + request.getClientDetailsId()));

        Rating rating = Rating.builder()
                .rating(request.getRating())
                .comments(request.getComments())
                .client(client)
                .worker(worker)
                .workerDetails(workerDetails)
                .clientDetails(clientDetails)
                .build();
        ratingRepository.save(rating);

        // Update average on worker_details
        Double avg = ratingRepository.getAverageRatingByWorkerId(worker.getId());
        workerDetails.setRatings(avg);
        workerDetailsRepository.save(workerDetails);

        return toResponse(rating);
    }

    public List<RatingResponse> getRatingsByWorker(Long workerId) {
        return ratingRepository.findByWorkerId(workerId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<RatingResponse> getRatingsByClient(Long clientId) {
        return ratingRepository.findByClientId(clientId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Double getAverageRating(Long workerId) {
        return ratingRepository.getAverageRatingByWorkerId(workerId);
    }

    // ── Mapper ─────────────────────────────────────────────────
    private RatingResponse toResponse(Rating r) {
        return RatingResponse.builder()
                .id(r.getId())
                .rating(r.getRating())
                .comments(r.getComments())
                .createdAt(r.getCreatedAt())
                .clientId(r.getClient() != null ? r.getClient().getId() : null)
                .clientName(r.getClient() != null ? r.getClient().getUsername() : null)
                .workerId(r.getWorker() != null ? r.getWorker().getId() : null)
                .workerName(r.getWorker() != null ? r.getWorker().getUsername() : null)
                .build();
    }
}
