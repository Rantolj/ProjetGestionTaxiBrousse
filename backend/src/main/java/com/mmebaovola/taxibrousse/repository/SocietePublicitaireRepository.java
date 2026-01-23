package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.SocietePublicitaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocietePublicitaireRepository extends JpaRepository<SocietePublicitaire, Long> {
}
