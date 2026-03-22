package com.homeservices.app.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RatingRequest {

    @NotNull(message = "Worker ID is required")
    private Long workerId;

    @NotNull(message = "Client ID is required")
    private Long clientId;

    @NotNull(message = "Worker details ID is required")
    private Long workerDetailsId;

    @NotNull(message = "Client details ID is required")
    private Long clientDetailsId;

    @Min(1) @Max(5)
    private double rating;

    private String comments;
}
