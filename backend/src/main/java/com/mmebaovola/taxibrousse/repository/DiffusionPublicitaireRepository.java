package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.DiffusionPublicitaire;
import com.mmebaovola.taxibrousse.dto.AnnonceurDetailsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiffusionPublicitaireRepository extends JpaRepository<DiffusionPublicitaire, Long> {

    @Query("SELECT COALESCE(SUM(d.prixUnitaire * d.nbDiffusions), 0) FROM DiffusionPublicitaire d WHERE d.dateDiffusion >= :from AND d.dateDiffusion < :to")
    BigDecimal sumTarifBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("SELECT new com.mmebaovola.taxibrousse.repository.SocieteCaProjection(d.annonceur.id, d.annonceur.nom, COALESCE(SUM(d.prixUnitaire * d.nbDiffusions),0)) " +
           "FROM DiffusionPublicitaire d WHERE d.dateDiffusion >= :from AND d.dateDiffusion < :to GROUP BY d.annonceur.id, d.annonceur.nom")
    List<SocieteCaProjection> findCaPerSocieteBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("SELECT new com.mmebaovola.taxibrousse.dto.AnnonceurDetailsDto(" +
           "d.annonceur.id, d.annonceur.nom, " +
           "COALESCE(SUM(d.prixUnitaire * d.nbDiffusions), 0), " +
           "COALESCE(SUM(d.nbDiffusions), 0), " +
           "COALESCE(AVG(d.prixUnitaire), 0)) " +
           "FROM DiffusionPublicitaire d WHERE d.dateDiffusion >= :from AND d.dateDiffusion < :to " +
           "GROUP BY d.annonceur.id, d.annonceur.nom " +
           "ORDER BY SUM(d.prixUnitaire * d.nbDiffusions) DESC")
    List<AnnonceurDetailsDto> findDetailedCaPerSocieteBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
