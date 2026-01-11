package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.TrajetDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrajetDetailRepository extends JpaRepository<TrajetDetail, Long> {
	List<TrajetDetail> findByTrajetIdOrderByOrdreAsc(Long trajetId);
}