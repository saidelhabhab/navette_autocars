package com.hormattalah.navette_autocars.request;

import lombok.Data;

import java.util.List;

@Data
public class CityDto {
    private Long id;
    private String cityName;
    private List<ToCityDto> toCities;
    private Long userId;
    private String firstName;
    private String lastName;

}
