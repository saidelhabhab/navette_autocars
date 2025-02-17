package com.hormattalah.navette_autocars.request.auth;


import lombok.*;

import javax.validation.constraints.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignupRequest {


    @Email
    @Size(min = 10, max = 50)
    private String email;

    @Size(min = 6, max = 50, message = "Password length must be between 10 and 50 characters")
    @Pattern.List({
            @Pattern(regexp = "^(?=.*[0-9]).{6,50}$", message = "Password must contain at least one digit"),
            @Pattern(regexp = "^(?=.*[a-z]).{6,50}$", message = "Password must contain at least one lowercase letter"),
            @Pattern(regexp = "^(?=.*[A-Z]).{6,50}$", message = "Password must contain at least one uppercase letter")
    })
    private String password;

    @Size(min = 6, max = 50)
    private String firstName;

    @Size(min = 6, max = 50)
    private String lastName;

    private String phone;
    private String role;



}