package com.homeservices.app.dto.request;

import lombok.Data;

@Data
public class SearchWorkerRequest {
    private String address;
    private String workType;
}