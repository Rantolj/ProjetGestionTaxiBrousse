package com.mmebaovola.taxibrousse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "details_reservations")
@Getter
@Setter
@NoArgsConstructor
public class DetailsReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    private String numeroPlace;

    @Column(name = "personne_id")
    private Long personneId;

    @Column(name = "type_place")
    private String typePlace;

    @Column(name = "is_enfant", nullable = false)
    private Boolean isEnfant = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "passager_categorie", nullable = false)
    private PassengerCategory passagerCategorie = PassengerCategory.ADULTE;

}