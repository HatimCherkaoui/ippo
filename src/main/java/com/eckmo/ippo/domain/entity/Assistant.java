package com.eckmo.ippo.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "assistants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assistant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor assignedDoctor;
}
