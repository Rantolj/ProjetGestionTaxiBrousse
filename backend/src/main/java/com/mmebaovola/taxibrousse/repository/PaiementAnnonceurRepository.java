package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.PaiementAnnonceur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaiementAnnonceurRepository extends JpaRepository<PaiementAnnonceur, Long> {

    @Query("SELECT COALESCE(SUM(p.montantPaye), 0) FROM PaiementAnnonceur p WHERE p.annonceur.id = :annonceurId AND p.datePaiement >= :from AND p.datePaiement <= :to")
    BigDecimal sumPaymentsByAnnonceurBetween(@Param("annonceurId") Long annonceurId, @Param("from") LocalDate from,
            @Param("to") LocalDate to);

    // Liste des paiements par période
    @Query("SELECT p FROM PaiementAnnonceur p JOIN FETCH p.annonceur WHERE p.datePaiement >= :from AND p.datePaiement <= :to ORDER BY p.datePaiement DESC")
    List<PaiementAnnonceur> findByDatePaiementBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);

    // Liste des paiements par annonceur et période
    @Query("SELECT p FROM PaiementAnnonceur p JOIN FETCH p.annonceur WHERE p.annonceur.id = :annonceurId AND p.datePaiement >= :from AND p.datePaiement <= :to ORDER BY p.datePaiement DESC")
    List<PaiementAnnonceur> findByAnnonceurIdAndDatePaiementBetween(@Param("annonceurId") Long annonceurId,
            @Param("from") LocalDate from, @Param("to") LocalDate to);

    // Liste de tous les paiements d'un annonceur
    @Query("SELECT p FROM PaiementAnnonceur p JOIN FETCH p.annonceur WHERE p.annonceur.id = :annonceurId ORDER BY p.datePaiement DESC")
    List<PaiementAnnonceur> findByAnnonceurId(@Param("annonceurId") Long annonceurId);

}
