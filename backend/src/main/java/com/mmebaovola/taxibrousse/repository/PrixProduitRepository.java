package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.PrixProduit;
import com.mmebaovola.taxibrousse.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrixProduitRepository extends JpaRepository<PrixProduit, Long> {

    List<PrixProduit> findByProduitOrderByDateEffectiveDesc(Produit produit);

    /**
     * Récupère le prix le plus récent d'un produit à une date donnée
     */
    @Query("SELECT pp FROM PrixProduit pp WHERE pp.produit.id = :produitId " +
           "AND pp.dateEffective <= :date ORDER BY pp.dateEffective DESC LIMIT 1")
    Optional<PrixProduit> findPrixActuel(@Param("produitId") Long produitId, @Param("date") LocalDate date);

    /**
     * Récupère le prix actuel (dernier prix effectif)
     */
    @Query("SELECT pp.prix FROM PrixProduit pp WHERE pp.produit.id = :produitId " +
           "AND pp.dateEffective <= CURRENT_DATE ORDER BY pp.dateEffective DESC LIMIT 1")
    Optional<BigDecimal> findPrixActuelByProduitId(@Param("produitId") Long produitId);
}
