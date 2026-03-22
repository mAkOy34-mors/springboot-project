package com.homeservices.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class WorkerDetailsResponse {
    private Long id;
    private Long workerId;
    private String workerName;
    private String profilePic;
    private String workType;
    private String workExperience;
    private String reviews;
    private String comments;
    private Double ratings;
    private Boolean isVerified;
    private List<String> documents;
    private List<WorkerDocumentResponse> workerDocuments;
}
