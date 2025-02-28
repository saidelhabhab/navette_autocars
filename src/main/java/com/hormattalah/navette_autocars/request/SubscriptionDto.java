package com.hormattalah.navette_autocars.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hormattalah.navette_autocars.entity.City;
import com.hormattalah.navette_autocars.entity.ToCity;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SubscriptionDto {
    private Long id;

    private Long navetteId;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private LocalDate subscriptionDate;
    private LocalTime hourDepart;
    private LocalTime hourRouteur;

    private Long userId;
    private String firstName;
    private String lastName;

    private String navetteName;
    private String cityName;
    private String toCityName;


    private Integer monthOption;
    private String preferredHour;

    private byte[] byteImg;
    private MultipartFile img;

    private long price;

    private long idSociety;


    private String  transactionId;
    private String currency;
    private String paymentMethodId;

}
