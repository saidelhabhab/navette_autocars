package com.hormattalah.navette_autocars.controller.user;



import com.hormattalah.navette_autocars.request.NotificationDto;
import com.hormattalah.navette_autocars.service.user.notifications.UserNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/user/")
@RequiredArgsConstructor
public class UserNotificationController {



    private final UserNotificationService notificationService;

    @GetMapping("notifications/{userId}")
    public ResponseEntity<List<NotificationDto>> getNotifications(@PathVariable Long userId) {

            return ResponseEntity.ok(notificationService.getNotifications(userId));

    }

    @GetMapping("notifications/count/{userId}")
    public ResponseEntity<Long> getNotificationCount(@PathVariable Long userId, @RequestParam(required = false, defaultValue = "false") boolean read) {
        long count = notificationService.countUserNotifications(userId, read);
        return ResponseEntity.ok(count);
    }


    @PostMapping("notifications/{id}/mark-read")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable UUID id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("notifications/by/{id}")
    public ResponseEntity<NotificationDto> getNotificationById(@PathVariable UUID id) {
        NotificationDto notification = notificationService.getNotificationBySecondId(id);
        if (notification != null) {
            return ResponseEntity.ok(notification);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("notification/{id}")
    public ResponseEntity<Void> deleteNotificationById(@PathVariable Long id) {
        boolean isDeleted = notificationService.deleteNotification(id);
        if (isDeleted) {
            return ResponseEntity.ok().build(); // Return HTTP 200 OK with no body
        } else {
            return ResponseEntity.notFound().build(); // Return HTTP 404 Not Found
        }
    }


}
