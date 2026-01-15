package com.mmebaovola.taxibrousse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "categories_places")
public class CategoriePlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "taxi_brousse_id")
    private TaxiBrousse taxiBrousse;

    @Column(name = "type", nullable = false, length = 50)
    private String type; // 'STANDARD', 'PREMIUM'

    @Column(name = "nbr_places_type", nullable = false)
    private Integer nbrPlacesType;

    @Column(name = "prix_par_type", nullable = false, precision = 15, scale = 2)
    private BigDecimal prixParType;

    // Constructeurs
    public CategoriePlace() {
    }

    public CategoriePlace(TaxiBrousse taxiBrousse, String type, Integer nbrPlacesType, BigDecimal prixParType) {
        this.taxiBrousse = taxiBrousse;
        this.type = type;
        this.nbrPlacesType = nbrPlacesType;
        this.prixParType = prixParType;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaxiBrousse getTaxiBrousse() {
        return taxiBrousse;
    }

    public void setTaxiBrousse(TaxiBrousse taxiBrousse) {
        this.taxiBrousse = taxiBrousse;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNbrPlacesType() {
        return nbrPlacesType;
    }

    public void setNbrPlacesType(Integer nbrPlacesType) {
        this.nbrPlacesType = nbrPlacesType;
    }

    public BigDecimal getPrixParType() {
        return prixParType;
    }

    public void setPrixParType(BigDecimal prixParType) {
        this.prixParType = prixParType;
    }

    // Méthode utilitaire pour calculer le CA potentiel de cette catégorie
    public BigDecimal getCAMax() {
        return prixParType.multiply(BigDecimal.valueOf(nbrPlacesType));
    }
}
