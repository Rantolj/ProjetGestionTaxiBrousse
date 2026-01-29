package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    
    List<Produit> findAllByOrderByLibelleAsc();
    
    List<Produit> findByLibelleContainingIgnoreCase(String libelle);
}
