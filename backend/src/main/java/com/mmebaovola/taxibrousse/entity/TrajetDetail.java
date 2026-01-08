package com.mmebaovola.taxibrousse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trajet_details")
@Getter
@Setter
@NoArgsConstructor
public class TrajetDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trajet_id")
    private Trajet trajet;

    @ManyToOne
    @JoinColumn(name = "arret_id")
    private Arret arret;

    private Integer ordre;
}