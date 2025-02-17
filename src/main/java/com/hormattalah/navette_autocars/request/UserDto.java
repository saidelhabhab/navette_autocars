package com.hormattalah.navette_autocars.request;


import com.hormattalah.navette_autocars.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private UserRole role;
    private String codeVerification ;
    private String aboutMe;
    private String country;
    private String phone;

    private byte[] byteImg;
    private MultipartFile img;

    private boolean isEnable;
    private boolean accountNonLocked;



}