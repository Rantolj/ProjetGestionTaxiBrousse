package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    // Recherche par nom / prenom du client via la réservation
    List<Paiement> findByReservation_Client_Personne_NomContainingIgnoreCaseOrReservation_Client_Personne_PrenomContainingIgnoreCase(
            String nom, String prenom);

    @Query("SELECT COALESCE(SUM(p.montantPaye), 0) FROM Paiement p WHERE p.reservation.id = :reservationId")
    Double sumMontantByReservationId(@Param("reservationId") Long reservationId);
}