package com.mmebaovola.taxibrousse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "diffusions_publicitaires")
public class DiffusionPublicitaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annonceur_id")
    private SocietePublicitaire annonceur;

    @Column(name = "date_diffusion", nullable = false)
    private LocalDate dateDiffusion;

    @Column(name = "nb_diffusions", nullable = false)
    private Integer nbDiffusions = 1;

    @Column(name = "prix_unitaire", nullable = false, precision = 15, scale = 2)
    private BigDecimal prixUnitaire;

    @Column(name = "montant_total", precision = 15, scale = 2)
    private BigDecimal montantTotal;

    private String note;

    // getters / setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SocietePublicitaire getAnnonceur() {
        return annonceur;
    }

    public void setAnnonceur(SocietePublicitaire annonceur) {
        this.annonceur = annonceur;
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

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public BigDecimal getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiffusionPublicitaire)) return false;
        DiffusionPublicitaire that = (DiffusionPublicitaire) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

