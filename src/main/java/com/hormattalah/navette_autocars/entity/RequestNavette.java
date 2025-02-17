package com.hormattalah.navette_autocars.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "request_navette")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestNavette {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String toCity;

    @Column(nullable = false)
    private String message;

    private LocalDate createdAt;

}
