package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.TarifPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TarifPlaceRepository extends JpaRepository<TarifPlace, Long> {

    List<TarifPlace> findByTrajetId(Long trajetId);

    @Query("SELECT tp FROM TarifPlace tp WHERE tp.trajet.id = :trajetId AND tp.typePlace = :typePlace ORDER BY tp.dateEffective DESC")
    List<TarifPlace> findByTrajetIdAndTypePlaceOrderByDateEffectiveDesc(@Param("trajetId") Long trajetId,
            @Param("typePlace") String typePlace);

    /**
     * Récupère le tarif le plus récent pour un trajet et un type de place
     */
    @Query("SELECT tp FROM TarifPlace tp WHERE tp.trajet.id = :trajetId AND tp.typePlace = :typePlace AND tp.dateEffective <= :date ORDER BY tp.dateEffective DESC LIMIT 1")
    Optional<TarifPlace> findCurrentTarif(@Param("trajetId") Long trajetId, @Param("typePlace") String typePlace,
            @Param("date") LocalDate date);

    /**
     * Récupère tous les tarifs effectifs pour un trajet à une date donnée
     */
    @Query(value = """
            SELECT tp.* FROM tarifs_places tp
            WHERE tp.trajet_id = :trajetId
            AND tp.date_effective = (
                SELECT MAX(tp2.date_effective) FROM tarifs_places tp2
                WHERE tp2.trajet_id = tp.trajet_id
                AND tp2.type_place = tp.type_place
                AND tp2.date_effective <= :date
            )
            """, nativeQuery = true)
    List<TarifPlace> findCurrentTarifsByTrajet(@Param("trajetId") Long trajetId, @Param("date") LocalDate date);
}
