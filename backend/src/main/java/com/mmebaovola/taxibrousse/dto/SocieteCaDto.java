package com.mmebaovola.taxibrousse.dto;

import java.math.BigDecimal;

public class SocieteCaDto {

    private Long societeId;
    private String nom;
    private BigDecimal montant;

    public SocieteCaDto() {}

    public SocieteCaDto(Long societeId, String nom, BigDecimal montant) {
        this.societeId = societeId;
        this.nom = nom;
        this.montant = montant;
    }

    public Long getSocieteId() {
        return societeId;
    }

    public void setSocieteId(Long societeId) {
        this.societeId = societeId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }
}

