package com.homeservices.app.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client_details")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClientDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String budget;

    @Column(name = "job_description")
    private String jobDescription;

    private String urgency;

    @Column(name = "work_type")
    private String workType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private UserTable client;

    @OneToMany(mappedBy = "clientDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Rating> ratings = new ArrayList<>();

    @OneToMany(mappedBy = "clientDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ClientRating> clientRatings = new ArrayList<>();
}
