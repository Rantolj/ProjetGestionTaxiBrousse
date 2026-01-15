package com.mmebaovola.taxibrousse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "taxi_brousses")
@Getter
@Setter
@NoArgsConstructor
public class TaxiBrousse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String immatriculation;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private CategorieTb categorie;

    private Integer nbrPlaces;

    private Double chargeMax;

    private Double consommation;

    private String dispositionPlaces;

    @OneToMany(mappedBy = "taxiBrousse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CategoriePlace> categoriesPlaces = new ArrayList<>();

    /**
     * Calcule le CA maximum potentiel pour ce taxi-brousse
     * CA_max = Σ (nbr_places_type × prix_par_type) pour chaque catégorie
     */
    public BigDecimal getCAMaxPotentiel() {
        return categoriesPlaces.stream()
                .map(CategoriePlace::getCAMax)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}