package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    // Recherche par nom / prenom du client via la réservation
    List<Paiement> findByReservation_Client_Personne_NomContainingIgnoreCaseOrReservation_Client_Personne_PrenomContainingIgnoreCase(
            String nom, String prenom);
}