package com.hormattalah.navette_autocars.controller;

import com.hormattalah.navette_autocars.entity.User;
import com.hormattalah.navette_autocars.exception.ResourceNotFoundException;
import com.hormattalah.navette_autocars.exception.ValidationException;
import com.hormattalah.navette_autocars.repository.UserRepo;
import com.hormattalah.navette_autocars.request.UserDto;
import com.hormattalah.navette_autocars.request.auth.ChangePasswordDto;
import com.hormattalah.navette_autocars.request.auth.LoginRequest;
import com.hormattalah.navette_autocars.request.auth.ResetPasswordDto;
import com.hormattalah.navette_autocars.request.auth.SignupRequest;
import com.hormattalah.navette_autocars.response.ChangePasswordResponse;
import com.hormattalah.navette_autocars.response.ErrorResponse;
import com.hormattalah.navette_autocars.response.SignupResponse;
import com.hormattalah.navette_autocars.response.SimpleResponse;
import com.hormattalah.navette_autocars.service.JwtService;
import com.hormattalah.navette_autocars.service.admin.users.UsersService;
import com.hormattalah.navette_autocars.service.auth.AuthService;
import com.hormattalah.navette_autocars.util.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthenticationController {


    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserRepo userRepo;

    private final JwtUtil jwtUtil;

    private final AuthService authService;

    private final UsersService usersService;

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String WEADER_STRING = "Authorization";

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);


    /// //////////////////////////////////////////////////////////////////////////////////////
    @PostMapping("/authenticate")
    public void createAuthenticationToken(@RequestBody LoginRequest loginRequest, HttpServletResponse response) throws JSONException {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), loginRequest.getPassword()));

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("incorrect username or PASS");
        }

        final UserDetails userDetails = jwtService.loadUserByUsername(loginRequest.getUsername());
        Optional<User> optionalUser = userRepo.findByEmail(userDetails.getUsername());

        if (optionalUser.isPresent() && optionalUser.get().isEnable()) {
            final String jwt = jwtUtil.generateToken(userDetails.getUsername());

            optionalUser.ifPresent(user -> {
                try {
                    String jsonResponse = new JSONObject()
                            .put("userId", user.getId())
                            .put("role", user.getRole())
                            .put("jwt", jwt)
                            .toString();

                    response.getWriter().write(jsonResponse);
                } catch (IOException e) {
                    LOGGER.error("An IOException occurred: {}", e.getMessage(), e);
                }
            });

            response.addHeader("Access-Control-Expose-Headers", WEADER_STRING);
            response.addHeader("Access-Control-Allow-Headers", "Authorization, X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, X-Custom-header");
            response.addHeader(WEADER_STRING, TOKEN_PREFIX + jwt);
        } else {
            throw new ResourceNotFoundException("This account is not enabled. Please check your email to enable your account.");
        }

    }

    /// /////////////////////////////////////////////////////////////////////////////////
    @PostMapping("/sign-up")
    public ResponseEntity<Object> signupUser(@RequestBody SignupRequest signupRequest, HttpServletRequest request)
            throws MessagingException, UnsupportedEncodingException {

        if (authService.hasUserWithEmail(signupRequest.getEmail())) {
            // Return error response with appropriate status code
            ErrorResponse errorResponse = new ErrorResponse("User already exists");
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT); // 409 Conflict for existing resource
        }

        UserDto userDto = authService.createUser(signupRequest);

        // String siteUrl = Utility.getSiteURL(request);
        String siteUrl = "http://localhost:4200";

        // Return success response with user data
        SignupResponse successResponse = new SignupResponse("User successfully created", userDto);
        return new ResponseEntity<>(successResponse, HttpStatus.CREATED); // 201 Created for successful creation
    }


    @GetMapping("api/user/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(usersService.getUserById(userId));
    }

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam("code") String code) {
        boolean verified = authService.verify(code);
        String pageTitle = verified ? "Verification successfully" : "Verification failed";
        //return "register / " + (verified ? "Verification successfully" : "Verification failed");
        return pageTitle;
    }


    @PutMapping("api/user/change-password/{userId}")
    public ResponseEntity<Object> changePassword(@PathVariable Long userId, @RequestBody ChangePasswordDto changePassword) {
        try {
            ChangePasswordDto updatedPassword = authService.changePassword(userId, changePassword);
            ChangePasswordResponse successResponse = new ChangePasswordResponse("Password updated successfully", updatedPassword);
            return ResponseEntity.ok(successResponse);
        } catch (ResourceNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (ValidationException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }


    @DeleteMapping("api/user/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        authService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }


    @PostMapping("/forget-password")
    public ResponseEntity<Object> forgotPassword(@RequestBody UserDto user) {

            SimpleResponse successResponse = new SimpleResponse("Password reset sent to email successfully");
            return ResponseEntity.ok(successResponse);


    }


    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestBody ResetPasswordDto request) {
        authService.resetPassword(request);
        SimpleResponse successResponse = new SimpleResponse("Password reset is modified successfully");
        return ResponseEntity.ok(successResponse);
    }


    @GetMapping("/secure")
    public String login() {
        return "secure";
    }


    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

    @GetMapping("loginByGoggle")
    public Map<String, Object> currentUser(OAuth2AuthenticationToken auth2AuthenticationToken) {
        return auth2AuthenticationToken.getPrincipal().getAttributes();
    }

    @GetMapping("getSiteURL")
    public static String getSiteURL(HttpServletRequest request) {
        String siteUrl = request.getRequestURL().toString();
        String siteURLProject = siteUrl.replace(request.getServletPath(), "");
        return siteURLProject;
    }

    @GetMapping("/order/hello")
    public String hello() {
        return "hello";
    }
}