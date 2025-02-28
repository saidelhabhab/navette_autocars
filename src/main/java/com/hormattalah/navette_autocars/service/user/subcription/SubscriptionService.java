package com.hormattalah.navette_autocars.service.user.subcription;

import com.hormattalah.navette_autocars.request.SubscriptionDto;
import com.stripe.exception.StripeException;

import java.util.List;

public interface SubscriptionService {

    SubscriptionDto createSubscription(SubscriptionDto subscriptionDto) throws StripeException;

    SubscriptionDto updateSubscription(Long id, SubscriptionDto subscriptionDto);

    void deleteSubscription(Long id);

    SubscriptionDto getSubscriptionById(Long id);

    public List<SubscriptionDto> getSubscriptionsByUserId(Long userId);

    List<SubscriptionDto> getAllSubscriptions();

    public boolean isUserSubscribed(Long userId, Long navetteId);

    public long countSubscriptionsByUserId(Long userId);

    public Long getTotalPriceForAdmin();

    public Long getTotalPrice();

    public Long getTotalPriceForSociety(Long idSociety);

    public long getTotalSubscriptionsByAdmin();

    public long getTotalSubscriptionsBySociety(Long idSociety);

}