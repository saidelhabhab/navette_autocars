package com.hormattalah.navette_autocars.response;

import com.hormattalah.navette_autocars.request.UserDto;
import lombok.Data;

@Data
public class SignupResponse {

    private String message;
    private UserDto user;


    // Constructor to initialize fields
    public SignupResponse(String message, UserDto user) {
        this.message = message;
        this.user = user;
    }
}