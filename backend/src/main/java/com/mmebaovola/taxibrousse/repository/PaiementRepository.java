package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {
}