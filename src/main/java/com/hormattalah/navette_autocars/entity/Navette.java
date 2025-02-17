package com.hormattalah.navette_autocars.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hormattalah.navette_autocars.enums.OwnerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "navette")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Navette {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "bus_id", nullable = false)
    @JsonIgnore
    private Bus bus;


    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "city_id",nullable = false)
    @JsonIgnore
    private City city;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "to_city_id")
    @JsonIgnore
    private ToCity toCity;

    private BigDecimal pricePerMouth;
    private BigDecimal priceFor6Mouth;
    private BigDecimal priceFor3Mouth;
    private BigDecimal priceForYear;



    private Integer year;

    @Lob
    @Column(name = "img", columnDefinition = "LONGBLOB")
    private byte[] img;


    @Enumerated(EnumType.STRING)
    @Column(name = "type_owner", nullable = false)
    private OwnerType typeOwner;

}