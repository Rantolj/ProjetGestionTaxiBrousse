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
    public String save(Paiement paiement) {
        paiementRepository.save(paiement);
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
