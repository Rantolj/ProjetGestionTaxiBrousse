package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.Trajet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrajetRepository extends JpaRepository<Trajet, Long> {
}