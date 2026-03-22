package com.homeservices.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RatingResponse {
    private Long id;
    private double rating;
    private String comments;
    private LocalDateTime createdAt;
    private Long clientId;
    private String clientName;
    private Long workerId;
    private String workerName;
}
