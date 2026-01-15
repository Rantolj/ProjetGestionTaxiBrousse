package com.mmebaovola.taxibrousse.controller;

import com.mmebaovola.taxibrousse.entity.Paiement;
import com.mmebaovola.taxibrousse.repository.PaiementRepository;
import com.mmebaovola.taxibrousse.repository.ReservationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/paiements")
public class PaiementController {

    private final PaiementRepository paiementRepository;
    private final ReservationRepository reservationRepository;

    public PaiementController(PaiementRepository paiementRepository, ReservationRepository reservationRepository) {
        this.paiementRepository = paiementRepository;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping
    public String list(Model model, @RequestParam(name = "q", required = false) String q) {
        List<Paiement> paiements = paiementRepository.findAll();

        if (q != null && !q.trim().isEmpty()) {
            String query = q.toLowerCase();
            paiements = paiements.stream()
                    .filter(p -> {
                        String clientNom = p.getReservation() != null && p.getReservation().getClient() != null
                                && p.getReservation().getClient().getPersonne() != null
                                && p.getReservation().getClient().getPersonne().getNom() != null
                                        ? p.getReservation().getClient().getPersonne().getNom().toLowerCase()
                                        : "";
                        String clientPrenom = p.getReservation() != null && p.getReservation().getClient() != null
                                && p.getReservation().getClient().getPersonne() != null
                                && p.getReservation().getClient().getPersonne().getPrenom() != null
                                        ? p.getReservation().getClient().getPersonne().getPrenom().toLowerCase()
                                        : "";

                        return clientNom.contains(query)
                                || clientPrenom.contains(query);
                    })
                    .toList();
        }

        model.addAttribute("pageTitle", "Paiements");
        model.addAttribute("currentPage", "paiements");
        model.addAttribute("paiements", paiements);
        model.addAttribute("q", q);
        return "paiements/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("pageTitle", "Créer Paiement");
        model.addAttribute("currentPage", "paiements");
        model.addAttribute("paiement", new Paiement());
        model.addAttribute("reservations", reservationRepository.findAll());
        return "paiements/form";
    }

    @PostMapping("/save")
    public String save(Paiement paiement,
            @RequestParam(name = "datePaiementStr", required = false) String datePaiementStr,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        // Parse date string (format attendu: yyyy-MM-dd'T'HH:mm)
        java.time.LocalDateTime paiementDate = null;
        try {
            if (datePaiementStr != null && !datePaiementStr.isBlank()) {
                paiementDate = java.time.LocalDateTime.parse(datePaiementStr);
                paiement.setDatePaiement(paiementDate);
            }
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Format de date invalide. Utilisez une date/heure valide.");
            return "redirect:/paiements/create";
        }

        // Vérifications métier
        if (paiement.getReservation() == null || paiement.getReservation().getId() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Veuillez sélectionner une réservation valide.");
            return "redirect:/paiements/create";
        }

        var reservation = reservationRepository.findById(paiement.getReservation().getId()).orElse(null);
        if (reservation == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Réservation introuvable.");
            return "redirect:/paiements/create";
        }

        // Somme des paiements déjà effectués
        Double dejaPaye = paiementRepository.sumMontantByReservationId(reservation.getId());
        if (dejaPaye == null)
            dejaPaye = 0.0;

        double montantTotal = reservation.getMontantTotal() != null ? reservation.getMontantTotal() : 0.0;
        double nouveauTotal = dejaPaye + (paiement.getMontantPaye() != null ? paiement.getMontantPaye() : 0.0);

        if (dejaPaye >= montantTotal) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cette réservation est déjà entièrement payée.");
            return "redirect:/paiements/create";
        }

        if (nouveauTotal > montantTotal) {
            redirectAttributes.addFlashAttribute("errorMessage", "Le montant payé dépasse le total de la réservation.");
            return "redirect:/paiements/create";
        }

        // Vérifier que la date de paiement est à la date de départ ou avant
        if (paiementDate != null && reservation.getVoyage() != null
                && reservation.getVoyage().getDateDepart() != null) {
            if (paiementDate.isAfter(reservation.getVoyage().getDateDepart())) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "La date de paiement doit être le jour du départ ou avant.");
                return "redirect:/paiements/create";
            }
        }

        paiementRepository.save(paiement);
        redirectAttributes.addFlashAttribute("successMessage", "Paiement enregistré.");
        return "redirect:/paiements";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Modifier Paiement");
        model.addAttribute("currentPage", "paiements");
        model.addAttribute("paiement", paiementRepository.findById(id).orElse(new Paiement()));
        model.addAttribute("reservations", reservationRepository.findAll());
        return "paiements/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        paiementRepository.deleteById(id);
        return "redirect:/paiements";
    }
}
