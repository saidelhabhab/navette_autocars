package com.hormattalah.navette_autocars.service.auth;


import com.hormattalah.navette_autocars.request.UserDto;
import com.hormattalah.navette_autocars.request.auth.ChangePasswordDto;
import com.hormattalah.navette_autocars.request.auth.ResetPasswordDto;
import com.hormattalah.navette_autocars.request.auth.SignupRequest;
import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;

public interface AuthService {

    public UserDto createUser(SignupRequest signupRequest);
    public UserDto createAdminAccount();
    public Boolean hasUserWithEmail(String email);

    public void sendVerificationEmail(UserDto user, String siteUrl) throws MessagingException, UnsupportedEncodingException;

    public boolean verify(String verificationCode);

    public ChangePasswordDto changePassword(Long userId, ChangePasswordDto changePassword);

    public void deleteUser(Long userId);

    public void processForgotPassword(UserDto user) throws MessagingException, UnsupportedEncodingException;

    public ResponseEntity<String> resetPassword(ResetPasswordDto request);

}