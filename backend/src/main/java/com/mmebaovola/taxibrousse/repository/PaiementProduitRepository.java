package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.PaiementProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaiementProduitRepository extends JpaRepository<PaiementProduit, Long> {

    List<PaiementProduit> findByVenteId(Long venteId);

    /**
     * Total des paiements pour une vente
     */
    @Query("SELECT COALESCE(SUM(p.montantPaye), 0) FROM PaiementProduit p WHERE p.vente.id = :venteId")
    BigDecimal findTotalPayeByVenteId(@Param("venteId") Long venteId);

    /**
     * Total des paiements pour une période
     */
    @Query("SELECT COALESCE(SUM(p.montantPaye), 0) FROM PaiementProduit p " +
           "WHERE p.datePaiement BETWEEN :from AND :to")
    BigDecimal findTotalPayeByPeriode(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
