package com.homeservices.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "brgy_personnel_details")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BrgyPersonnelDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "barangay_name")
    private String barangayName;

    @Column(name = "contact_number")
    private String contactNumber;

    private String position;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brgy_personnel_id")
    private UserTable brgyPersonnel;
}
