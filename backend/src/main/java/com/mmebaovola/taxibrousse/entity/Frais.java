package com.mmebaovola.taxibrousse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "frais")
@Getter
@Setter
@NoArgsConstructor
public class Frais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trajet_id")
    private Trajet trajet;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private CategorieTb categorie;

    private Double montant;

    private LocalDate dateEffective;
}