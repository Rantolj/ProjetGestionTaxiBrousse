package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VoyageRepository extends JpaRepository<Voyage, Long> {

    List<Voyage> findByTaxiBrousse_IdAndDateDepart(Long taxiBrousseId, LocalDateTime dateDepart);

    List<Voyage> findByChauffeur_IdAndDateDepart(Long chauffeurId, LocalDateTime dateDepart);
}