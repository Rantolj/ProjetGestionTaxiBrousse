package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface VoyageRepository extends JpaRepository<Voyage, Long> {

    List<Voyage> findByTaxiBrousse_IdAndDateDepart(Long taxiBrousseId, LocalDateTime dateDepart);

    List<Voyage> findByChauffeur_IdAndDateDepart(Long chauffeurId, LocalDateTime dateDepart);

    /**
     * Calcule le CA max potentiel pour un voyage donné
     * Formule: SUM(nbr_places_type * prix)
     * 
     * Le prix est pris de tarifs_places si configuré, sinon de
     * categories_places.prix_par_type
     * Version SIMPLIFIÉE avec LEFT JOIN pour robustesse
     */
    @Query(value = """
            SELECT COALESCE(SUM(
                cp.nbr_places_type * COALESCE(tp.montant, cp.prix_par_type)
            ), 0)
            FROM voyages v
            INNER JOIN categories_places cp ON cp.taxi_brousse_id = v.taxi_brousse_id
            LEFT JOIN tarifs_places tp ON tp.trajet_id = v.trajet_id
                AND (UPPER(TRIM(tp.type_place)) = UPPER(TRIM(cp.type))
                     OR tp.type_place = cp.type)
                AND tp.date_effective <= CAST(v.date_depart AS DATE)
            WHERE v.id = :voyageId
            GROUP BY v.id
            """, nativeQuery = true)
    BigDecimal calculateCAMaxByVoyage(@Param("voyageId") Long voyageId);

    /**
     * Récupère les CA max pour tous les voyages dans une période donnée
     */
    interface VoyageCAMaxView {
        Long getVoyageId();

        String getImmatriculation();

        String getTrajetNom();

        LocalDateTime getDateDepart();

        BigDecimal getCaMax();

        BigDecimal getCaReel();

        Integer getTauxRemplissage();
    }

    @Query(value = """
            SELECT
                v.id AS voyageId,
                tb.immatriculation AS immatriculation,
                t.nom AS trajetNom,
                v.date_depart AS dateDepart,
                COALESCE((
                    SELECT SUM(cp.nbr_places_type * COALESCE(tp.montant, cp.prix_par_type))
                    FROM categories_places cp
                    LEFT JOIN tarifs_places tp ON tp.trajet_id = v.trajet_id
                        AND (UPPER(TRIM(tp.type_place)) = UPPER(TRIM(cp.type)) OR tp.type_place = cp.type)
                        AND tp.date_effective <= CAST(v.date_depart AS DATE)
                    WHERE cp.taxi_brousse_id = v.taxi_brousse_id
                    GROUP BY v.id, v.taxi_brousse_id
                ), 0) AS caMax,
                COALESCE((
                    SELECT SUM(r.montant_total) 
                    FROM reservations r
                    WHERE r.voyage_id = v.id
                ), 0) AS caReel,
                CASE
                    WHEN COALESCE((
                        SELECT SUM(cp.nbr_places_type * COALESCE(tp.montant, cp.prix_par_type))
                        FROM categories_places cp
                        LEFT JOIN tarifs_places tp ON tp.trajet_id = v.trajet_id
                            AND (UPPER(TRIM(tp.type_place)) = UPPER(TRIM(cp.type)) OR tp.type_place = cp.type)
                            AND tp.date_effective <= CAST(v.date_depart AS DATE)
                        WHERE cp.taxi_brousse_id = v.taxi_brousse_id
                        GROUP BY v.id, v.taxi_brousse_id
                    ), 0) > 0
                    THEN CAST(
                        COALESCE((SELECT SUM(r.montant_total) FROM reservations r WHERE r.voyage_id = v.id), 0) * 100
                        / COALESCE((
                            SELECT SUM(cp.nbr_places_type * COALESCE(tp.montant, cp.prix_par_type))
                            FROM categories_places cp
                            LEFT JOIN tarifs_places tp ON tp.trajet_id = v.trajet_id
                                AND (UPPER(TRIM(tp.type_place)) = UPPER(TRIM(cp.type)) OR tp.type_place = cp.type)
                                AND tp.date_effective <= CAST(v.date_depart AS DATE)
                            WHERE cp.taxi_brousse_id = v.taxi_brousse_id
                            GROUP BY v.id, v.taxi_brousse_id
                        ), 1) AS INTEGER)
                    ELSE 0
                END AS tauxRemplissage
            FROM voyages v
            JOIN taxi_brousses tb ON tb.id = v.taxi_brousse_id
            JOIN trajets t ON t.id = v.trajet_id
            WHERE v.date_depart >= :startDate AND v.date_depart < :endDate
            ORDER BY v.date_depart DESC
            """, nativeQuery = true)
    List<VoyageCAMaxView> findVoyagesWithCAMax(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}