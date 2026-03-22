package com.homeservices.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "worker_document")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WorkerDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_url")
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private WorkerDetails workerDetails;
}
