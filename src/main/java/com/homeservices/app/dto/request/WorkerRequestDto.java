package com.homeservices.app.dto.request;

import com.homeservices.app.entity.WorkerRequest.CertificateType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class WorkerRequestDto {

    @NotNull(message = "Worker ID is required")
    private Long workerId;

    @NotBlank(message = "Full name is required")
    private String fullname;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Contact number is required")
    private String contactNumber;

    private String purpose;
    private String type;
    private LocalDate claimDate;
    private List<CertificateType> certificateTypes;
}
