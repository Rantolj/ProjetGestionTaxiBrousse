package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.TaxiBrousse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxiBrousseRepository extends JpaRepository<TaxiBrousse, Long> {
}