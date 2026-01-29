package com.mmebaovola.taxibrousse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vente_produit_details")
@Getter
@Setter
@NoArgsConstructor
public class VenteProduitDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vente_id", nullable = false)
    private VenteProduit vente;

    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @Column(nullable = false)
    private Integer quantite = 1;

    @Column(name = "prix_unitaire", nullable = false, precision = 15, scale = 2)
    private BigDecimal prixUnitaire;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Calcule le montant total (quantité * prix unitaire) - non persisté
     */
    public BigDecimal getMontantTotal() {
        if (quantite != null && prixUnitaire != null) {
            return prixUnitaire.multiply(BigDecimal.valueOf(quantite));
        }
        return BigDecimal.ZERO;
    }
}
