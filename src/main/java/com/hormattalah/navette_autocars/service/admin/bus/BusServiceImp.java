package com.hormattalah.navette_autocars.service.admin.bus;

import com.hormattalah.navette_autocars.entity.Bus;
import com.hormattalah.navette_autocars.entity.RequestNavette;
import com.hormattalah.navette_autocars.entity.User;
import com.hormattalah.navette_autocars.exception.ResourceNotFoundException;
import com.hormattalah.navette_autocars.repository.BusRepository;
import com.hormattalah.navette_autocars.repository.UserRepo;
import com.hormattalah.navette_autocars.request.BusDto;
import com.hormattalah.navette_autocars.request.RequestNavetteDto;
import com.hormattalah.navette_autocars.request.UserDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class BusServiceImp implements  BusService{

    private final BusRepository busRepository;
    private final UserRepo userRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<BusDto> getAllBuses(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        return busRepository.findAll(pageable)
                .map(bus -> modelMapper.map(bus, BusDto.class));
    }

    @Override
    public List<BusDto> getAllBuses2(){
        return busRepository.findAll()
                .stream().map(bus -> modelMapper.map(bus,BusDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<BusDto> getAllUnusedBuses() {
        return busRepository.findUnusedBuses()
                .stream().map(bus -> modelMapper.map(bus, BusDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<BusDto> getUnusedBusesByUserId(Long userId) {
        List<Bus> buses = busRepository.findUnusedBusesByUserId(userId);
        return buses.stream()
                .map(bus -> modelMapper.map(bus, BusDto.class))
                .collect(Collectors.toList());
    }


    @Override
    public BusDto getBusById(Long id) {
        Bus bus = busRepository.findById(id).orElseThrow(() -> new RuntimeException("Bus not found"));
        return modelMapper.map(bus, BusDto.class);
    }
    /*@Override
    public BusDto createBus(BusDto busDto) {
        Bus bus = modelMapper.map(busDto, Bus.class);
        Bus savedBus = busRepository.save(bus);
        return modelMapper.map(savedBus, BusDto.class);
    }*/

    @Override
    public BusDto createBus(BusDto busDto) throws IOException {
        User user = userRepository.findById(busDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + busDto.getUserId()));


        Bus bus = modelMapper.map(busDto, Bus.class);

        if (busDto.getImg() != null) {
            bus.setImg(busDto.getImg().getBytes());
        }

        bus.setUser(user); // Associe le bus à l'utilisateur

        Bus savedBus = busRepository.save(bus);
        return modelMapper.map(savedBus, BusDto.class);
    }

    @Override
    public BusDto updateBus(Long id, BusDto busDto) throws IOException {
        Bus bus = busRepository.findById(id).orElseThrow(() -> new RuntimeException("Bus not found"));

        bus.setBusName(busDto.getBusName());
        bus.setCapacity(busDto.getCapacity());
        bus.setModel(busDto.getModel());
        bus.setLicensePlate(busDto.getLicensePlate());
        bus.setClimate(busDto.isClimate());


        if (busDto.getImg() != null && !busDto.getImg().isEmpty()) {
            // Convertir l'image base64 en bytes
            byte[] imageBytes = Base64.getDecoder().decode(busDto.getImg().getBytes());
            bus.setImg(imageBytes);
        }
        return modelMapper.map(busRepository.save(bus), BusDto.class);

    }
    @Override
    public void deleteBus(Long id) {
        Bus bus = busRepository.findById(id).orElseThrow(() -> new RuntimeException("Bus not found"));
        busRepository.delete(bus);
    }

    @Override
    public Page<BusDto> getAllBusesByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return busRepository.findByUserId(userId, pageable)
                .map(bus -> modelMapper.map(bus, BusDto.class));
    }

    @Override
    public Long getTotalBusCount() {
        return busRepository.countTotalBuses();
    }
    @Override
    public Long getBusCountForSociety(Long userId) {
        return busRepository.countBusesForSociety(userId);
    }

}
