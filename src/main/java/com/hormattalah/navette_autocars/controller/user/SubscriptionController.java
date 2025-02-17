package com.hormattalah.navette_autocars.controller.user;


import com.hormattalah.navette_autocars.request.SubscriptionDto;
import com.hormattalah.navette_autocars.service.user.subcription.SubscriptionService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {


    private final SubscriptionService subscriptionService;

    // Create a new subscription
    @PostMapping
    public ResponseEntity<SubscriptionDto> createSubscription(@RequestBody SubscriptionDto subscriptionDto)  throws StripeException {
        SubscriptionDto created = subscriptionService.createSubscription(subscriptionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Get a subscription by ID
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDto> getSubscriptionById(@PathVariable Long id) {
        SubscriptionDto subscription = subscriptionService.getSubscriptionById(id);
        return ResponseEntity.ok(subscription);
    }

    // Get all subscriptions
    @GetMapping
    public ResponseEntity<List<SubscriptionDto>> getAllSubscriptions() {
        List<SubscriptionDto> subscriptions = subscriptionService.getAllSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SubscriptionDto>> getSubscriptionsByUserId(@PathVariable Long userId) {
        List<SubscriptionDto> subscriptions = subscriptionService.getSubscriptionsByUserId(userId);
        return ResponseEntity.ok(subscriptions);
    }


    // Update a subscription by ID
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionDto> updateSubscription(@PathVariable Long id, @RequestBody SubscriptionDto subscriptionDto) {
        SubscriptionDto updated = subscriptionService.updateSubscription(id, subscriptionDto);
        return ResponseEntity.ok(updated);
    }



    // Delete a subscription by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/count/{userId}")
    public ResponseEntity<Long> getUserSubscriptionCount(@PathVariable Long userId) {
        long count = subscriptionService.countSubscriptionsByUserId(userId);
        return ResponseEntity.ok(count);
    }



    @GetMapping("/total")
    public ResponseEntity<Long> getTotalPrice() {
        return ResponseEntity.ok(subscriptionService.getTotalPrice());
    }

    @GetMapping("/total/admin")
    public ResponseEntity<Long> getTotalPriceForAdmin() {
        return ResponseEntity.ok(subscriptionService.getTotalPriceForAdmin());
    }


    @GetMapping("/total/society")
    public ResponseEntity<Long> getTotalPriceForSociety() {
        return ResponseEntity.ok(subscriptionService.getTotalPriceForSociety());
    }

    @GetMapping("/admin/count")
    public long getTotalSubscriptionsByAdmin() {
        return subscriptionService.getTotalSubscriptionsByAdmin();
    }

    @GetMapping("/society/count")
    public long getTotalSubscriptionsBySociety() {
        return subscriptionService.getTotalSubscriptionsBySociety();
    }

}
