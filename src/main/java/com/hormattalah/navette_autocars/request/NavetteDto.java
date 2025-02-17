package com.hormattalah.navette_autocars.request;


import com.hormattalah.navette_autocars.enums.OwnerType;
import jakarta.persistence.Column;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


import java.math.BigDecimal;
import java.time.LocalTime;

@Data
public class NavetteDto {
    private Long id;

    private String name;

    private Long cityId;
    private Long toCityId;
    private String cityName;
    private String toCityName;


    private BigDecimal pricePerMouth;
    private BigDecimal priceFor6Mouth;
    private BigDecimal priceFor3Mouth;
    private BigDecimal priceForYear;

    private String busName;
    private Long busId;

    private Long userId;
    private String firstName;
    private String lastName;

    private String typeOwner;

    private Integer year;

    private byte[] byteImg;
    private MultipartFile img;
}
