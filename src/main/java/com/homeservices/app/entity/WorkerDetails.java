package com.homeservices.app.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "worker_details")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WorkerDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String documents;

    private Double ratings;
    private String reviews;

    @Column(name = "work_experience")
    private String workExperience;

    @Column(name = "work_type")
    private String workType;

    @Column(name = "is_verified")
    private Boolean isVerified;

    private String comments;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    private UserTable worker;

    @OneToMany(mappedBy = "workerDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkerDocument> workerDocuments = new ArrayList<>();

    @OneToMany(mappedBy = "workerDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Rating> ratingsReceived = new ArrayList<>();

    @OneToMany(mappedBy = "workerDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkerRating> workerRatings = new ArrayList<>();
}
