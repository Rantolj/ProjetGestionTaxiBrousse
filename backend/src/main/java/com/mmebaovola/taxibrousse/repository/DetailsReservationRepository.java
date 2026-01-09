package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.DetailsReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailsReservationRepository extends JpaRepository<DetailsReservation, Long> {
}