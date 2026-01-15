package com.mmebaovola.taxibrousse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tarifs_places")
public class TarifPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trajet_id")
    private Trajet trajet;

    @Column(name = "type_place", nullable = false, length = 50)
    private String typePlace; // 'STANDARD', 'PREMIUM'

    @Column(name = "montant", nullable = false, precision = 15, scale = 2)
    private BigDecimal montant;

    @Column(name = "date_effective", nullable = false)
    private LocalDate dateEffective;

    // Constructeurs
    public TarifPlace() {
    }

    public TarifPlace(Trajet trajet, String typePlace, BigDecimal montant, LocalDate dateEffective) {
        this.trajet = trajet;
        this.typePlace = typePlace;
        this.montant = montant;
        this.dateEffective = dateEffective;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Trajet getTrajet() {
        return trajet;
    }

    public void setTrajet(Trajet trajet) {
        this.trajet = trajet;
    }

    public String getTypePlace() {
        return typePlace;
    }

    public void setTypePlace(String typePlace) {
        this.typePlace = typePlace;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public LocalDate getDateEffective() {
        return dateEffective;
    }

    public void setDateEffective(LocalDate dateEffective) {
        this.dateEffective = dateEffective;
    }
}
