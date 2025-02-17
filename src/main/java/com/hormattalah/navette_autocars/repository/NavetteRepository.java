package com.hormattalah.navette_autocars.repository;

import com.hormattalah.navette_autocars.entity.Navette;
import com.hormattalah.navette_autocars.enums.OwnerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NavetteRepository extends JpaRepository<Navette, Long> {

    Page<Navette> findByUserId(Long userId, Pageable pageable);

    // Compter les navettes d'un utilisateur en fonction du type de propriétaire
    @Query("SELECT COUNT(n) FROM Navette n WHERE n.user.id = :userId AND n.typeOwner = :ownerType")
    Long countByUserIdAndOwnerType(@Param("userId") Long userId, @Param("ownerType") OwnerType ownerType);

    // Compter toutes les navettes (pour l'admin)
    @Query("SELECT COUNT(n) FROM Navette n")
    Long countAllNavettes();

}