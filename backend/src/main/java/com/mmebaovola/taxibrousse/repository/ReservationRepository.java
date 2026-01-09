package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByClientId(Long clientId);
}