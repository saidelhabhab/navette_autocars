package com.hormattalah.navette_autocars.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "subscription")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-one relationship with the User entity
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Many-to-one relationship with the Navette entity
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "navette_id", nullable = false)
    private Navette navette;

    @Column(name = "date_start")
    private LocalDate dateStart;

    @Column(name = "date_end")
    private LocalDate dateEnd;

    @Column(name = "hour_depart")
    private LocalTime hourDepart;

    @Column(name = "hour_routeur")
    private LocalTime hourRouteur;

    @Column(name = "subscription_date")
    private LocalDate subscriptionDate;

    @Column(name = "month_option")
    private Integer monthOption;

    private long idSociety;

    private long price;

    private String  transactionId;
    private String currency;
    private String paymentMethodId;


}
