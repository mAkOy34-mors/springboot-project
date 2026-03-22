package com.homeservices.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class WorkerDocumentResponse {
    private Long id;
    private String fileName;
    private String fileUrl;
}
