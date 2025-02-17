package com.hormattalah.navette_autocars.service.user.subcription;


import com.hormattalah.navette_autocars.entity.Bus;
import com.hormattalah.navette_autocars.entity.Navette;
import com.hormattalah.navette_autocars.entity.Subscription;
import com.hormattalah.navette_autocars.exception.ResourceNotFoundException;
import com.hormattalah.navette_autocars.repository.BusRepository;
import com.hormattalah.navette_autocars.repository.NavetteRepository;
import com.hormattalah.navette_autocars.repository.SubscriptionRepository;
import com.hormattalah.navette_autocars.request.SubscriptionDto;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {


    private final SubscriptionRepository subscriptionRepository;

    private final NavetteRepository navetteRepository;

    private final BusRepository busRepository;



    private final ModelMapper modelMapper;

    @Override
    public SubscriptionDto createSubscription(SubscriptionDto subscriptionDto) throws StripeException {
        // Fetch the associated Navette
        Navette navette = navetteRepository.findById(subscriptionDto.getNavetteId())
                .orElseThrow(() -> new RuntimeException("Navette not found"));

        // Fetch the associated Bus
        Bus bus = navette.getBus();
        if (bus == null) {
            throw new RuntimeException("No bus assigned to this navette");
        }

        // Check if the bus has available capacity
        if (bus.getCapacity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No available seats on this bus");
        }

        // Map the DTO to an entity
        Subscription subscription = modelMapper.map(subscriptionDto, Subscription.class);

        // Save the entity
        Subscription saved = subscriptionRepository.save(subscription);

        // Reduce bus capacity
        bus.setCapacity(bus.getCapacity() - 1);
        busRepository.save(bus);

        // Map back to DTO
        SubscriptionDto savedDto = modelMapper.map(saved, SubscriptionDto.class);

        // Create payment intent
        PaymentIntent paymentIntent = createPaymentIntent(subscriptionDto);

        // Optionally, set additional properties from associated entities:
        if (saved.getUser() != null) {
            savedDto.setUserId(saved.getUser().getId());
        }
        if (saved.getNavette() != null) {
            savedDto.setNavetteId(saved.getNavette().getId());
        }

        return savedDto;
    }


    private PaymentIntent createPaymentIntent(SubscriptionDto subscriptionDto) throws StripeException {
        PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder()
                .setAmount((long) (subscriptionDto.getPrice() * 100))
                .setCurrency(subscriptionDto.getCurrency())
                .setPaymentMethod(subscriptionDto.getPaymentMethodId())
                .setConfirm(true)
                .build();

        return PaymentIntent.create(createParams);
    }

    @Override
    public SubscriptionDto updateSubscription(Long id, SubscriptionDto subscriptionDto) {
        Subscription existing = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id: " + id));

        // Get the previous Navette and Bus
        Navette previousNavette = existing.getNavette();
        Bus previousBus = (previousNavette != null) ? previousNavette.getBus() : null;

        // Get the new Navette
        Navette newNavette = navetteRepository.findById(subscriptionDto.getNavetteId())
                .orElseThrow(() -> new ResourceNotFoundException("Navette not found with id: " + subscriptionDto.getNavetteId()));

        Bus newBus = newNavette.getBus();
        if (newBus.getCapacity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No available seats on this bus");
        }

        // If the navette (and bus) is changing, adjust capacity
        if (!newNavette.getId().equals(previousNavette.getId())) {
            // Restore capacity to the previous bus
            if (previousBus != null) {
                previousBus.setCapacity(previousBus.getCapacity() + 1);
                busRepository.save(previousBus);
            }

            // Check if the new bus has available capacity
            if (newBus.getCapacity() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No available seats on this bus");
            }

            // Decrease capacity for the new bus
            newBus.setCapacity(newBus.getCapacity() - 1);
            busRepository.save(newBus);
        }

        // Update the subscription fields
        existing.setDateStart(subscriptionDto.getDateStart());
        existing.setDateEnd(subscriptionDto.getDateEnd());
        existing.setSubscriptionDate(subscriptionDto.getSubscriptionDate());
        existing.setNavette(newNavette); // Assign the new navette if changed

        // Save the updated subscription
        Subscription updated = subscriptionRepository.save(existing);
        return modelMapper.map(updated, SubscriptionDto.class);
    }

    @Override
    public void deleteSubscription(Long id) {
        Subscription existing = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id: " + id));
        subscriptionRepository.delete(existing);
    }

    @Override
    public SubscriptionDto getSubscriptionById(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id: " + id));
        return modelMapper.map(subscription, SubscriptionDto.class);
    }

    @Override
    public List<SubscriptionDto> getSubscriptionsByUserId(Long userId) {
        List<Subscription> subscriptions = subscriptionRepository.findByUserId(userId);
        return subscriptions.stream()
                .map(subscription -> modelMapper.map(subscription, SubscriptionDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<SubscriptionDto> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        return subscriptions.stream()
                .map(subscription -> modelMapper.map(subscription, SubscriptionDto.class))
                .collect(Collectors.toList());
    }


    @Override
    public boolean isUserSubscribed(Long userId, Long navetteId) {
        return subscriptionRepository.existsByUserIdAndNavetteId(userId, navetteId);
    }


    @Override
    public long countSubscriptionsByUserId(Long userId) {
        return subscriptionRepository.countByUserId(userId);
    }

    @Override
    public Long getTotalPriceForAdmin() {
        return subscriptionRepository.getTotalPriceForAdmin();
    }

    @Override
    public Long getTotalPrice() {
        return subscriptionRepository.getTotalPrice();
    }

    @Override
    public Long getTotalPriceForSociety() {
        return subscriptionRepository.getTotalPriceForSociety();
    }

    @Override
    public long getTotalSubscriptionsByAdmin() {
        return subscriptionRepository.countByAdmin();
    }

    @Override
    public long getTotalSubscriptionsBySociety() {
        return subscriptionRepository.countBySociety();
    }

}
