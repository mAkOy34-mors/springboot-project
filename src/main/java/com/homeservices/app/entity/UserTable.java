package com.homeservices.app.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_table")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "time_stamp")
    private LocalDateTime timeStamp;

    @Column(name = "gov_id_image_path")
    private String govIdImagePath;

    @Column(name = "gov_id_number")
    private String govIdNumber;

    @Column(name = "selfie_path")
    private String selfiePath;

    // ── Relationships ────────────────────────────────────────────────

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile userProfile;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private List<UserRole> roles = new ArrayList<>();
    
    @OneToOne(mappedBy = "worker", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private WorkerDetails workerDetails;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ClientDetails clientDetails;

    @OneToOne(mappedBy = "brgyPersonnel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BrgyPersonnelDetails brgyPersonnelDetails;

    @PrePersist
    protected void onCreate() {
        timeStamp = LocalDateTime.now();
        if (isAvailable == null) isAvailable = true;
    }
}
