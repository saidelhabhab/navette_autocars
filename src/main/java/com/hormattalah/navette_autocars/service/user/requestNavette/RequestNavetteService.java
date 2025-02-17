package com.hormattalah.navette_autocars.service.user.requestNavette;

import com.hormattalah.navette_autocars.request.RequestNavetteDto;

import java.util.List;

public interface RequestNavetteService {

    public RequestNavetteDto createRequest(RequestNavetteDto request);

    public List<RequestNavetteDto> getAllRequests();

    public List<RequestNavetteDto> getRequestsByUserId(Long userId);

    public long countRequestsByUserId(Long userId);

    public long getTotalNavetteRequests();
}
