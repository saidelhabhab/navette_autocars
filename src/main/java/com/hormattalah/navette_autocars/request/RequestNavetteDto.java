package com.hormattalah.navette_autocars.request;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RequestNavetteDto {

    private Long id;

    private String firstName;
    private String lastName;
    private Long userId;


    private String city;

    private String toCity;

    private String message;

    private LocalDate createdAt;
}
