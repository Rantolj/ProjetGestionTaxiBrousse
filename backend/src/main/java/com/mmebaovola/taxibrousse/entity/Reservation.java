package com.mmebaovola.taxibrousse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "voyage_id")
    private Voyage voyage;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private Double montantTotal;

    private LocalDateTime dateReservation;
}