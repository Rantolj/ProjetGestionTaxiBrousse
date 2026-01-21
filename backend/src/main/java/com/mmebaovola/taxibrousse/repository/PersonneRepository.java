package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.Personne;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonneRepository extends JpaRepository<Personne, Long> {
    List<Personne> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrContactContainingIgnoreCase(
            String nom, String prenom, String contact);
}
