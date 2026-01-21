package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    interface TaxiDailyTurnoverRow {
        Long getTaxiId();

        String getImmatriculation();

        java.sql.Date getJour();

        Double getTotal();
    }

    interface VoyageTurnoverRow {
        Long getVoyageId();

        String getImmatriculation();

        String getTrajetNom();

        LocalDateTime getDateDepart();

        Long getReservationCount();

        Double getTotal();
    }

    interface TrajetTurnoverRow {
        Long getTrajetId();

        String getTrajetNom();

        Long getReservationCount();

        Double getTotal();
    }

    boolean existsByClientId(Long clientId);

    @Query(value = """
            SELECT
                tb.id AS taxiId,
                tb.immatriculation AS immatriculation,
                CAST(v.date_depart AS date) AS jour,
                COALESCE(SUM(r.montant_total), 0) AS total
            FROM reservations r
            JOIN voyages v ON v.id = r.voyage_id
            JOIN taxi_brousses tb ON tb.id = v.taxi_brousse_id
            WHERE v.date_depart >= :startInclusive AND v.date_depart < :endExclusive
            GROUP BY tb.id, tb.immatriculation, CAST(v.date_depart AS date)
            ORDER BY CAST(v.date_depart AS date) DESC, tb.immatriculation ASC
            """, nativeQuery = true)
    List<TaxiDailyTurnoverRow> findTurnoverByTaxiPerDay(
            @Param("startInclusive") LocalDateTime startInclusive,
            @Param("endExclusive") LocalDateTime endExclusive);

    @Query(value = """
            SELECT
                v.id AS voyageId,
                tb.immatriculation AS immatriculation,
                t.nom AS trajetNom,
                v.date_depart AS dateDepart,
                COUNT(r.id) AS reservationCount,
                COALESCE(SUM(r.montant_total), 0) AS total
            FROM voyages v
            LEFT JOIN reservations r ON r.voyage_id = v.id
            LEFT JOIN taxi_brousses tb ON tb.id = v.taxi_brousse_id
            LEFT JOIN trajets t ON t.id = v.trajet_id
            WHERE v.date_depart >= :startInclusive AND v.date_depart < :endExclusive
            GROUP BY v.id, tb.immatriculation, t.nom, v.date_depart
            ORDER BY v.date_depart DESC, v.id DESC
            """, nativeQuery = true)
    List<VoyageTurnoverRow> findTurnoverByVoyage(
            @Param("startInclusive") LocalDateTime startInclusive,
            @Param("endExclusive") LocalDateTime endExclusive);

    @Query(value = """
            SELECT
                t.id AS trajetId,
                t.nom AS trajetNom,
                COUNT(r.id) AS reservationCount,
                COALESCE(SUM(r.montant_total), 0) AS total
            FROM voyages v
            LEFT JOIN trajets t ON t.id = v.trajet_id
            LEFT JOIN reservations r ON r.voyage_id = v.id
            WHERE v.date_depart >= :startInclusive AND v.date_depart < :endExclusive
              AND (:trajetId IS NULL OR t.id = :trajetId)
            GROUP BY t.id, t.nom
            ORDER BY total DESC
            """, nativeQuery = true)
    List<TrajetTurnoverRow> findTurnoverByTrajet(
            @Param("startInclusive") LocalDateTime startInclusive,
            @Param("endExclusive") LocalDateTime endExclusive,
            @Param("trajetId") Long trajetId);
}