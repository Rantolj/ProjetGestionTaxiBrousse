package com.mmebaovola.taxibrousse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "paiements_produits")
@Getter
@Setter
@NoArgsConstructor
public class PaiementProduit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vente_id", nullable = false)
    private VenteProduit vente;

    @Column(name = "montant_paye", nullable = false, precision = 15, scale = 2)
    private BigDecimal montantPaye;

    @Column(name = "date_paiement", nullable = false)
    private LocalDate datePaiement = LocalDate.now();

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
