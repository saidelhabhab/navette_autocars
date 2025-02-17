package com.hormattalah.navette_autocars.request.auth;

import lombok.Data;

@Data
public class ChangePasswordDto {

    String currentPassword;
    String newPassword;
}
