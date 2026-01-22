package com.mmebaovola.taxibrousse.dto;

import java.math.BigDecimal;

public class AnnonceurDetailsDto {
    private Long annonceurId;
    private String nom;
    private BigDecimal montant;
    private Long nbDiffusions;
    private Double prixUnitaireMoyen;
    private BigDecimal montantPaye;
    private BigDecimal solde;

    public AnnonceurDetailsDto() {
    }

    public AnnonceurDetailsDto(Long annonceurId, String nom, BigDecimal montant, Long nbDiffusions,
            Double prixUnitaireMoyen) {
        this.annonceurId = annonceurId;
        this.nom = nom;
        this.montant = montant;
        this.nbDiffusions = nbDiffusions;
        this.prixUnitaireMoyen = prixUnitaireMoyen;
        this.montantPaye = BigDecimal.ZERO;
        this.solde = montant != null ? montant : BigDecimal.ZERO;
    }

    public Long getAnnonceurId() {
        return annonceurId;
    }

    public void setAnnonceurId(Long annonceurId) {
        this.annonceurId = annonceurId;
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

    public Long getNbDiffusions() {
        return nbDiffusions;
    }

    public void setNbDiffusions(Long nbDiffusions) {
        this.nbDiffusions = nbDiffusions;
    }

    public Double getPrixUnitaireMoyen() {
        return prixUnitaireMoyen;
    }

    public void setPrixUnitaireMoyen(Double prixUnitaireMoyen) {
        this.prixUnitaireMoyen = prixUnitaireMoyen;
    }

    public BigDecimal getMontantPaye() {
        return montantPaye;
    }

    public void setMontantPaye(BigDecimal montantPaye) {
        this.montantPaye = montantPaye;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }
}
