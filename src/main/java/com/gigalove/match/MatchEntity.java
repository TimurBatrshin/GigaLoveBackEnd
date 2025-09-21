package com.gigalove.match;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "matches")
@Getter @Setter
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId1;
    private Long userId2;
    private String status; // PENDING/ACCEPTED/REJECTED
    private Instant createdAt;
    private Instant lastMessageAt;
}


