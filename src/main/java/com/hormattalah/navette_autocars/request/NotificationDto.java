package com.hormattalah.navette_autocars.request;

import lombok.Data;

import java.util.Date;
import java.util.UUID;


@Data
public class NotificationDto {

    private Long id;

    private UUID SecondId;

    private String description;

    private boolean read;

    private Date createdDateTime;


}