package com.hormattalah.navette_autocars.repository;

import com.hormattalah.navette_autocars.entity.Subscription;
import com.hormattalah.navette_autocars.enums.OwnerType;
import com.hormattalah.navette_autocars.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    boolean existsByUserIdAndNavetteId(Long userId, Long navetteId);

    List<Subscription> findByUserId(Long userId);

    long countByUserId(Long userId);

    @Query("SELECT SUM(s.price) FROM Subscription s JOIN s.navette n WHERE n.typeOwner = 'ADMIN'")
    Long getTotalPriceForAdmin();

    @Query("SELECT SUM(s.price) FROM Subscription  s")
    Long getTotalPrice();

    @Query("SELECT SUM(s.price) FROM Subscription s JOIN s.navette n WHERE s.idSociety = :idSociety AND   n.typeOwner = 'SOCIETY'")
    Long getTotalPriceForSociety(@Param("idSociety") Long idSociety);


    // Count subscriptions by admin (OwnerType.ADMIN)
    @Query("SELECT COUNT(s) FROM Subscription s WHERE s.navette.typeOwner = 'ADMIN'")
    long countByAdmin();

    // Count subscriptions by society (OwnerType.SOCIETY)
    @Query("SELECT COUNT(s) FROM Subscription s WHERE s.idSociety = :idSociety AND s.navette.typeOwner = 'SOCIETY'")
    long countBySociety(@Param("idSociety") Long idSociety);

}