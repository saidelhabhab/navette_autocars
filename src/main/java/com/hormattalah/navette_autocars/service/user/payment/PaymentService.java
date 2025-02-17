package com.hormattalah.navette_autocars.service.user.payment;

import com.stripe.model.Charge;

public interface PaymentService {

    public Charge chargeNewCard(String token, int amount) throws Exception;
}
