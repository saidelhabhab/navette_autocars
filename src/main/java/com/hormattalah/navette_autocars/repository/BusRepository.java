package com.hormattalah.navette_autocars.repository;


import com.hormattalah.navette_autocars.entity.Bus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {

    @Query("SELECT b FROM Bus b WHERE b.id NOT IN (SELECT n.bus.id FROM Navette n)")
    List<Bus> findUnusedBuses();

    @Query("SELECT b FROM Bus b WHERE b.id NOT IN (SELECT n.bus.id FROM Navette n) AND b.user.id = :userId")
    List<Bus> findUnusedBusesByUserId(@Param("userId") Long userId);

    Page<Bus> findByUserId(Long userId, Pageable pageable);


    @Query("SELECT COUNT(b) FROM Bus b")
    Long countTotalBuses();

    @Query("SELECT COUNT(b) FROM Bus b JOIN b.navette n WHERE n.typeOwner = 'SOCIETY'")
    Long countBusesForSociety();

}