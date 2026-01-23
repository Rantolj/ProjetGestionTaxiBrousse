package com.mmebaovola.taxibrousse.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * DTO pour afficher le calcul du prorata par diffusion
 * Le prorata répartit les paiements proportionnellement sur chaque diffusion
 */
public class DiffusionProrataDto {

    private Long diffusionId;
    private Long voyageId;
    private String voyageInfo; // ex: "Tana - Toamasina (15/01/2026)"
    private LocalDate dateDiffusion;
    private Integer nbDiffusions;
    private BigDecimal montantDu; // Montant total de cette diffusion
    private BigDecimal montantPaye; // Montant payé (prorata)
    private BigDecimal soldeRestant; // Montant restant à payer
    private BigDecimal pourcentagePaye; // Pourcentage payé

    public DiffusionProrataDto() {
    }

    public DiffusionProrataDto(Long diffusionId, Long voyageId, String voyageInfo,
            LocalDate dateDiffusion, Integer nbDiffusions, BigDecimal montantDu,
            BigDecimal montantPaye) {
        this.diffusionId = diffusionId;
        this.voyageId = voyageId;
        this.voyageInfo = voyageInfo;
        this.dateDiffusion = dateDiffusion;
        this.nbDiffusions = nbDiffusions;
        this.montantDu = montantDu != null ? montantDu : BigDecimal.ZERO;
        this.montantPaye = montantPaye != null ? montantPaye : BigDecimal.ZERO;
        this.soldeRestant = this.montantDu.subtract(this.montantPaye);

        // Calcul pourcentage payé
        if (this.montantDu.compareTo(BigDecimal.ZERO) > 0) {
            this.pourcentagePaye = this.montantPaye
                    .multiply(BigDecimal.valueOf(100))
                    .divide(this.montantDu, 2, RoundingMode.HALF_UP);
        } else {
            this.pourcentagePaye = BigDecimal.ZERO;
        }
    }

    // Getters et Setters
    public Long getDiffusionId() {
        return diffusionId;
    }

    public void setDiffusionId(Long diffusionId) {
        this.diffusionId = diffusionId;
    }

    public Long getVoyageId() {
        return voyageId;
    }

    public void setVoyageId(Long voyageId) {
        this.voyageId = voyageId;
    }

    public String getVoyageInfo() {
        return voyageInfo;
    }

    public void setVoyageInfo(String voyageInfo) {
        this.voyageInfo = voyageInfo;
    }

    public LocalDate getDateDiffusion() {
        return dateDiffusion;
    }

    public void setDateDiffusion(LocalDate dateDiffusion) {
        this.dateDiffusion = dateDiffusion;
    }

    public Integer getNbDiffusions() {
        return nbDiffusions;
    }

    public void setNbDiffusions(Integer nbDiffusions) {
        this.nbDiffusions = nbDiffusions;
    }

    public BigDecimal getMontantDu() {
        return montantDu;
    }

    public void setMontantDu(BigDecimal montantDu) {
        this.montantDu = montantDu;
    }

    public BigDecimal getMontantPaye() {
        return montantPaye;
    }

    public void setMontantPaye(BigDecimal montantPaye) {
        this.montantPaye = montantPaye;
    }

    public BigDecimal getSoldeRestant() {
        return soldeRestant;
    }

    public void setSoldeRestant(BigDecimal soldeRestant) {
        this.soldeRestant = soldeRestant;
    }

    public BigDecimal getPourcentagePaye() {
        return pourcentagePaye;
    }

    public void setPourcentagePaye(BigDecimal pourcentagePaye) {
        this.pourcentagePaye = pourcentagePaye;
    }
}
