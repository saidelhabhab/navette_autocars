package com.hormattalah.navette_autocars.entity;

import com.hormattalah.navette_autocars.enums.UserRole;
import com.hormattalah.navette_autocars.request.UserDto;
import jakarta.persistence.*;
import lombok.*;



import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "user")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;


    private String firstName;


    private String lastName;

    private String password;
    private UserRole role;

    private String phone;

    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] img;

    @Lob
    private String aboutMe;

    private String country;

    private boolean accountNotExpired;
    private boolean isEnable;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private String providedId;
    private String codeVerification;
    private Date CreatedTime;


}