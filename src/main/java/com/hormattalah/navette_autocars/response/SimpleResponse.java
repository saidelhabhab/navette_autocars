package com.hormattalah.navette_autocars.response;

import lombok.Data;

@Data
public class SimpleResponse {

    private String message;

    public SimpleResponse(String message) {
        this.message = message;
    }
}