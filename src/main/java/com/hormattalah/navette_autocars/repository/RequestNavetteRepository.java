package com.hormattalah.navette_autocars.repository;

import com.hormattalah.navette_autocars.entity.RequestNavette;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestNavetteRepository extends JpaRepository<RequestNavette, Long> {

    List<RequestNavette> findByUserId(Long userId);

    long countByUserId(Long userId);
    long count();
}