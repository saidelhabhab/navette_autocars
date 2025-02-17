package com.hormattalah.navette_autocars.controller.admin;

import com.hormattalah.navette_autocars.request.CityDto;
import com.hormattalah.navette_autocars.request.ToCityDto;
import com.hormattalah.navette_autocars.service.admin.city.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/")
@RequiredArgsConstructor
public class CityController {

    private  final CityService cityService;

    @PostMapping("city")
    public ResponseEntity<CityDto> createCity(@RequestBody CityDto cityDto) {
        CityDto city = cityService.saveCity(cityDto);
        return  ResponseEntity.status(HttpStatus.CREATED).body(city);
    }

    @GetMapping("cities2")
    public ResponseEntity<List<CityDto>> getAllCities2(){
        return ResponseEntity.ok(cityService.getAllCities2());
    }
    @GetMapping("cities2/by-user/{userId}")
    public ResponseEntity<List<CityDto>> getAllCitiesByUser2(@PathVariable Long userId){
        return ResponseEntity.ok(cityService.getAllCitiesUserId2(userId));
    }


    @GetMapping("cities")
    public ResponseEntity<Page<CityDto>> getAllCities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Page<CityDto> cityDtos = cityService.getAllCities(page, size);
        return ResponseEntity.ok(cityDtos);
    }

    @GetMapping("cities/by-user/{userId}")
    public ResponseEntity<Page<CityDto>> getCitiesByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CityDto> cityDtos = cityService.getCitiesByUserId(userId, page, size);
        return ResponseEntity.ok(cityDtos);
    }


    @PutMapping("city/update/{id}")
    public ResponseEntity<CityDto> updateCity(@PathVariable Long id, @RequestBody CityDto cityDto) {
        cityDto.setId(id); // Ensure the ID is set in the DTO
        CityDto updatedCategory = cityService.updateCity(id , cityDto);
        return ResponseEntity.ok(updatedCategory);
    }


    @DeleteMapping("city/delete/toCities/{toCityId}")
    public ResponseEntity<Void> deleteToCity(@PathVariable Long toCityId) {
        cityService.deleteToCity(toCityId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("city/delete/{cityId}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long cityId) {
        cityService.deleteCity(cityId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("city/{cityId}/toCity")
    public ResponseEntity<ToCityDto> addToCityForCity(@PathVariable Long cityId, @RequestBody ToCityDto toCity) {
        ToCityDto savedToCity = cityService.addToCityForCity(cityId, toCity);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedToCity);
    }


    @GetMapping("city/count")
    public ResponseEntity<Long> getTotalCityCount() {
        return ResponseEntity.ok(cityService.getTotalCityCount());
    }

    @GetMapping("city/count/society")
    public ResponseEntity<Long> getCityCountForSociety() {
        return ResponseEntity.ok(cityService.getCityCountForSociety());
    }
}
