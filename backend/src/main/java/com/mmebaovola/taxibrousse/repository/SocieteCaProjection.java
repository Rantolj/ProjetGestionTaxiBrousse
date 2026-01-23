package com.mmebaovola.taxibrousse.repository;

import java.math.BigDecimal;

/**
 * Interface de projection pour le CA par société/annonceur
 * Utilisée par les requêtes natives qui retournent des tuples
 */
public interface SocieteCaProjection {
    Long getAnnonceurId();

    String getNom();

    BigDecimal getCa();

    // Alias pour compatibilité avec l'ancien code
    default Long getSocieteId() {
        return getAnnonceurId();
    }

    default BigDecimal getMontant() {
        return getCa();
    }
}
