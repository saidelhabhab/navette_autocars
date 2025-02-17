package com.hormattalah.navette_autocars.repository;

import com.hormattalah.navette_autocars.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdAndRead(Long userId, boolean read);

    Optional<Notification> findBySecondId(UUID secondId);


    List<Notification> findByUserId(Long userId);
    long countByUserIdAndRead(Long userId, boolean read);

}