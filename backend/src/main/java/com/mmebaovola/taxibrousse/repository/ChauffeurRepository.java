package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.Chauffeur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChauffeurRepository extends JpaRepository<Chauffeur, Long> {
}