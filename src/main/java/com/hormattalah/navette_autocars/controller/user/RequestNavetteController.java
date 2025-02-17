package com.hormattalah.navette_autocars.controller.user;

import com.hormattalah.navette_autocars.request.RequestNavetteDto;
import com.hormattalah.navette_autocars.service.user.requestNavette.RequestNavetteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user/navette-requests")
@RequiredArgsConstructor
public class RequestNavetteController {

    private final RequestNavetteService service;

    @PostMapping
    public ResponseEntity<RequestNavetteDto> createRequest(@RequestBody RequestNavetteDto request) {
        return ResponseEntity.ok(service.createRequest(request));
    }

    @GetMapping
    public ResponseEntity<List<RequestNavetteDto>> getRequests() {
        return ResponseEntity.ok(service.getAllRequests());
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RequestNavetteDto>> getRequestsByUser(@PathVariable Long userId) {
        List<RequestNavetteDto> requests = service.getRequestsByUserId(userId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/count/{userId}")
    public ResponseEntity<Long> getUserNavetteRequestCount(@PathVariable Long userId) {
        long count = service.countRequestsByUserId(userId);
        return ResponseEntity.ok(count);
    }


    @GetMapping("/count/all")
    public ResponseEntity<Long> getTotalNavetteRequests() {
        return ResponseEntity.ok(service.getTotalNavetteRequests());
    }
}

