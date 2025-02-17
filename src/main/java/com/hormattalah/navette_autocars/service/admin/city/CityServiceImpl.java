package com.hormattalah.navette_autocars.service.admin.city;

import com.hormattalah.navette_autocars.entity.Bus;
import com.hormattalah.navette_autocars.entity.City;
import com.hormattalah.navette_autocars.entity.ToCity;
import com.hormattalah.navette_autocars.entity.User;
import com.hormattalah.navette_autocars.exception.ResourceNotFoundException;
import com.hormattalah.navette_autocars.repository.CityRepository;
import com.hormattalah.navette_autocars.repository.ToCityRepository;
import com.hormattalah.navette_autocars.repository.UserRepo;
import com.hormattalah.navette_autocars.request.BusDto;
import com.hormattalah.navette_autocars.request.CityDto;
import com.hormattalah.navette_autocars.request.ToCityDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private  final CityRepository cityRepository;
    private  final ToCityRepository toCityRepository;
    private final UserRepo userRepository;
    private final ModelMapper modelMapper;


    @Override
    public Page<CityDto> getAllCities(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cityRepository.findAll(pageable)
                .map(city -> modelMapper.map(city, CityDto.class));
    }

    @Override
    public List<CityDto> getAllCities2() {
        return cityRepository.findAll()
                .stream()
                .map(city -> modelMapper.map(city, CityDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CityDto> getAllCitiesUserId2(Long userId) {
        return cityRepository.findByUserId(userId)
                .stream()
                .map(city -> modelMapper.map(city, CityDto.class))
                .collect(Collectors.toList());
    }


    @Override
    public Page<CityDto> getCitiesByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cityRepository.findByUserId(userId, pageable)
                .map(city -> modelMapper.map(city, CityDto.class));
    }



    @Override
    public CityDto getCityById(Long id) {
        return null;
    }

    @Override
    public CityDto saveCity(CityDto cityDto) {

        User user = userRepository.findById(cityDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + cityDto.getUserId()));


        City city = new City();
        city.setUser(user);
        city.setCityName(cityDto.getCityName());

        // Sauvegarder la ville avant d'ajouter les toCities
        City savedCity = cityRepository.save(city);

        List<ToCity> toCities = cityDto.getToCities().stream()
                .map(tocitiesMap -> {
                    ToCity toCity = new ToCity();
                    toCity.setToCityName(tocitiesMap.getToCityName());
                    toCity.setCity(savedCity);
                    return toCity;
                })
                .collect(Collectors.toList());

        // Sauvegarder les ToCity
        toCityRepository.saveAll(toCities);

        savedCity.setToCities(toCities);

        return modelMapper.map(savedCity, CityDto.class);
    }
    @Override
    @Transactional
    public CityDto updateCity(Long id, CityDto cityDto) {
        // Récupérer la ville existante
        City existingCity = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));

        // Mettre à jour le nom de la ville
        existingCity.setCityName(cityDto.getCityName());

        // Préparer la liste des nouveaux ToCity à partir du DTO
        List<ToCity> newToCities = cityDto.getToCities().stream()
                .map(toCityDto -> {
                    ToCity toCity = new ToCity();
                    toCity.setToCityName(toCityDto.getToCityName());
                    // L'association bidirectionnelle : le ToCity est lié à la ville existante
                    toCity.setCity(existingCity);
                    return toCity;
                })
                .collect(Collectors.toList());

        // Modifier la collection existante "in place" :
        // 1. Vider la collection existante (les orphelins seront supprimés grâce à orphanRemoval)
        existingCity.getToCities().clear();
        // 2. Ajouter les nouveaux éléments dans la même instance de collection
        existingCity.getToCities().addAll(newToCities);

        // Sauvegarder la ville mise à jour (cascade = ALL prendra en compte la collection)
        City updatedCity = cityRepository.save(existingCity);

        return modelMapper.map(updatedCity, CityDto.class);
    }




    @Override
    public void deleteCity(Long cityId) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException("city not found with id " + cityId));

        // Delete all subcategories of this category
        toCityRepository.deleteAll(city.getToCities());

        // Now delete the category
        cityRepository.delete(city);
    }

    @Override
    public void deleteToCity(Long toCityId) {
        ToCity toCity = toCityRepository.findById(toCityId)
                .orElseThrow(() -> new ResourceNotFoundException("toCity not found with id " + toCityId));
        toCityRepository.delete(toCity);
    }

    @Override
    public ToCityDto addToCityForCity(Long cityId, ToCityDto toCityDto) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException("city not found with id: " + cityId));

        ToCity toCity = new ToCity();
        toCity.setToCityName(toCityDto.getToCityName());
        toCity.setCity(city);

        city.getToCities().add(toCity);
        toCityRepository.save(toCity);

        return  modelMapper.map(toCity, ToCityDto.class);
    }

    @Override
    public Long getTotalCityCount() {
        return cityRepository.countTotalCities();
    }
    @Override
    public Long getCityCountForSociety() {
        return cityRepository.countCitiesForSociety();
    }


}
