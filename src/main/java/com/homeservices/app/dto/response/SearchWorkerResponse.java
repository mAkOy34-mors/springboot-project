package com.homeservices.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SearchWorkerResponse {
    private Long id;
    private String name;
    private String lastName;
    private String address;
    private String workType;
    private String workExperience;
    private String profilePic;
    private Boolean isVerified;
    private String documents;      // ← raw JSON string as frontend expects
    private Double averageRating;
    private Integer totalRatings;
    private String latestComment;
}