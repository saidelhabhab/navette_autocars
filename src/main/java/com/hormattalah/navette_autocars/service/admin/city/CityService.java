package com.hormattalah.navette_autocars.service.admin.city;

import com.hormattalah.navette_autocars.request.CityDto;
import com.hormattalah.navette_autocars.request.ToCityDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CityService {

    public Page<CityDto> getAllCities(int page, int size) ;

    public List<CityDto> getAllCities2() ;

    public List<CityDto> getAllCitiesUserId2(Long userId) ;

    public CityDto getCityById(Long id);

    public CityDto saveCity(CityDto city) ;


    public CityDto updateCity(Long id,  CityDto city);

    public void deleteCity(Long cityId);
    public void deleteToCity(Long toCityId);

    public ToCityDto addToCityForCity(Long cityId, ToCityDto toCityDto);

    Page<CityDto> getCitiesByUserId(Long userId, int page, int size);


    public Long getTotalCityCount() ;

    public Long getCityCountForSociety();
}
