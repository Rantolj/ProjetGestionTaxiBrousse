package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.Arret;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArretRepository extends JpaRepository<Arret, Long> {
    // Recherche côté DB pour éviter le filtrage en mémoire
    List<Arret> findByNomContainingIgnoreCase(String nom);
}