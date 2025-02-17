package com.hormattalah.navette_autocars.service.admin.users;

import com.hormattalah.navette_autocars.entity.User;
import com.hormattalah.navette_autocars.enums.UserRole;
import com.hormattalah.navette_autocars.exception.ResourceNotFoundException;
import com.hormattalah.navette_autocars.repository.UserRepo;
import com.hormattalah.navette_autocars.request.UserDto;
import com.hormattalah.navette_autocars.service.user.notifications.UserNotificationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {


    private final UserRepo userRepo;
    private final MailSender mailSender;
    private final ModelMapper modelMapper;

    private  final UserNotificationService userNotificationService;

    @Override
    public Page<UserDto> getAllUser(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepo.findAll(pageable);
        return users.map(user -> modelMapper.map(user, UserDto.class));
    }



    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public long countUsers() {
        return userRepo.countByRole(UserRole.USER);
    }


    @Override
    public UserDto updateUser(Long userId, UserDto userDto) throws IOException {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not fond : " + userId));

        if (userDto.getPhone() != null) user.setPhone(userDto.getPhone());
        if (userDto.getFirstName() != null) user.setFirstName(userDto.getFirstName());
        if (userDto.getLastName() != null) user.setLastName(userDto.getLastName());
        if (userDto.getAboutMe() != null) user.setAboutMe(userDto.getAboutMe());
        if (userDto.getCountry() != null) user.setCountry(userDto.getCountry());

        // Gérer l'image séparément
        if (userDto.getImg() != null) {
            user.setImg(userDto.getImg().getBytes());
        }

        return modelMapper.map(userRepo.save(user), UserDto.class);
    }


    @Override
    public UserDto changeStatus(Long userId, String status) {

        Optional<User> optionalUser = userRepo.findById(userId);


        return null;

    }

    private void sendNewCourseNotification(User user) {

        List<User> users = userRepo.findAll(); // Fetch all users

        for (User user1 : users) {
            // Construct the email message
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user1.getEmail()); // Set recipient email
            message.setSubject("New Instructor Available"); // Set email subject
            message.setText("Dear " + user1.getLastName() +" " + user1.getFirstName() + ",\n\nA new Instructor  '" + user.getLastName() +" "+ user.getFirstName() +
                    "' has been added to our platform. Check it out now!\n\nRegards,\nThe eLearning Team");

            // Send the email
            mailSender.send(message);
        }
    }






}