package com.hormattalah.navette_autocars.controller.admin;

import java.io.IOException;
import java.util.List;

import com.hormattalah.navette_autocars.request.BusDto;
import com.hormattalah.navette_autocars.request.CityDto;
import com.hormattalah.navette_autocars.request.NavetteDto;
import com.hormattalah.navette_autocars.service.admin.navette.NavetteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/")
@RequiredArgsConstructor
public class NavetteController {


    final private NavetteService navetteService;

    @PostMapping("navette")
    public ResponseEntity<NavetteDto> createNavette(@ModelAttribute NavetteDto navetteDto) throws IOException {
        NavetteDto created = navetteService.createNavette(navetteDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("navette/{id}")
    public ResponseEntity<NavetteDto> updateNavette(@PathVariable Long id, @ModelAttribute NavetteDto navetteDto) {
        NavetteDto updated = navetteService.updateNavette(id, navetteDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("navette/{id}")
    public ResponseEntity<Void> deleteNavette(@PathVariable Long id) {
        navetteService.deleteNavette(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("navette/{id}")
    public ResponseEntity<NavetteDto> getNavette(@PathVariable Long id) {
        NavetteDto navette = navetteService.getNavetteById(id);
        return ResponseEntity.ok(navette);
    }

    @GetMapping("navette2")
    public ResponseEntity<List<NavetteDto>> getAllNavettes2() {
        List<NavetteDto> navettes = navetteService.getAllNavettes2();
        return ResponseEntity.ok(navettes);
    }

    @GetMapping("navette")
    public ResponseEntity<Page<NavetteDto>> getAllNavettes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<NavetteDto> navettes = navetteService.getAllNavettes(page, size);
        return ResponseEntity.ok(navettes);
    }

    @GetMapping("navette/by-user/{userId}")
    public ResponseEntity<Page<NavetteDto>> getNavettesByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<NavetteDto> navetteDtos = navetteService.getAllNavettesByUser(userId, page, size);
        return ResponseEntity.ok(navetteDtos);
    }


    // Compter les navettes par utilisateur et type de propriétaire
    @GetMapping("navette/count/user/{userId}")
    public ResponseEntity<Long> countNavettesByUserAndType(@PathVariable Long userId) {
        Long count = navetteService.getNavetteCountByUserAndType(userId);
        return ResponseEntity.ok(count);
    }

    // Compter toutes les navettes (pour l'admin)
    @GetMapping("navette/count/all")
    public ResponseEntity<Long> countAllNavettes() {
        Long count = navetteService.getTotalNavetteCount();
        return ResponseEntity.ok(count);
    }
}
