package com.hormattalah.navette_autocars.controller;


import com.hormattalah.navette_autocars.request.BusDto;
import com.hormattalah.navette_autocars.request.NavetteDto;
import com.hormattalah.navette_autocars.service.admin.bus.BusService;
import com.hormattalah.navette_autocars.service.admin.navette.NavetteService;
import com.hormattalah.navette_autocars.service.user.subcription.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ForAllController {

    private final NavetteService navetteService;

    private final BusService busService;


    private final SubscriptionService subscriptionService;


    @GetMapping("navette2")
    public ResponseEntity<List<NavetteDto>> getAllNavettes2() {
        List<NavetteDto> navettes = navetteService.getAllNavettes2();
        return ResponseEntity.ok(navettes);
    }

    @GetMapping("navette")
    public ResponseEntity<Page<NavetteDto>> getAllNavettes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {

        Page<NavetteDto> navettes = navetteService.getAllNavettes(page, size);
        return ResponseEntity.ok(navettes);
    }

    @GetMapping("buses")
    public ResponseEntity<Page<BusDto>> getAllBuses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<BusDto> buses = busService.getAllBuses(page, size);
        return ResponseEntity.ok(buses);
    }


    @GetMapping("/check")
    public ResponseEntity<?> checkSubscription(@RequestParam Long userId, @RequestParam Long navetteId) {
        boolean isSubscribed = subscriptionService.isUserSubscribed(userId, navetteId);

        return isSubscribed ? ResponseEntity.status(HttpStatus.CONFLICT).body(true)
                : ResponseEntity.ok(false);
    }


    @GetMapping("/morocco/cities")
    public List<String> getCities() {
        return List.of(
                "Casablanca",
                "Rabat",
                "Marrakech",
                "Fes",
                "Tanger",
                "Agadir",
                "Oujda",
                "Tetouan",
                "Beni Mellal",
                "El Jadida",
                "Nador",
                "Safi",
                "Khouribga",
                "Kenitra",
                "Larache",
                "Meknes",
                "Taza",
                "Sidi Kacem",
                "Berkane",
                "Marrakech",
                "Tiznit",
                "Al Hoceima",
                "Ifrane",
                "Dakhla",
                "Errachidia",
                "Taroudant",
                "Essaouira",
                "Khenifra",
                "Boudnib",
                "Sidi Ifni",
                "Tanger",
                "Laayoune",
                "Chichaoua",
                "Ouarzazate",
                "Agadir",
                "Fkih Ben Saleh",
                "Chefchaouen",
                "Tata",
                "Khemisset",
                "Fquih Ben Saleh"
        );
    }
}
