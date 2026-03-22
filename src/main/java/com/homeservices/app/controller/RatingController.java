package com.homeservices.app.controller;

import com.homeservices.app.dto.request.RatingRequest;
import com.homeservices.app.dto.response.ApiResponse;
import com.homeservices.app.dto.response.RatingResponse;
import com.homeservices.app.dto.response.WorkerDetailsResponse;
import com.homeservices.app.entity.UserTable;
import com.homeservices.app.repository.UserRepository;
import com.homeservices.app.security.JwtUtil;
import com.homeservices.app.service.impl.RatingService;
import com.homeservices.app.service.impl.WorkerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;
    private final UserRepository userRepository;     // ← add this
    private final JwtUtil jwtUtil; 
    private final WorkerService workerService;

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
    
 // POST /api/v1/users/worker/rating/{workerId}
    @PostMapping("/users/worker/rating/{workerId}")
    public ResponseEntity<Map<String, Object>> rateWorker(
            @PathVariable Long workerId,
            @RequestBody RatingRequest request,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtUtil.extractUsername(token);
        UserTable client = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        request.setClientId(client.getId());
        request.setWorkerId(workerId);
        ratingService.submitRating(request);

        Double avg = ratingService.getAverageRating(workerId);
        Map<String, Object> result = new HashMap<>();
        result.put("averageRating", avg != null ? avg : 0.0);
        return ResponseEntity.ok(result);
    }

    // GET /api/v1/users/worker/rate/client?name=&lastName=
    @GetMapping("/users/worker/rate/client")
    public ResponseEntity<List<WorkerDetailsResponse>> searchWorkerByName(
            @RequestParam String name,
            @RequestParam String lastName) {
        List<WorkerDetailsResponse> workers = workerService.searchByName(name, lastName);
        return ResponseEntity.ok(workers);
    }
}
