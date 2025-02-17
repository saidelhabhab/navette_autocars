package com.hormattalah.navette_autocars.service.admin.bus;

import com.hormattalah.navette_autocars.request.BusDto;
import com.hormattalah.navette_autocars.request.RequestNavetteDto;
import org.springframework.data.domain.Page;


import java.io.IOException;
import java.util.List;


public interface BusService {

    public Page<BusDto> getAllBuses(int page, int size);

    public List<BusDto> getAllBuses2();

    public List<BusDto> getAllUnusedBuses();

    public List<BusDto> getUnusedBusesByUserId(Long userId);

    public BusDto getBusById(Long id);

    public BusDto createBus(BusDto busDto) throws IOException;

    public BusDto updateBus(Long id, BusDto busDto) throws IOException;

    public void deleteBus(Long id) ;

    public Page<BusDto> getAllBusesByUserId(Long userId,int page, int size);

    public Long getTotalBusCount();

    public Long getBusCountForSociety() ;

}