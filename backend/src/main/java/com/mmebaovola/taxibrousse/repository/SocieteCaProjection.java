package com.mmebaovola.taxibrousse.repository;

import java.math.BigDecimal;

public class SocieteCaProjection {
    private Long societeId;
    private String nom;
    private BigDecimal montant;

    public SocieteCaProjection(Long societeId, String nom, BigDecimal montant) {
        this.societeId = societeId;
        this.nom = nom;
        this.montant = montant;
    }

    public Long getSocieteId() {
        return societeId;
    }

    public String getNom() {
        return nom;
    }

    public BigDecimal getMontant() {
        return montant;
    }
}
