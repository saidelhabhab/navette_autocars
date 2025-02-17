package com.hormattalah.navette_autocars.repository;

import com.hormattalah.navette_autocars.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Page<City> findByUserId(Long userId, Pageable pageable);

    List<City> findByUserId(Long userId);  // Nouvelle méthode pour récupérer une liste

    @Query("SELECT COUNT(c) FROM City c")
    Long countTotalCities();

    @Query("SELECT COUNT(c) FROM City c JOIN c.navettes n WHERE n.typeOwner = 'SOCIETY'")
    Long countCitiesForSociety();


}

