package com.mmebaovola.taxibrousse.service;

import com.mmebaovola.taxibrousse.dto.AnnonceurDetailsDto;
import com.mmebaovola.taxibrousse.dto.CaResponseDto;
import com.mmebaovola.taxibrousse.dto.SocieteCaDto;
import com.mmebaovola.taxibrousse.entity.PaiementAnnonceur;
import com.mmebaovola.taxibrousse.entity.SocietePublicitaire;
import com.mmebaovola.taxibrousse.repository.DiffusionPublicitaireRepository;
import com.mmebaovola.taxibrousse.repository.PaiementAnnonceurRepository;
import com.mmebaovola.taxibrousse.repository.SocieteCaProjection;
import com.mmebaovola.taxibrousse.repository.SocietePublicitaireRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PubliciteService {

    private final DiffusionPublicitaireRepository diffusionRepository;
    private final PaiementAnnonceurRepository paiementRepo;
    private final SocietePublicitaireRepository annonceurRepo;

    public PubliciteService(DiffusionPublicitaireRepository diffusionRepository,
            PaiementAnnonceurRepository paiementRepo,
            SocietePublicitaireRepository annonceurRepo) {
        this.diffusionRepository = diffusionRepository;
        this.paiementRepo = paiementRepo;
        this.annonceurRepo = annonceurRepo;
    }

    public CaResponseDto calculateCaForPeriod(LocalDate from, LocalDate to) {
        BigDecimal total = diffusionRepository.sumTarifBetween(from, to);
        if (total == null)
            total = BigDecimal.ZERO;

        List<SocieteCaProjection> projections = diffusionRepository.findCaPerSocieteBetween(from, to);
        List<SocieteCaDto> perSociete = projections.stream()
                .map(p -> new SocieteCaDto(p.getSocieteId(), p.getNom(), p.getMontant()))
                .collect(Collectors.toList());

        return new CaResponseDto(total, perSociete);
    }

    public List<AnnonceurDetailsDto> getDetailedCaWithPayments(LocalDate from, LocalDate to) {
        List<AnnonceurDetailsDto> details = diffusionRepository.findDetailedCaPerSocieteBetween(from, to);
        for (AnnonceurDetailsDto d : details) {
            Long id = d.getAnnonceurId();
            java.math.BigDecimal paid = paiementRepo.sumPaymentsByAnnonceurBetween(id, from, to);
            if (paid == null)
                paid = java.math.BigDecimal.ZERO;
            d.setMontantPaye(paid);
            java.math.BigDecimal montant = d.getMontant() != null ? d.getMontant() : java.math.BigDecimal.ZERO;
            d.setSolde(montant.subtract(paid));
        }
        return details;
    }

    public java.math.BigDecimal getBalanceForAnnonceur(Long annonceurId, LocalDate from, LocalDate to) {
        List<AnnonceurDetailsDto> details = diffusionRepository.findDetailedCaPerSocieteBetween(from, to);
        java.math.BigDecimal montant = java.math.BigDecimal.ZERO;
        for (AnnonceurDetailsDto d : details) {
            if (d.getAnnonceurId() != null && d.getAnnonceurId().equals(annonceurId)) {
                montant = d.getMontant() != null ? d.getMontant() : java.math.BigDecimal.ZERO;
                break;
            }
        }
        java.math.BigDecimal paid = paiementRepo.sumPaymentsByAnnonceurBetween(annonceurId, from, to);
        if (paid == null)
            paid = java.math.BigDecimal.ZERO;
        return montant.subtract(paid);
    }

    // Liste des paiements avec filtre optionnel par annonceur et période
    public List<PaiementAnnonceur> getPayments(Long annonceurId, LocalDate from, LocalDate to) {
        if (annonceurId != null) {
            return paiementRepo.findByAnnonceurIdAndDatePaiementBetween(annonceurId, from, to);
        }
        return paiementRepo.findByDatePaiementBetween(from, to);
    }

    // Total des paiements pour une période
    public BigDecimal getTotalPayments(Long annonceurId, LocalDate from, LocalDate to) {
        List<PaiementAnnonceur> payments = getPayments(annonceurId, from, to);
        return payments.stream()
                .map(PaiementAnnonceur::getMontantPaye)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Liste de tous les annonceurs
    public List<SocietePublicitaire> getAllAnnonceurs() {
        return annonceurRepo.findAll();
    }

    // Sauvegarder un nouveau paiement
    public PaiementAnnonceur savePaiement(Long annonceurId, BigDecimal montantPaye, LocalDate datePaiement) {
        SocietePublicitaire annonceur = annonceurRepo.findById(annonceurId)
                .orElseThrow(() -> new IllegalArgumentException("Annonceur non trouvé: " + annonceurId));

        PaiementAnnonceur paiement = new PaiementAnnonceur();
        paiement.setAnnonceur(annonceur);
        paiement.setMontantPaye(montantPaye);
        paiement.setDatePaiement(datePaiement);

        return paiementRepo.save(paiement);
    }
}
