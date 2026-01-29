package com.mmebaovola.taxibrousse.service;

import com.mmebaovola.taxibrousse.entity.PaiementProduit;
import com.mmebaovola.taxibrousse.entity.Produit;
import com.mmebaovola.taxibrousse.entity.VenteProduit;
import com.mmebaovola.taxibrousse.entity.VenteProduitDetail;
import com.mmebaovola.taxibrousse.repository.PaiementProduitRepository;
import com.mmebaovola.taxibrousse.repository.ProduitRepository;
import com.mmebaovola.taxibrousse.repository.VenteProduitDetailRepository;
import com.mmebaovola.taxibrousse.repository.VenteProduitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VenteProduitService {

    private final VenteProduitRepository venteProduitRepository;
    private final VenteProduitDetailRepository venteDetailRepository;
    private final PaiementProduitRepository paiementRepository;
    private final ProduitRepository produitRepository;
    private final ProduitService produitService;

    public VenteProduitService(VenteProduitRepository venteProduitRepository,
                               VenteProduitDetailRepository venteDetailRepository,
                               PaiementProduitRepository paiementRepository,
                               ProduitRepository produitRepository,
                               ProduitService produitService) {
        this.venteProduitRepository = venteProduitRepository;
        this.venteDetailRepository = venteDetailRepository;
        this.paiementRepository = paiementRepository;
        this.produitRepository = produitRepository;
        this.produitService = produitService;
    }

    public List<VenteProduit> findAll() {
        return venteProduitRepository.findAll();
    }

    public List<VenteProduit> findByPeriode(LocalDate from, LocalDate to) {
        return venteProduitRepository.findByDateVenteBetweenOrderByDateVenteDesc(from, to);
    }

    public Optional<VenteProduit> findById(Long id) {
        return venteProduitRepository.findById(id);
    }

    /**
     * Crée une nouvelle vente avec ses détails
     */
    public VenteProduit createVente(LocalDate dateVente, List<VenteDetailDTO> details) {
        VenteProduit vente = new VenteProduit();
        vente.setDateVente(dateVente != null ? dateVente : LocalDate.now());
        vente.setDetails(new ArrayList<>());
        vente = venteProduitRepository.save(vente);

        for (VenteDetailDTO dto : details) {
            Produit produit = produitRepository.findById(dto.produitId())
                    .orElseThrow(() -> new RuntimeException("Produit non trouvé: " + dto.produitId()));

            BigDecimal prixUnitaire = produitService.getPrixADate(dto.produitId(), vente.getDateVente());

            VenteProduitDetail detail = new VenteProduitDetail();
            detail.setVente(vente);
            detail.setProduit(produit);
            detail.setQuantite(dto.quantite());
            detail.setPrixUnitaire(prixUnitaire);

            venteDetailRepository.save(detail);
            vente.getDetails().add(detail);
        }

        return vente;
    }

    /**
     * DTO pour les détails de vente
     */
    public record VenteDetailDTO(Long produitId, Integer quantite) {}

    /**
     * Calcule le montant total d'une vente
     */
    public BigDecimal getMontantTotal(Long venteId) {
        VenteProduit vente = venteProduitRepository.findById(venteId)
                .orElseThrow(() -> new RuntimeException("Vente non trouvée"));

        return vente.getDetails().stream()
                .map(VenteProduitDetail::getMontantTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcule le montant payé pour une vente
     */
    public BigDecimal getMontantPaye(Long venteId) {
        return paiementRepository.findTotalPayeByVenteId(venteId);
    }

    /**
     * Calcule le reste à payer pour une vente
     */
    public BigDecimal getResteAPayer(Long venteId) {
        BigDecimal total = getMontantTotal(venteId);
        BigDecimal paye = getMontantPaye(venteId);
        return total.subtract(paye);
    }

    /**
     * Enregistre un paiement pour une vente
     */
    public PaiementProduit enregistrerPaiement(Long venteId, BigDecimal montant, LocalDate datePaiement) {
        VenteProduit vente = venteProduitRepository.findById(venteId)
                .orElseThrow(() -> new RuntimeException("Vente non trouvée"));

        PaiementProduit paiement = new PaiementProduit();
        paiement.setVente(vente);
        paiement.setMontantPaye(montant);
        paiement.setDatePaiement(datePaiement != null ? datePaiement : LocalDate.now());

        return paiementRepository.save(paiement);
    }

    /**
     * CA total des produits pour une période
     */
    public BigDecimal getTotalCA(LocalDate from, LocalDate to) {
        return venteProduitRepository.findTotalCAByPeriode(from, to);
    }

    /**
     * CA ENCAISSE des produits pour une période (somme des paiements reçus)
     */
    public BigDecimal getTotalCAPaye(LocalDate from, LocalDate to) {
        return paiementRepository.findTotalPayeByPeriode(from, to);
    }

    /**
     * CA des produits par mois
     */
    public List<CAMensuelDTO> getCAParMois(LocalDate from, LocalDate to) {
        List<Object[]> results = venteProduitRepository.findCAByMois(from, to);
        List<CAMensuelDTO> caList = new ArrayList<>();

        for (Object[] row : results) {
            int annee = ((Number) row[0]).intValue();
            int mois = ((Number) row[1]).intValue();
            BigDecimal total = (BigDecimal) row[2];
            caList.add(new CAMensuelDTO(annee, mois, total));
        }

        return caList;
    }

    /**
     * DTO pour le CA mensuel
     */
    public record CAMensuelDTO(int annee, int mois, BigDecimal total) {}

    /**
     * CA par produit pour une période
     */
    public List<VenteProduitRepository.ProduitCAView> getCAParProduit(LocalDate from, LocalDate to) {
        return venteProduitRepository.findCAByProduit(from, to);
    }

    public void delete(Long id) {
        venteProduitRepository.deleteById(id);
    }
}
