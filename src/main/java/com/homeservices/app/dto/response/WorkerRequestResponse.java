package com.homeservices.app.dto.response;

import com.homeservices.app.entity.WorkerRequest.CertificateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class WorkerRequestResponse {
    private Long id;
    private String trackingId;
    private String fullname;
    private String address;
    private String contactNumber;
    private String purpose;
    private String type;
    private String status;
    private LocalDate claimDate;
    private Long workerId;
    private String workerName;
    private List<CertificateType> certificateTypes;
}
