package com.homeservices.app.dto.request;

import lombok.Data;

@Data
public class ClientDetailsRequest {
    private String budget;
    private String jobDescription;
    private String urgency;
    private String workType;
}
