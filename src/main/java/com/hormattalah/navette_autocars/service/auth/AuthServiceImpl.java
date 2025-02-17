package com.hormattalah.navette_autocars.service.auth;

import com.hormattalah.navette_autocars.entity.PasswordResetToken;
import com.hormattalah.navette_autocars.entity.User;
import com.hormattalah.navette_autocars.enums.Provider;
import com.hormattalah.navette_autocars.enums.UserRole;
import com.hormattalah.navette_autocars.exception.ResourceNotFoundException;
import com.hormattalah.navette_autocars.exception.ValidationException;
import com.hormattalah.navette_autocars.repository.PasswordResetTokenRepo;
import com.hormattalah.navette_autocars.repository.UserRepo;
import com.hormattalah.navette_autocars.request.UserDto;
import com.hormattalah.navette_autocars.request.auth.ChangePasswordDto;
import com.hormattalah.navette_autocars.request.auth.ResetPasswordDto;
import com.hormattalah.navette_autocars.request.auth.SignupRequest;
import com.hormattalah.navette_autocars.service.EmailService;
import jakarta.annotation.PostConstruct;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{



    private final PasswordEncoder passwordEncoder;

    private final UserRepo userRepo;

    private final JavaMailSender mailSender;

    private final EmailService mailService;

    private final PasswordResetTokenRepo tokenRepository;

    @Value("${admin.password}")
    private String adminPassword;




    private final EntityManager entityManager;



    @Override
    @Transactional
    public UserDto createUser(SignupRequest signupRequest){

        User user = new User();

        user.setEmail(signupRequest.getEmail());
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setPhone(signupRequest.getPhone());

        // Définir le rôle selon l'entrée utilisateur
        if ("USER".equalsIgnoreCase(signupRequest.getRole())) {
            user.setRole(UserRole.USER);
        } else if ("SOCIETY".equalsIgnoreCase(signupRequest.getRole())) {
            user.setRole(UserRole.SOCIETY);
        } else {
            throw new IllegalArgumentException("Invalid role selected");
        }

        user.setEnable(true);
        user.setAccountNotExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setProvidedId(Provider.LOCAL.name());
        user.setCreatedTime(new Date());

        String randomCode = RandomString.make(64);
        user.setCodeVerification(randomCode);

        User createUser= userRepo.save(user);



        UserDto userDto = new UserDto();
        userDto.setId(createUser.getId());
        userDto.setCodeVerification(createUser.getCodeVerification());
        userDto.setEmail(createUser.getEmail());
        userDto.setLastName(createUser.getLastName());

        return userDto;

    }

    @Override
    public void sendVerificationEmail(UserDto user, String siteUrl) throws  UnsupportedEncodingException {

        String subject = "Please verify your registration";
        String sendName = "Team Said Dev";
        String mailContent = "<p>Dear " + user.getLastName() +",</p>";
        mailContent +="<p>Please click the link below to verify to your registration:</p>";


        String verifyUrl = siteUrl +"/verify?code="+user.getCodeVerification();

        mailContent += "<h3><a href=\""+ verifyUrl +"\">VERIFY</a> </h3>";
        mailContent+="<p>Thank you <br> The Team SAID DEV </p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);



        mailSender.send(message);

        System.out.println("Mail sent successfully........");


    }

    @Override
    @PostConstruct
    public UserDto createAdminAccount() {
        User admin = userRepo.findByRole(UserRole.ADMIN);
        if (admin== null){

            User user = new User();
            user.setFirstName("admin");
            user.setLastName("admin");
            user.setEmail("admin@admin.ma");
            user.setPassword(passwordEncoder.encode(adminPassword));
            user.setRole(UserRole.ADMIN);
            user.setEnable(true);
            user.setAccountNotExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setProvidedId(Provider.LOCAL.name());

            userRepo.save(user);
        }

        return null;
    }

    @Override
    public Boolean hasUserWithEmail(String email) {
        return userRepo.findByEmail(email).isPresent();
    }

    @Override
    public boolean verify(String verificationCode){
        User user = userRepo.findByCodeVerification(verificationCode);

        if (user.isEnable() || user == null){
            return false;
        }
        else {
            userRepo.enable(user.getId());
            return true;
        }
    }

    @Override
    public ChangePasswordDto changePassword(Long userId, ChangePasswordDto changePassword) {
        Optional<User> optionalUser = userRepo.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();


            if (passwordEncoder.matches(changePassword.getCurrentPassword(), user.getPassword())) {
                // Hash the new password before saving it to the database
                user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
                user =  userRepo.save(user);

                // Return the updated ChangePasswordDto
                return changePassword;
            } else {
                throw new ValidationException("Invalid current password");
            }
        } else {
            throw new ResourceNotFoundException("User not found");
        }
    }

    @Override
    public void deleteUser(Long userId) {
        userRepo.deleteById(userId);
    }

    @Override
    public void processForgotPassword(UserDto user) throws  UnsupportedEncodingException {
        Optional<User> optionalUser = userRepo.findByEmail(user.getEmail());


        User user1 = optionalUser.get();

        PasswordResetToken resetToken = tokenRepository.findByUser(user1);

        // Check if a token exists  // Check if it has expired
        if (resetToken == null) {
            // Generate a new token if none exists or if the existing one has expired
            String token = UUID.randomUUID().toString();
            resetToken = new PasswordResetToken();
            resetToken.setToken(token);
            resetToken.setUser(user1);
            resetToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // 24 hours expiration
            tokenRepository.save(resetToken);
        }

        if ( resetToken.getToken() == null && resetToken.getExpiryDate() == null ) {
            // Generate a new token if none exists
            String saveToken = UUID.randomUUID().toString();

            resetToken.setToken(saveToken);
            resetToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // 24 hours expiration
            tokenRepository.save(resetToken);
        }

        if ( resetToken.getExpiryDate().isBefore(LocalDateTime.now()) ) {

            resetToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // 24 hours expiration
            tokenRepository.save(resetToken);
        }

        // Use the token after it's generated or retrieved
        String token = resetToken.getToken();


        String subject = "Password Reset Request";
        String sendName = "Team Said from e-learning";
        String resetLink = "http://localhost:4200/reset-password?token=" + token;
        String mailContent = "<p>Dear " + user1.getLastName() + " " + user1.getFirstName() + ",<p>"
                + "<p>You have requested a password reset.<p>"
                + "<p>Please click on the following link to reset your password: <p>"
                + "<p>" + resetLink + "<p>"
                + "<p>If you did not request this, please ignore this email.<p>"
                + "<p>Thank you. <p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);


        mailSender.send(message);

        System.out.println("Mail sent successfully........");
    }

    @Override
    public ResponseEntity<String> resetPassword( ResetPasswordDto request){

        PasswordResetToken passwordResetTokenOptional = tokenRepository.findByToken(request.getToken());



        User user = passwordResetTokenOptional.getUser();


        // Set the new password for the user
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // Clear the password reset token
        passwordResetTokenOptional.setToken(null);
        passwordResetTokenOptional.setExpiryDate(null);

        // Save the updated token
        tokenRepository.save(passwordResetTokenOptional);

        // Save the updated password user
        userRepo.save(user);

        return ResponseEntity.ok("Password reset successfully");


    }





}