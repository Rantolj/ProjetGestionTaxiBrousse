package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.Personne;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonneRepository extends JpaRepository<Personne, Long> {
}
