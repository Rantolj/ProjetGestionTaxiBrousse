package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.CategoriePlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CategoriePlaceRepository extends JpaRepository<CategoriePlace, Long> {

    List<CategoriePlace> findByTaxiBrousseId(Long taxiBrousseId);

    @Query("SELECT cp FROM CategoriePlace cp WHERE cp.taxiBrousse.id = :taxiBrousseId AND cp.type = :type")
    CategoriePlace findByTaxiBrousseIdAndType(@Param("taxiBrousseId") Long taxiBrousseId, @Param("type") String type);

    /**
     * Calcule le CA max potentiel pour un taxi-brousse donné
     */
    @Query("SELECT COALESCE(SUM(cp.nbrPlacesType * cp.prixParType), 0) FROM CategoriePlace cp WHERE cp.taxiBrousse.id = :taxiBrousseId")
    BigDecimal calculateCAMaxByTaxiBrousse(@Param("taxiBrousseId") Long taxiBrousseId);
}
