package com.homeservices.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ClientDetailsResponse {
    private Long id;
    private Long clientId;
    private String clientName;
    private String budget;
    private String jobDescription;
    private String urgency;
    private String workType;
}
