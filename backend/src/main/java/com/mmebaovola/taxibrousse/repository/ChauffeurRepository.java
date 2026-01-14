package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.Chauffeur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChauffeurRepository extends JpaRepository<Chauffeur, Long> {
    // Recherche par nom / prenom / contact via la relation personne
    List<Chauffeur> findByPersonne_NomContainingIgnoreCaseOrPersonne_PrenomContainingIgnoreCaseOrPersonne_ContactContainingIgnoreCase(
            String nom, String prenom, String contact);
}