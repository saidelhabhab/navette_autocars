package com.hormattalah.navette_autocars.repository;

import com.hormattalah.navette_autocars.entity.PasswordResetToken;
import com.hormattalah.navette_autocars.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(User user);
}