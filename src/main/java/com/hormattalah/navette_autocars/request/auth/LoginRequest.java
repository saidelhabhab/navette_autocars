package com.hormattalah.navette_autocars.request.auth;


import lombok.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest {

    private String username;
    private String password;
}