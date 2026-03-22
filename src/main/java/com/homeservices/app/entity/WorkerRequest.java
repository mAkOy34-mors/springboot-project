package com.homeservices.app.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "worker_request")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WorkerRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

    @Column(name = "contact_number")
    private String contactNumber;

    private String purpose;
    private String type;
    private String status;
    private String fullname;

    @Column(name = "tracking_id", unique = true, nullable = false)
    private String trackingId;

    @Column(name = "claim_date")
    private LocalDate claimDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    private UserTable worker;

    @ElementCollection
    @CollectionTable(name = "request_certificate_types",
            joinColumns = @JoinColumn(name = "request_id"))
    @Column(name = "certificate_type")
    @Enumerated(EnumType.STRING)
    private List<CertificateType> certificateTypes = new ArrayList<>();

    public enum CertificateType {
        CLEARANCE, INDIGENCY, RESIDENCY
    }
}
