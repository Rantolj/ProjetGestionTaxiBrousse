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

       /**
        * Calcule le CA total des diffusions publicitaires sur une période
        * Utilise la table details_diffusion pour les diffusions effectives
        */
       @Query(value = """
                     SELECT COALESCE(SUM(dd.montant_total), 0)
                     FROM details_diffusion dd
                     WHERE dd.date_diffusion >= :from AND dd.date_diffusion < :to
                     """, nativeQuery = true)
       BigDecimal sumTarifBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);

       /**
        * CA par société/annonceur sur une période
        */
       @Query(value = """
                     SELECT a.id AS annonceurId, a.nom AS nom, COALESCE(SUM(dd.montant_total), 0) AS ca
                     FROM annonceurs a
                     LEFT JOIN diffusions_publicitaires dp ON dp.annonceur_id = a.id
                     LEFT JOIN details_diffusion dd ON dd.diffusion_publicitaire_id = dp.id
                         AND dd.date_diffusion >= :from AND dd.date_diffusion < :to
                     GROUP BY a.id, a.nom
                     ORDER BY ca DESC
                     """, nativeQuery = true)
       List<SocieteCaProjection> findCaPerSocieteBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);

       /**
        * Détails CA par annonceur avec statistiques sur une période
        */
       @Query(value = """
                     SELECT
                         a.id AS annonceurId,
                         a.nom AS nom,
                         COALESCE(SUM(dd.montant_total), 0) AS ca,
                         COALESCE(SUM(dd.nb_diffusions), 0) AS nbDiffusions,
                         COALESCE(AVG(dp.prix_unitaire), 0) AS avgPrix
                     FROM annonceurs a
                     LEFT JOIN diffusions_publicitaires dp ON dp.annonceur_id = a.id
                     LEFT JOIN details_diffusion dd ON dd.diffusion_publicitaire_id = dp.id
                         AND dd.date_diffusion >= :from AND dd.date_diffusion < :to
                     GROUP BY a.id, a.nom
                     ORDER BY ca DESC
                     """, nativeQuery = true)
       List<AnnonceurDetailsCaView> findDetailedCaPerSocieteBetween(@Param("from") LocalDate from,
                     @Param("to") LocalDate to);

       /**
        * Interface pour projection détaillée des CA par annonceur
        */
       interface AnnonceurDetailsCaView {
              Long getAnnonceurId();

              String getNom();

              BigDecimal getCa();

              Long getNbDiffusions();

              BigDecimal getAvgPrix();
       }
}
