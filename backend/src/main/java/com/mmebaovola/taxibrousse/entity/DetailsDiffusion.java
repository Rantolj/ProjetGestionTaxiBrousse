package com.mmebaovola.taxibrousse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "details_diffusion")
public class DetailsDiffusion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diffusion_publicitaire_id", nullable = false)
    private DiffusionPublicitaire diffusionPublicitaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voyage_id", nullable = false)
    private Voyage voyage;

    @Column(name = "nb_diffusions", nullable = false)
    private Integer nbDiffusions = 1;

    @Column(name = "montant_total", precision = 15, scale = 2)
    private BigDecimal montantTotal = BigDecimal.ZERO;

    @Column(name = "date_diffusion", nullable = false)
    private LocalDate dateDiffusion;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Constructeurs
    public DetailsDiffusion() {
    }

    // Getters / Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DiffusionPublicitaire getDiffusionPublicitaire() {
        return diffusionPublicitaire;
    }

    public void setDiffusionPublicitaire(DiffusionPublicitaire diffusionPublicitaire) {
        this.diffusionPublicitaire = diffusionPublicitaire;
    }

    public Voyage getVoyage() {
        return voyage;
    }

    public void setVoyage(Voyage voyage) {
        this.voyage = voyage;
    }

    public Integer getNbDiffusions() {
        return nbDiffusions;
    }

    public void setNbDiffusions(Integer nbDiffusions) {
        this.nbDiffusions = nbDiffusions;
    }

    public BigDecimal getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
    }

    public LocalDate getDateDiffusion() {
        return dateDiffusion;
    }

    public void setDateDiffusion(LocalDate dateDiffusion) {
        this.dateDiffusion = dateDiffusion;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Méthode utilitaire pour calculer le montant total
    public void calculerMontantTotal() {
        if (diffusionPublicitaire != null && diffusionPublicitaire.getPrixUnitaire() != null && nbDiffusions != null) {
            this.montantTotal = diffusionPublicitaire.getPrixUnitaire().multiply(BigDecimal.valueOf(nbDiffusions));
        }
    }
}
