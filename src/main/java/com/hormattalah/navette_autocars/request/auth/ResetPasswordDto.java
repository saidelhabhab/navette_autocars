package com.hormattalah.navette_autocars.request.auth;

import lombok.Data;

@Data
public class ResetPasswordDto {

    private  String newPassword ;
    private  String token ;
}