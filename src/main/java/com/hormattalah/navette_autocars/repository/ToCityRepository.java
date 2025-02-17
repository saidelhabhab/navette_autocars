package com.hormattalah.navette_autocars.repository;

import com.hormattalah.navette_autocars.entity.ToCity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ToCityRepository extends JpaRepository<ToCity, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM ToCity t WHERE t.city.id = :cityId")
    void deleteByCityId(@Param("cityId") Long cityId);
}