package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.DetailsDiffusion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DetailsDiffusionRepository extends JpaRepository<DetailsDiffusion, Long> {

    // CA pub par voyage
    @Query("SELECT COALESCE(SUM(dd.montantTotal), 0) FROM DetailsDiffusion dd WHERE dd.voyage.id = :voyageId")
    BigDecimal sumMontantByVoyageId(@Param("voyageId") Long voyageId);

    // Liste des diffusions par voyage
    List<DetailsDiffusion> findByVoyageId(Long voyageId);

    // Liste des diffusions par annonceur (via DiffusionPublicitaire)
    @Query("SELECT dd FROM DetailsDiffusion dd " +
            "JOIN dd.diffusionPublicitaire dp " +
            "WHERE dp.annonceur.id = :annonceurId " +
            "ORDER BY dd.dateDiffusion DESC")
    List<DetailsDiffusion> findByAnnonceurId(@Param("annonceurId") Long annonceurId);

    // Liste des diffusions par annonceur dans une période
    @Query("SELECT dd FROM DetailsDiffusion dd " +
            "JOIN dd.diffusionPublicitaire dp " +
            "WHERE dp.annonceur.id = :annonceurId " +
            "AND dd.dateDiffusion >= :from AND dd.dateDiffusion < :to " +
            "ORDER BY dd.dateDiffusion DESC")
    List<DetailsDiffusion> findByAnnonceurIdAndPeriod(
            @Param("annonceurId") Long annonceurId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);

    // Total CA par annonceur (toutes diffusions)
    @Query("SELECT COALESCE(SUM(dd.montantTotal), 0) FROM DetailsDiffusion dd " +
            "JOIN dd.diffusionPublicitaire dp " +
            "WHERE dp.annonceur.id = :annonceurId")
    BigDecimal sumMontantByAnnonceurId(@Param("annonceurId") Long annonceurId);

    // Total CA par annonceur dans une période
    @Query("SELECT COALESCE(SUM(dd.montantTotal), 0) FROM DetailsDiffusion dd " +
            "JOIN dd.diffusionPublicitaire dp " +
            "WHERE dp.annonceur.id = :annonceurId " +
            "AND dd.dateDiffusion >= :from AND dd.dateDiffusion < :to")
    BigDecimal sumMontantByAnnonceurIdAndPeriod(
            @Param("annonceurId") Long annonceurId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);

    // Projection pour CA pub par voyage avec nom annonceur
    interface VoyagePubCAView {
        Long getVoyageId();

        BigDecimal getCaPub();
    }

    // CA pub groupé par voyage pour une période
    @Query(value = """
            SELECT
                dd.voyage_id AS voyageId,
                COALESCE(SUM(dd.montant_total), 0) AS caPub
            FROM details_diffusion dd
            JOIN voyages v ON v.id = dd.voyage_id
            WHERE v.date_depart >= :startDate AND v.date_depart < :endDate
            GROUP BY dd.voyage_id
            """, nativeQuery = true)
    List<VoyagePubCAView> findCaPubByVoyageInPeriod(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Projection pour détails de diffusion avec info voyage
    interface DiffusionDetailView {
        Long getDiffusionId();

        Long getVoyageId();

        String getTrajetNom();

        LocalDate getDateDiffusion();

        Integer getNbDiffusions();

        BigDecimal getMontantTotal();
    }

    // Détails diffusions par annonceur avec info voyage
    @Query(value = """
            SELECT
                dd.id AS diffusionId,
                dd.voyage_id AS voyageId,
                t.nom AS trajetNom,
                dd.date_diffusion AS dateDiffusion,
                dd.nb_diffusions AS nbDiffusions,
                dd.montant_total AS montantTotal
            FROM details_diffusion dd
            JOIN diffusions_publicitaires dp ON dp.id = dd.diffusion_publicitaire_id
            JOIN voyages v ON v.id = dd.voyage_id
            JOIN trajets t ON t.id = v.trajet_id
            WHERE dp.annonceur_id = :annonceurId
            ORDER BY dd.date_diffusion DESC
            """, nativeQuery = true)
    List<DiffusionDetailView> findDiffusionDetailsByAnnonceurId(@Param("annonceurId") Long annonceurId);

    // Détails diffusions par annonceur dans une période
    @Query(value = """
            SELECT
                dd.id AS diffusionId,
                dd.voyage_id AS voyageId,
                t.nom AS trajetNom,
                dd.date_diffusion AS dateDiffusion,
                dd.nb_diffusions AS nbDiffusions,
                dd.montant_total AS montantTotal
            FROM details_diffusion dd
            JOIN diffusions_publicitaires dp ON dp.id = dd.diffusion_publicitaire_id
            JOIN voyages v ON v.id = dd.voyage_id
            JOIN trajets t ON t.id = v.trajet_id
            WHERE dp.annonceur_id = :annonceurId
            AND dd.date_diffusion >= :from AND dd.date_diffusion < :to
            ORDER BY dd.date_diffusion DESC
            """, nativeQuery = true)
    List<DiffusionDetailView> findDiffusionDetailsByAnnonceurIdAndPeriod(
            @Param("annonceurId") Long annonceurId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);
}
