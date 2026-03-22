package com.homeservices.app.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "worker_ratings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WorkerRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double rating;
    private String comments;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private UserTable client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    private UserTable worker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_details_id", nullable = false)
    private WorkerDetails workerDetails;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }
}
