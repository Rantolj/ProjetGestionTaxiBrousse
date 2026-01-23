package com.mmebaovola.taxibrousse.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * DTO pour le résumé du prorata d'un annonceur
 * Contient le total dû, total payé, pourcentage global et détails par diffusion
 */
public class AnnonceurProrataDto {

    private Long annonceurId;
    private String annonceurNom;
    private BigDecimal totalDu;
    private BigDecimal totalPaye;
    private BigDecimal soldeRestant;
    private BigDecimal pourcentageGlobalPaye; // Pourcentage total payé
    private List<DiffusionProrataDto> diffusionsProrata;

    public AnnonceurProrataDto() {
    }

    public AnnonceurProrataDto(Long annonceurId, String annonceurNom, BigDecimal totalDu,
            BigDecimal totalPaye, List<DiffusionProrataDto> diffusionsProrata) {
        this.annonceurId = annonceurId;
        this.annonceurNom = annonceurNom;
        this.totalDu = totalDu != null ? totalDu : BigDecimal.ZERO;
        this.totalPaye = totalPaye != null ? totalPaye : BigDecimal.ZERO;
        this.soldeRestant = this.totalDu.subtract(this.totalPaye);
        this.diffusionsProrata = diffusionsProrata;

        // Calcul pourcentage global
        if (this.totalDu.compareTo(BigDecimal.ZERO) > 0) {
            this.pourcentageGlobalPaye = this.totalPaye
                    .multiply(BigDecimal.valueOf(100))
                    .divide(this.totalDu, 2, RoundingMode.HALF_UP);
        } else {
            this.pourcentageGlobalPaye = BigDecimal.ZERO;
        }
    }

    /**
     * Calcule le montant payé au prorata pour une diffusion donnée
     * Formule: montantPayeDiffusion = (montantDiffusion / totalDu) * totalPaye
     * Ou simplement: montantPayeDiffusion = montantDiffusion *
     * (pourcentageGlobalPaye / 100)
     */
    public static BigDecimal calculerProrataParDiffusion(BigDecimal montantDiffusion,
            BigDecimal totalDu, BigDecimal totalPaye) {
        if (totalDu == null || totalDu.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (totalPaye == null || totalPaye.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        // prorata = montantDiffusion * (totalPaye / totalDu)
        return montantDiffusion
                .multiply(totalPaye)
                .divide(totalDu, 2, RoundingMode.HALF_UP);
    }

    // Getters et Setters
    public Long getAnnonceurId() {
        return annonceurId;
    }

    public void setAnnonceurId(Long annonceurId) {
        this.annonceurId = annonceurId;
    }

    public String getAnnonceurNom() {
        return annonceurNom;
    }

    public void setAnnonceurNom(String annonceurNom) {
        this.annonceurNom = annonceurNom;
    }

    public BigDecimal getTotalDu() {
        return totalDu;
    }

    public void setTotalDu(BigDecimal totalDu) {
        this.totalDu = totalDu;
    }

    public BigDecimal getTotalPaye() {
        return totalPaye;
    }

    public void setTotalPaye(BigDecimal totalPaye) {
        this.totalPaye = totalPaye;
    }

    public BigDecimal getSoldeRestant() {
        return soldeRestant;
    }

    public void setSoldeRestant(BigDecimal soldeRestant) {
        this.soldeRestant = soldeRestant;
    }

    public BigDecimal getPourcentageGlobalPaye() {
        return pourcentageGlobalPaye;
    }

    public void setPourcentageGlobalPaye(BigDecimal pourcentageGlobalPaye) {
        this.pourcentageGlobalPaye = pourcentageGlobalPaye;
    }

    public List<DiffusionProrataDto> getDiffusionsProrata() {
        return diffusionsProrata;
    }

    public void setDiffusionsProrata(List<DiffusionProrataDto> diffusionsProrata) {
        this.diffusionsProrata = diffusionsProrata;
    }
}
