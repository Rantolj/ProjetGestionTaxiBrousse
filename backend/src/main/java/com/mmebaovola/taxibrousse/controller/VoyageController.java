package com.mmebaovola.taxibrousse.controller;

import com.mmebaovola.taxibrousse.entity.Voyage;
import com.mmebaovola.taxibrousse.repository.VoyageRepository;
import com.mmebaovola.taxibrousse.repository.TrajetRepository;
import com.mmebaovola.taxibrousse.repository.ChauffeurRepository;
import com.mmebaovola.taxibrousse.repository.TaxiBrousseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mmebaovola.taxibrousse.entity.Voyage;
import com.mmebaovola.taxibrousse.service.NotificationService;

import java.util.List;

@Controller
@RequestMapping("/voyages")
public class VoyageController {

    private final VoyageRepository voyageRepository;
    private final TrajetRepository trajetRepository;
    private final ChauffeurRepository chauffeurRepository;
    private final TaxiBrousseRepository taxiBrousseRepository;
    private final NotificationService notificationService;

    public VoyageController(VoyageRepository voyageRepository,
            TrajetRepository trajetRepository,
            ChauffeurRepository chauffeurRepository,
            TaxiBrousseRepository taxiBrousseRepository,
            NotificationService notificationService) {
        this.voyageRepository = voyageRepository;
        this.trajetRepository = trajetRepository;
        this.chauffeurRepository = chauffeurRepository;
        this.taxiBrousseRepository = taxiBrousseRepository;
        this.notificationService = notificationService;
    }

    @GetMapping
    public String list(Model model, @RequestParam(name = "q", required = false) String q,
                       @RequestParam(name = "status", required = false) String status) {
        List<Voyage> voyages = voyageRepository.findAll();

        if (q != null && !q.trim().isEmpty()) {
            String query = q.toLowerCase();
            voyages = voyages.stream()
                    .filter(v -> {
                        String trajetNom = v.getTrajet() != null && v.getTrajet().getNom() != null
                                ? v.getTrajet().getNom().toLowerCase()
                                : "";
                        String chauffeurNom = v.getChauffeur() != null && v.getChauffeur().getPersonne() != null
                                && v.getChauffeur().getPersonne().getNom() != null
                                        ? v.getChauffeur().getPersonne().getNom().toLowerCase()
                                        : "";
                        String chauffeurPrenom = v.getChauffeur() != null && v.getChauffeur().getPersonne() != null
                                && v.getChauffeur().getPersonne().getPrenom() != null
                                        ? v.getChauffeur().getPersonne().getPrenom().toLowerCase()
                                        : "";
                        String immatriculation = v.getTaxiBrousse() != null
                                && v.getTaxiBrousse().getImmatriculation() != null
                                        ? v.getTaxiBrousse().getImmatriculation().toLowerCase()
                                        : "";

                        return trajetNom.contains(query)
                                || chauffeurNom.contains(query)
                                || chauffeurPrenom.contains(query)
                                || immatriculation.contains(query);
                    })
                    .toList();
        }

        if (status != null && !status.isBlank()) {
            voyages = voyages.stream()
                    .filter(v -> v.getStatus() != null && v.getStatus().name().equalsIgnoreCase(status))
                    .toList();
        }

        model.addAttribute("pageTitle", "Voyages");
        model.addAttribute("currentPage", "voyages");
        model.addAttribute("voyages", voyages);
        model.addAttribute("q", q);
        model.addAttribute("status", status);
        model.addAttribute("statuses", com.mmebaovola.taxibrousse.entity.VoyageStatus.values());
        return "voyages/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("pageTitle", "Créer Voyage");
        model.addAttribute("currentPage", "voyages");
        model.addAttribute("voyage", new Voyage());
        model.addAttribute("trajets", trajetRepository.findAll());
        model.addAttribute("chauffeurs", chauffeurRepository.findAll());
        model.addAttribute("taxibrousses", taxiBrousseRepository.findAll());
        return "voyages/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("voyage") Voyage voyage,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (voyage.getDateDepart() != null) {
            // Vérifier conflit taxi au même moment
            if (voyage.getTaxiBrousse() != null && voyage.getTaxiBrousse().getId() != null) {
                var voyagesTaxi = voyageRepository
                        .findByTaxiBrousse_IdAndDateDepart(voyage.getTaxiBrousse().getId(), voyage.getDateDepart());
                boolean conflitTaxi = voyagesTaxi.stream()
                        .anyMatch(v -> voyage.getId() == null || !v.getId().equals(voyage.getId()));
                if (conflitTaxi) {
                    bindingResult.reject("conflitTaxi",
                            "Ce taxi-brousse est déjà planifié pour un voyage à cette date/heure.");
                }
            }

            // Vérifier conflit chauffeur au même moment
            if (voyage.getChauffeur() != null && voyage.getChauffeur().getId() != null) {
                var voyagesChauffeur = voyageRepository
                        .findByChauffeur_IdAndDateDepart(voyage.getChauffeur().getId(), voyage.getDateDepart());
                boolean conflitChauffeur = voyagesChauffeur.stream()
                        .anyMatch(v -> voyage.getId() == null || !v.getId().equals(voyage.getId()));
                if (conflitChauffeur) {
                    bindingResult.reject("conflitChauffeur",
                            "Ce chauffeur est déjà planifié pour un voyage à cette date/heure.");
                }
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Créer Voyage");
            model.addAttribute("currentPage", "voyages");
            model.addAttribute("trajets", trajetRepository.findAll());
            model.addAttribute("chauffeurs", chauffeurRepository.findAll());
            model.addAttribute("taxibrousses", taxiBrousseRepository.findAll());
            return "voyages/form";
        }

        voyageRepository.save(voyage);
        redirectAttributes.addFlashAttribute("successMessage", "Voyage enregistré avec succès.");
        return "redirect:/voyages";
    }

    @PostMapping("/{id}/status")
    public String changeStatus(@PathVariable Long id, @RequestParam String status,
                               org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        var opt = voyageRepository.findById(id);
        if (opt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Voyage introuvable.");
            return "redirect:/voyages";
        }
        Voyage v = opt.get();
        String old = v.getStatus() != null ? v.getStatus().name() : "";
        try {
            com.mmebaovola.taxibrousse.entity.VoyageStatus ns = com.mmebaovola.taxibrousse.entity.VoyageStatus.valueOf(status);
            v.setStatus(ns);
            voyageRepository.save(v);
            // create notifications
            notificationService.notifyVoyageStatusChange(v, old, ns.name());
            redirectAttributes.addFlashAttribute("successMessage", "Statut mis à jour.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Statut invalide.");
        }
        return "redirect:/voyages";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Modifier Voyage");
        model.addAttribute("currentPage", "voyages");
        model.addAttribute("voyage", voyageRepository.findById(id).orElse(new Voyage()));
        model.addAttribute("trajets", trajetRepository.findAll());
        model.addAttribute("chauffeurs", chauffeurRepository.findAll());
        model.addAttribute("taxibrousses", taxiBrousseRepository.findAll());
        return "voyages/form";
    }
}