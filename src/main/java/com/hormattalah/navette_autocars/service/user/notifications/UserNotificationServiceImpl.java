package com.hormattalah.navette_autocars.service.user.notifications;

import com.hormattalah.navette_autocars.entity.Notification;
import com.hormattalah.navette_autocars.entity.User;
import com.hormattalah.navette_autocars.exception.ResourceNotFoundException;
import com.hormattalah.navette_autocars.repository.NotificationRepo;
import com.hormattalah.navette_autocars.repository.UserRepo;
import com.hormattalah.navette_autocars.request.NotificationDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserNotificationServiceImpl implements UserNotificationService {



    private final NotificationRepo notificationRepo;

    private final UserRepo userRepo;

    private final ModelMapper modelMapper;





    @Override
    public List<NotificationDto> getNotifications(Long userId) {
        List<Notification> notifications = notificationRepo.findByUserId(userId);
        return notifications.stream()
                .map(notification -> modelMapper.map(notification, NotificationDto.class)) // Map each entity to DTO
                .collect(Collectors.toList());
    }

    @Override
    public void createNotification(String message) {
        List<User> users = userRepo.findAll();
        for (User user : users) {
            Notification notification = new Notification();
            notification.setDescription(message);
            notification.setCreatedDateTime(new Date());
            notification.setRead(false);
            notification.setUser(user);
            notification.setSecondId(UUID.randomUUID());
            notificationRepo.save(notification);
        }
    }

    @Override
    public void createNotificationPaying(Long userId, String message) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        Notification notification = new Notification();
        notification.setDescription(message);
        notification.setCreatedDateTime(new Date());
        notification.setRead(false);
        notification.setUser(user);
        notification.setSecondId(UUID.randomUUID());
        notificationRepo.save(notification);

    }

    @Override
    public long countUserNotifications(Long userId, boolean read) {
        return notificationRepo.countByUserIdAndRead(userId, read);
    }

    @Override
    public void markAsRead(UUID notificationId) {
        Optional<Notification> optionalNotification = notificationRepo.findBySecondId(notificationId);
        optionalNotification.ifPresent(notification -> {
            notification.setRead(true); // Set read status to true
            notificationRepo.save(notification); // Save the updated notification
        });
    }

    @Override
    public NotificationDto getNotificationBySecondId(UUID id) {

        // Find the notification by its secondary ID
        Optional<Notification> optionalNotification = notificationRepo.findBySecondId(id);

        // Handle the case where the notification is not found
        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();

            // Use ModelMapper to convert the Notification entity to NotificationDto
            return modelMapper.map(notification, NotificationDto.class);
        } else {
            throw new EntityNotFoundException("Notification not found");
        }
    }


    @Override
    public boolean deleteNotification(Long id) {

        Notification notification = notificationRepo.findById(id).orElseThrow();
        if (notification != null){
            notificationRepo.deleteById(id);
            return true;
        }
        return false;


    }
}
