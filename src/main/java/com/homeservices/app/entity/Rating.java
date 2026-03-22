package com.homeservices.app.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

// ── Rating (combined ratings table) ─────────────────────────────
@Entity
@Table(name = "ratings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double rating;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @Column(name = "created_at", nullable = false)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_details_id", nullable = false)
    private ClientDetails clientDetails;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }
}
