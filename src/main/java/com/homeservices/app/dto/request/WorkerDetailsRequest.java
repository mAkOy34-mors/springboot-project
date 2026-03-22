package com.homeservices.app.dto.request;

import lombok.Data;

@Data
public class WorkerDetailsRequest {
    private String workType;
    private String workExperience;
    private String reviews;
    private String comments;
}
