package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrajetRepository extends JpaRepository<Trajet, Long> {
    List<Trajet> findByNomContainingIgnoreCaseOrDistance(String nom, Double distance);
}