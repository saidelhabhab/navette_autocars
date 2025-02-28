package com.hormattalah.navette_autocars.controller.admin;

import com.hormattalah.navette_autocars.request.BusDto;
import com.hormattalah.navette_autocars.request.RequestNavetteDto;
import com.hormattalah.navette_autocars.service.admin.bus.BusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/")
@RequiredArgsConstructor
public class BusController {
    private final BusService busService;

    @GetMapping("buses")
    public ResponseEntity<Page<BusDto>> getAllBuses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<BusDto> buses = busService.getAllBuses(page, size);
        return ResponseEntity.ok(buses);
    }

    @GetMapping("buses2")
    public ResponseEntity<List<BusDto>> getAllBuses() {
        List<BusDto> buses = busService.getAllBuses2();
        return ResponseEntity.ok(buses);
    }

    @GetMapping("/buses/unused-buses")
    public ResponseEntity<List<BusDto>> getUnusedBuses() {
        return ResponseEntity.ok(busService.getAllUnusedBuses());
    }

    @GetMapping("buses/unused/by-user/{userId}")
    public ResponseEntity<List<BusDto>> getUnusedBusesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(busService.getUnusedBusesByUserId(userId));
    }

    @GetMapping("buses/{id}")
    public ResponseEntity<BusDto> getBusById(@PathVariable Long id) {
        return ResponseEntity.ok(busService.getBusById(id));
    }

    @PostMapping("buses")
    public ResponseEntity<BusDto> createBus(@ModelAttribute BusDto busDto) throws IOException  {
        return ResponseEntity.ok(busService.createBus(busDto));
    }

    @PutMapping("buses/{id}")
    public ResponseEntity<BusDto> updateBus(@PathVariable Long id, @ModelAttribute BusDto busDto) throws IOException {
        return ResponseEntity.ok(busService.updateBus(id, busDto));
    }

    @DeleteMapping("buses/{id}")
    public ResponseEntity<Void> deleteBus(@PathVariable Long id) {
        busService.deleteBus(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("buses/find-by-user/{userId}")
    public ResponseEntity<Page<BusDto>> getBusesByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<BusDto> buses = busService.getAllBusesByUserId(userId, page, size);
        return ResponseEntity.ok(buses);
    }

    @GetMapping("buses/count")
    public ResponseEntity<Long> getTotalBusCount() {
        return ResponseEntity.ok(busService.getTotalBusCount());
    }

    @GetMapping("buses/count/society/{userId}")
    public ResponseEntity<Long> getBusCountForSociety(@PathVariable  Long userId) {
        return ResponseEntity.ok(busService.getBusCountForSociety(userId));
    }
}
