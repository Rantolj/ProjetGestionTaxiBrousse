package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.VenteProduitDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VenteProduitDetailRepository extends JpaRepository<VenteProduitDetail, Long> {

    List<VenteProduitDetail> findByVenteId(Long venteId);
}
