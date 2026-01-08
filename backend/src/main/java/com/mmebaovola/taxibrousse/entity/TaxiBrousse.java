package com.mmebaovola.taxibrousse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}