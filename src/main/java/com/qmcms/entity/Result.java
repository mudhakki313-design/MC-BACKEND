package com.qmcms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "results",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"participant_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "participant_id",
            nullable = false,
            unique = true
    )
    private Participant participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "competition_id",
            nullable = false
    )
    private Competition competition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Juzuu juzuu;

    @Column(nullable = false)
    private Double totalScore;

    @Column(nullable = false)
    private Integer rank;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}