package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.DetailsReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetailsReservationRepository extends JpaRepository<DetailsReservation, Long> {
    boolean existsByReservationId(Long reservationId);

    List<DetailsReservation> findByReservation_Voyage_Id(Long voyageId);
}