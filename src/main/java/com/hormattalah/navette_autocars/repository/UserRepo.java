package com.hormattalah.navette_autocars.repository;

import com.hormattalah.navette_autocars.entity.User;
import com.hormattalah.navette_autocars.enums.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {


    Optional<User> findByEmail(String email);

    User findByRole(UserRole userRole);

    Optional<User> findByEmailAndProvidedId(String username,String providerId);

    @Query("UPDATE User u SET u.isEnable = true WHERE u.id = ?1")
    @Modifying
    @Transactional
    public  void enable(Long id);

    @Query("SELECT u FROM User u WHERE u.codeVerification = ?1")
    public User findByCodeVerification(String code);


    long countByRole(UserRole role);




}