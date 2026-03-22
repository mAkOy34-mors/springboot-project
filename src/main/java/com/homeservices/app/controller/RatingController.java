package com.homeservices.app.controller;

import com.homeservices.app.dto.request.RatingRequest;
import com.homeservices.app.dto.response.ApiResponse;
import com.homeservices.app.dto.response.RatingResponse;
import com.homeservices.app.service.impl.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    // POST /api/v1/ratings
    @PostMapping
    public ResponseEntity<ApiResponse<RatingResponse>> submitRating(
            @Valid @RequestBody RatingRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Rating submitted",
                ratingService.submitRating(request)));
    }

    // GET /api/v1/ratings/worker/{workerId}
    @GetMapping("/worker/{workerId}")
    public ResponseEntity<ApiResponse<List<RatingResponse>>> getRatingsByWorker(
            @PathVariable Long workerId) {
        return ResponseEntity.ok(ApiResponse.ok("Ratings fetched",
                ratingService.getRatingsByWorker(workerId)));
    }

    // GET /api/v1/ratings/client/{clientId}
    @GetMapping("/client/{clientId}")
    public ResponseEntity<ApiResponse<List<RatingResponse>>> getRatingsByClient(
            @PathVariable Long clientId) {
        return ResponseEntity.ok(ApiResponse.ok("Ratings fetched",
                ratingService.getRatingsByClient(clientId)));
    }

    // GET /api/v1/ratings/worker/{workerId}/average
    @GetMapping("/worker/{workerId}/average")
    public ResponseEntity<ApiResponse<Double>> getAverageRating(@PathVariable Long workerId) {
        return ResponseEntity.ok(ApiResponse.ok("Average rating fetched",
                ratingService.getAverageRating(workerId)));
    }
}
