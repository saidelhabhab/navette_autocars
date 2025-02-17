package com.hormattalah.navette_autocars.service.user.requestNavette;

import com.hormattalah.navette_autocars.config.ModelMapperConfig;
import com.hormattalah.navette_autocars.entity.RequestNavette;
import com.hormattalah.navette_autocars.entity.Subscription;
import com.hormattalah.navette_autocars.repository.RequestNavetteRepository;
import com.hormattalah.navette_autocars.request.RequestNavetteDto;
import com.hormattalah.navette_autocars.request.SubscriptionDto;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestNavetteServiceImpl implements  RequestNavetteService{

    private final RequestNavetteRepository requestNavetteRepository;

    private final ModelMapper modelMapper;

    @Override
    public RequestNavetteDto createRequest(RequestNavetteDto requestNavetteDto) {
        // Map the DTO to an entity
        RequestNavette requestNavette = modelMapper.map(requestNavetteDto, RequestNavette.class);

        // Set the createdAt field to the current date
        requestNavette.setCreatedAt(LocalDate.now()); // Assuming createdAt is of type LocalDate

        // Save the entity
        RequestNavette saved = requestNavetteRepository.save(requestNavette);
        // Map back to DTO
        RequestNavetteDto savedDto = modelMapper.map(saved, RequestNavetteDto.class);

        // Optionally, set additional properties from associated entities:
        if (saved.getUser() != null) {
            savedDto.setUserId(saved.getUser().getId());
        }

        return savedDto;
    }

    @Override
    public List<RequestNavetteDto> getAllRequests() {
        return requestNavetteRepository.findAll().stream()
                .map(request -> modelMapper.map(request, RequestNavetteDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestNavetteDto> getRequestsByUserId(Long userId) {
        List<RequestNavette> requests = requestNavetteRepository.findByUserId(userId);
        return requests.stream()
                .map(request -> modelMapper.map(request, RequestNavetteDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public long countRequestsByUserId(Long userId) {
        return requestNavetteRepository.countByUserId(userId);
    }

    @Override
    public long getTotalNavetteRequests() {
        return requestNavetteRepository.count();
    }
}
