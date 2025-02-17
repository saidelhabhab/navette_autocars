package com.hormattalah.navette_autocars.request;



import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
public class BusDto {
    private Long id;
    private String busName;
    private String licensePlate;
    private String model;
    private int capacity;
    private String driverName;

    private byte[] byteImg;
    private MultipartFile img;

    private boolean climate;

    private Long userId;
    private String firstName;
    private String lastName;
}