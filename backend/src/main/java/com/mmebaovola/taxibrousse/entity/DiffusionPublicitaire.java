package com.mmebaovola.taxibrousse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "diffusions_publicitaires")
public class DiffusionPublicitaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annonceur_id")
    private SocietePublicitaire annonceur;

    @Column(name = "prix_unitaire", nullable = false, precision = 15, scale = 2)
    private BigDecimal prixUnitaire;

    private String note;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "diffusionPublicitaire", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetailsDiffusion> detailsDiffusions = new ArrayList<>();

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

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<DetailsDiffusion> getDetailsDiffusions() {
        return detailsDiffusions;
    }

    public void setDetailsDiffusions(List<DetailsDiffusion> detailsDiffusions) {
        this.detailsDiffusions = detailsDiffusions;
    }
}
