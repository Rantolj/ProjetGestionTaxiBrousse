package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.VenteProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface VenteProduitRepository extends JpaRepository<VenteProduit, Long> {

    List<VenteProduit> findByDateVenteBetweenOrderByDateVenteDesc(LocalDate from, LocalDate to);

    List<VenteProduit> findByDateVenteOrderByIdDesc(LocalDate date);

    /**
     * CA total des ventes de produits pour une période
     */
    @Query("SELECT COALESCE(SUM(d.prixUnitaire * d.quantite), 0) FROM VenteProduitDetail d " +
           "WHERE d.vente.dateVente BETWEEN :from AND :to")
    BigDecimal findTotalCAByPeriode(@Param("from") LocalDate from, @Param("to") LocalDate to);

    /**
     * CA des ventes de produits par mois
     */
    @Query(value = "SELECT EXTRACT(YEAR FROM v.date_vente) as annee, " +
           "EXTRACT(MONTH FROM v.date_vente) as mois, " +
           "COALESCE(SUM(d.prix_unitaire * d.quantite), 0) as total " +
           "FROM ventes_produits v " +
           "JOIN vente_produit_details d ON d.vente_id = v.id " +
           "WHERE v.date_vente BETWEEN :from AND :to " +
           "GROUP BY EXTRACT(YEAR FROM v.date_vente), EXTRACT(MONTH FROM v.date_vente) " +
           "ORDER BY annee, mois", nativeQuery = true)
    List<Object[]> findCAByMois(@Param("from") LocalDate from, @Param("to") LocalDate to);

    /**
     * Vue CA par produit pour une période
     */
    interface ProduitCAView {
        Long getProduitId();
        String getLibelle();
        Long getQuantiteTotale();
        BigDecimal getCaTotal();
    }

    @Query("SELECT d.produit.id as produitId, d.produit.libelle as libelle, " +
           "SUM(d.quantite) as quantiteTotale, SUM(d.prixUnitaire * d.quantite) as caTotal " +
           "FROM VenteProduitDetail d " +
           "WHERE d.vente.dateVente BETWEEN :from AND :to " +
           "GROUP BY d.produit.id, d.produit.libelle " +
           "ORDER BY caTotal DESC")
    List<ProduitCAView> findCAByProduit(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
