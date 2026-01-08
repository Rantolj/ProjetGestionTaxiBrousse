package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoyageRepository extends JpaRepository<Voyage, Long> {
}