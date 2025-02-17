package com.hormattalah.navette_autocars.service.admin.navette;

import com.hormattalah.navette_autocars.enums.OwnerType;
import com.hormattalah.navette_autocars.request.NavetteDto;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface NavetteService {

    NavetteDto createNavette(NavetteDto navetteDto) throws IOException;
    NavetteDto updateNavette(Long id, NavetteDto navetteDto);
    void deleteNavette(Long id);
    NavetteDto getNavetteById(Long id);
    List<NavetteDto> getAllNavettes2();

    public Page<NavetteDto> getAllNavettes(int page, int size);

    List<NavetteDto> getAllNavettesByUser2();

    public Page<NavetteDto> getAllNavettesByUser(Long userId ,int page, int size);

    public Long getNavetteCountByUserAndType(Long userId);

    public Long getTotalNavetteCount();
}

