package com.mmebaovola.taxibrousse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "paiements_annonceurs")
public class PaiementAnnonceur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annonceur_id", nullable = false)
    private SocietePublicitaire annonceur;

    @Column(name = "montant_paye", nullable = false)
    private BigDecimal montantPaye;

    @Column(name = "date_paiement", nullable = false)
    private LocalDate datePaiement;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public PaiementAnnonceur() {
    }

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

    public BigDecimal getMontantPaye() {
        return montantPaye;
    }

    public void setMontantPaye(BigDecimal montantPaye) {
        this.montantPaye = montantPaye;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
