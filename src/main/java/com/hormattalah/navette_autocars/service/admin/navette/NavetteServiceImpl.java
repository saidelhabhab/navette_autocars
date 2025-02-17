package com.hormattalah.navette_autocars.service.admin.navette;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.hormattalah.navette_autocars.entity.*;
import com.hormattalah.navette_autocars.enums.OwnerType;
import com.hormattalah.navette_autocars.enums.UserRole;
import com.hormattalah.navette_autocars.exception.ResourceNotFoundException;
import com.hormattalah.navette_autocars.repository.*;
import com.hormattalah.navette_autocars.request.BusDto;
import com.hormattalah.navette_autocars.request.NavetteDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class NavetteServiceImpl implements NavetteService {


    private final NavetteRepository navetteRepository;

    private final CityRepository cityRepository;

    private final ToCityRepository toCityRepository;

    private final BusRepository busRepository;

    private final UserRepo userRepo;

    private final ModelMapper modelMapper;

    @Override
    public NavetteDto createNavette(NavetteDto navetteDto) throws IOException {

        // Convert NavetteDto to Navette entity
        Navette navette = new Navette();
        navette.setName(navetteDto.getName());
        navette.setPricePerMouth(navetteDto.getPricePerMouth());
        navette.setPriceFor6Mouth(navetteDto.getPriceFor6Mouth());
        navette.setPriceFor3Mouth(navetteDto.getPriceFor3Mouth());
        navette.setPriceForYear(navetteDto.getPriceForYear());
        navette.setImg(navetteDto.getImg().getBytes());
        navette.setYear(navetteDto.getYear());

        Bus bus = busRepository.findById(navetteDto.getBusId())
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found"));
        navette.setBus(bus);

        User user = userRepo.findById(navetteDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        navette.setUser(user);

        City city = cityRepository.findById(navetteDto.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found"));
        navette.setCity(city);

        ToCity toCity = toCityRepository.findById(navetteDto.getToCityId())
                .orElseThrow(() -> new ResourceNotFoundException("ToCity not found"));
        navette.setToCity(toCity);

        // Déterminer le type de propriétaire
        if (user.getRole().equals(UserRole.ADMIN)) {
            navette.setTypeOwner(OwnerType.ADMIN);
        } else {
            navette.setTypeOwner(OwnerType.SOCIETY);
        }

        Navette savedNavette = navetteRepository.save(navette);

        return modelMapper.map(savedNavette, NavetteDto.class);


    }

    @Override
    public NavetteDto updateNavette(Long id, NavetteDto navetteDto) {
        Navette navette = navetteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Navette not found"));

        navette.setName(navetteDto.getName());

        City city = cityRepository.findById(navetteDto.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found"));
        navette.setCity(city);

        ToCity toCity = toCityRepository.findById(navetteDto.getToCityId())
                .orElseThrow(() -> new ResourceNotFoundException("ToCity not found"));
        navette.setToCity(toCity);

        navette.setPricePerMouth(navetteDto.getPricePerMouth());
        navette.setPriceFor6Mouth(navetteDto.getPriceFor6Mouth());
        navette.setPriceFor3Mouth(navetteDto.getPriceFor3Mouth());
        navette.setYear(navetteDto.getYear());
        navette.setImg(navetteDto.getByteImg());

        Navette updatedNavette = navetteRepository.save(navette);
        return modelMapper.map(updatedNavette, NavetteDto.class);
    }

    @Override
    public void deleteNavette(Long id) {
        if (!navetteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Navette not found");
        }
        navetteRepository.deleteById(id);
    }

    @Override
    public NavetteDto getNavetteById(Long id) {
        Navette navette = navetteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Navette not found"));
        return modelMapper.map(navette, NavetteDto.class);
    }

    @Override
    public List<NavetteDto> getAllNavettes2() {
        List<Navette> navettes = navetteRepository.findAll();
        return navettes.stream()
                .map(nav -> modelMapper.map(nav, NavetteDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<NavetteDto> getAllNavettes(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        return navetteRepository.findAll(pageable)
                .map(nav -> modelMapper.map(nav, NavetteDto.class));

    }

    @Override
    public List<NavetteDto> getAllNavettesByUser2() {
        List<Navette> navettes = navetteRepository.findAll();
        return navettes.stream()
                .map(nav -> modelMapper.map(nav, NavetteDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<NavetteDto> getAllNavettesByUser(Long userId ,int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        return navetteRepository.findByUserId(userId,pageable)
                .map(nav -> modelMapper.map(nav, NavetteDto.class));

    }

    // Obtenir le nombre de navettes par utilisateur et type de propriétaire
    @Override
    public Long getNavetteCountByUserAndType(Long userId) {
        return navetteRepository.countByUserIdAndOwnerType(userId, OwnerType.SOCIETY);
    }

    // Obtenir le nombre total de navettes (Admin)
    @Override
    public Long getTotalNavetteCount() {
        return navetteRepository.countAllNavettes();
    }
}
