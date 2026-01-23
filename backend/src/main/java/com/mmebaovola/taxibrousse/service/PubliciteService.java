package com.mmebaovola.taxibrousse.service;

import com.mmebaovola.taxibrousse.dto.AnnonceurDetailsDto;
import com.mmebaovola.taxibrousse.dto.AnnonceurProrataDto;
import com.mmebaovola.taxibrousse.dto.CaResponseDto;
import com.mmebaovola.taxibrousse.dto.DiffusionProrataDto;
import com.mmebaovola.taxibrousse.dto.SocieteCaDto;
import com.mmebaovola.taxibrousse.entity.PaiementAnnonceur;
import com.mmebaovola.taxibrousse.entity.SocietePublicitaire;
import com.mmebaovola.taxibrousse.repository.DetailsDiffusionRepository;
import com.mmebaovola.taxibrousse.repository.DetailsDiffusionRepository.DiffusionDetailView;
import com.mmebaovola.taxibrousse.repository.DiffusionPublicitaireRepository;
import com.mmebaovola.taxibrousse.repository.DiffusionPublicitaireRepository.AnnonceurDetailsCaView;
import com.mmebaovola.taxibrousse.repository.PaiementAnnonceurRepository;
import com.mmebaovola.taxibrousse.repository.SocieteCaProjection;
import com.mmebaovola.taxibrousse.repository.SocietePublicitaireRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PubliciteService {

    private final DiffusionPublicitaireRepository diffusionRepository;
    private final DetailsDiffusionRepository detailsDiffusionRepository;
    private final PaiementAnnonceurRepository paiementRepo;
    private final SocietePublicitaireRepository annonceurRepo;

    public PubliciteService(DiffusionPublicitaireRepository diffusionRepository,
            DetailsDiffusionRepository detailsDiffusionRepository,
            PaiementAnnonceurRepository paiementRepo,
            SocietePublicitaireRepository annonceurRepo) {
        this.diffusionRepository = diffusionRepository;
        this.detailsDiffusionRepository = detailsDiffusionRepository;
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
        List<AnnonceurDetailsCaView> views = diffusionRepository.findDetailedCaPerSocieteBetween(from, to);
        List<AnnonceurDetailsDto> details = new ArrayList<>();

        for (AnnonceurDetailsCaView v : views) {
            Double avgPrix = v.getAvgPrix() != null ? v.getAvgPrix().doubleValue() : 0.0;
            AnnonceurDetailsDto dto = new AnnonceurDetailsDto(
                    v.getAnnonceurId(),
                    v.getNom(),
                    v.getCa() != null ? v.getCa() : BigDecimal.ZERO,
                    v.getNbDiffusions() != null ? v.getNbDiffusions() : 0L,
                    avgPrix);

            Long id = dto.getAnnonceurId();
            BigDecimal paid = paiementRepo.sumPaymentsByAnnonceurBetween(id, from, to);
            if (paid == null)
                paid = BigDecimal.ZERO;
            dto.setMontantPaye(paid);
            BigDecimal montant = dto.getMontant() != null ? dto.getMontant() : BigDecimal.ZERO;
            dto.setSolde(montant.subtract(paid));

            details.add(dto);
        }
        return details;
    }

    public BigDecimal getBalanceForAnnonceur(Long annonceurId, LocalDate from, LocalDate to) {
        List<AnnonceurDetailsCaView> views = diffusionRepository.findDetailedCaPerSocieteBetween(from, to);
        BigDecimal montant = BigDecimal.ZERO;
        for (AnnonceurDetailsCaView v : views) {
            if (v.getAnnonceurId() != null && v.getAnnonceurId().equals(annonceurId)) {
                montant = v.getCa() != null ? v.getCa() : BigDecimal.ZERO;
                break;
            }
        }
        BigDecimal paid = paiementRepo.sumPaymentsByAnnonceurBetween(annonceurId, from, to);
        if (paid == null)
            paid = BigDecimal.ZERO;
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

    // ===================== CALCUL PRORATA =====================

    /**
     * Calcule le prorata des paiements pour un annonceur
     * 
     * Le prorata répartit les paiements proportionnellement sur chaque diffusion.
     * Exemple:
     * - Diffusion 1: 300 Ar
     * - Diffusion 2: 600 Ar
     * - Diffusion 3: 100 Ar
     * - Total: 1000 Ar
     * 
     * Si paiement = 400 Ar (40% du total):
     * - Diffusion 1 payée: 300 × 40% = 120 Ar
     * - Diffusion 2 payée: 600 × 40% = 240 Ar
     * - Diffusion 3 payée: 100 × 40% = 40 Ar
     * 
     * @param annonceurId ID de l'annonceur
     * @return Détail du prorata par diffusion
     */
    public AnnonceurProrataDto calculerProrata(Long annonceurId) {
        SocietePublicitaire annonceur = annonceurRepo.findById(annonceurId)
                .orElseThrow(() -> new IllegalArgumentException("Annonceur non trouvé: " + annonceurId));

        // 1. Récupérer toutes les diffusions de cet annonceur
        List<DiffusionDetailView> diffusions = detailsDiffusionRepository
                .findDiffusionDetailsByAnnonceurId(annonceurId);

        // 2. Calculer le total dû (somme de toutes les diffusions)
        BigDecimal totalDu = diffusions.stream()
                .map(d -> d.getMontantTotal() != null ? d.getMontantTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. Récupérer le total payé (somme de tous les paiements)
        BigDecimal totalPaye = paiementRepo.sumAllPaymentsByAnnonceur(annonceurId);
        if (totalPaye == null) {
            totalPaye = BigDecimal.ZERO;
        }

        // 4. Calculer le prorata pour chaque diffusion
        List<DiffusionProrataDto> diffusionsProrata = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (DiffusionDetailView d : diffusions) {
            BigDecimal montantDiffusion = d.getMontantTotal() != null ? d.getMontantTotal() : BigDecimal.ZERO;

            // Calcul du montant payé au prorata pour cette diffusion
            BigDecimal montantPayeProrata = calculerMontantProrataParDiffusion(
                    montantDiffusion, totalDu, totalPaye);

            // Info voyage formatée
            String voyageInfo = d.getTrajetNom() + " (" +
                    (d.getDateDiffusion() != null ? d.getDateDiffusion().format(dtf) : "N/A") + ")";

            DiffusionProrataDto prorataDto = new DiffusionProrataDto(
                    d.getDiffusionId(),
                    d.getVoyageId(),
                    voyageInfo,
                    d.getDateDiffusion(),
                    d.getNbDiffusions(),
                    montantDiffusion,
                    montantPayeProrata);

            diffusionsProrata.add(prorataDto);
        }

        return new AnnonceurProrataDto(
                annonceurId,
                annonceur.getNom(),
                totalDu,
                totalPaye,
                diffusionsProrata);
    }

    /**
     * Calcule le prorata des paiements pour un annonceur sur une période donnée
     */
    public AnnonceurProrataDto calculerProrata(Long annonceurId, LocalDate from, LocalDate to) {
        SocietePublicitaire annonceur = annonceurRepo.findById(annonceurId)
                .orElseThrow(() -> new IllegalArgumentException("Annonceur non trouvé: " + annonceurId));

        // 1. Récupérer les diffusions de cet annonceur dans la période
        List<DiffusionDetailView> diffusions = detailsDiffusionRepository
                .findDiffusionDetailsByAnnonceurIdAndPeriod(annonceurId, from, to);

        // 2. Calculer le total dû pour la période
        BigDecimal totalDu = diffusions.stream()
                .map(d -> d.getMontantTotal() != null ? d.getMontantTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. Récupérer le total payé pour la période
        BigDecimal totalPaye = paiementRepo.sumPaymentsByAnnonceurBetween(annonceurId, from, to);
        if (totalPaye == null) {
            totalPaye = BigDecimal.ZERO;
        }

        // 4. Calculer le prorata pour chaque diffusion
        List<DiffusionProrataDto> diffusionsProrata = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (DiffusionDetailView d : diffusions) {
            BigDecimal montantDiffusion = d.getMontantTotal() != null ? d.getMontantTotal() : BigDecimal.ZERO;

            // Calcul du montant payé au prorata
            BigDecimal montantPayeProrata = calculerMontantProrataParDiffusion(
                    montantDiffusion, totalDu, totalPaye);

            String voyageInfo = d.getTrajetNom() + " (" +
                    (d.getDateDiffusion() != null ? d.getDateDiffusion().format(dtf) : "N/A") + ")";

            DiffusionProrataDto prorataDto = new DiffusionProrataDto(
                    d.getDiffusionId(),
                    d.getVoyageId(),
                    voyageInfo,
                    d.getDateDiffusion(),
                    d.getNbDiffusions(),
                    montantDiffusion,
                    montantPayeProrata);

            diffusionsProrata.add(prorataDto);
        }

        return new AnnonceurProrataDto(
                annonceurId,
                annonceur.getNom(),
                totalDu,
                totalPaye,
                diffusionsProrata);
    }

    /**
     * Calcule le montant payé au prorata pour une diffusion donnée
     * 
     * Formule: montantPayeDiffusion = montantDiffusion × (totalPaye / totalDu)
     * 
     * @param montantDiffusion Montant de la diffusion
     * @param totalDu          Total dû (somme de toutes les diffusions)
     * @param totalPaye        Total payé (somme de tous les paiements)
     * @return Montant payé au prorata pour cette diffusion
     */
    private BigDecimal calculerMontantProrataParDiffusion(BigDecimal montantDiffusion,
            BigDecimal totalDu, BigDecimal totalPaye) {
        if (totalDu == null || totalDu.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (totalPaye == null || totalPaye.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (montantDiffusion == null || montantDiffusion.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        // prorata = montantDiffusion × (totalPaye / totalDu)
        // Limiter le prorata au montant de la diffusion (ne pas dépasser 100%)
        BigDecimal pourcentagePaye = totalPaye.divide(totalDu, 10, RoundingMode.HALF_UP);

        // Si le pourcentage dépasse 100%, on limite à 100% (diffusion totalement payée)
        if (pourcentagePaye.compareTo(BigDecimal.ONE) > 0) {
            return montantDiffusion;
        }

        return montantDiffusion.multiply(pourcentagePaye).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calcule le prorata pour tous les annonceurs
     */
    public List<AnnonceurProrataDto> calculerProrataAllAnnonceurs() {
        List<SocietePublicitaire> annonceurs = annonceurRepo.findAll();
        List<AnnonceurProrataDto> result = new ArrayList<>();

        for (SocietePublicitaire annonceur : annonceurs) {
            AnnonceurProrataDto prorata = calculerProrata(annonceur.getId());
            result.add(prorata);
        }

        return result;
    }

    /**
     * Calcule le prorata pour tous les annonceurs sur une période
     */
    public List<AnnonceurProrataDto> calculerProrataAllAnnonceurs(LocalDate from, LocalDate to) {
        List<SocietePublicitaire> annonceurs = annonceurRepo.findAll();
        List<AnnonceurProrataDto> result = new ArrayList<>();

        for (SocietePublicitaire annonceur : annonceurs) {
            AnnonceurProrataDto prorata = calculerProrata(annonceur.getId(), from, to);
            result.add(prorata);
        }

        return result;
    }
}
