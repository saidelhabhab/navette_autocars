package com.hormattalah.navette_autocars.response;

import com.hormattalah.navette_autocars.request.auth.ChangePasswordDto;
import lombok.Data;


@Data
public class ChangePasswordResponse {

    private String message;
    private ChangePasswordDto data;

    public ChangePasswordResponse(String passwordUpdatedSuccessfully, ChangePasswordDto updatedPassword) {
        this.message = passwordUpdatedSuccessfully;
        this.data = updatedPassword;

    }
}