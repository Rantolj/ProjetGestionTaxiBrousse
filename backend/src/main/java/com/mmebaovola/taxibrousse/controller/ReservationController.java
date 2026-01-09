package com.mmebaovola.taxibrousse.controller;

import com.mmebaovola.taxibrousse.entity.Reservation;
import com.mmebaovola.taxibrousse.repository.ClientRepository;
import com.mmebaovola.taxibrousse.repository.ReservationRepository;
import com.mmebaovola.taxibrousse.repository.VoyageRepository;
import com.mmebaovola.taxibrousse.repository.DetailsReservationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final VoyageRepository voyageRepository;
    private final ClientRepository clientRepository;
    private final DetailsReservationRepository detailsReservationRepository;

    public ReservationController(ReservationRepository reservationRepository, VoyageRepository voyageRepository,
            ClientRepository clientRepository, DetailsReservationRepository detailsReservationRepository) {
        this.reservationRepository = reservationRepository;
        this.voyageRepository = voyageRepository;
        this.clientRepository = clientRepository;
        this.detailsReservationRepository = detailsReservationRepository;
    }

        @GetMapping
        public String list(Model model, @RequestParam(name = "q", required = false) String q) {
        List<Reservation> reservations = reservationRepository.findAll();

        if (q != null && !q.trim().isEmpty()) {
            String query = q.toLowerCase();
            reservations = reservations.stream()
                .filter(r -> {
                String clientNom = r.getClient() != null && r.getClient().getPersonne() != null
                    && r.getClient().getPersonne().getNom() != null
                        ? r.getClient().getPersonne().getNom().toLowerCase()
                        : "";
                String clientPrenom = r.getClient() != null && r.getClient().getPersonne() != null
                    && r.getClient().getPersonne().getPrenom() != null
                        ? r.getClient().getPersonne().getPrenom().toLowerCase()
                        : "";
                String trajetNom = r.getVoyage() != null && r.getVoyage().getTrajet() != null
                    && r.getVoyage().getTrajet().getNom() != null
                        ? r.getVoyage().getTrajet().getNom().toLowerCase()
                        : "";

                return clientNom.contains(query)
                    || clientPrenom.contains(query)
                    || trajetNom.contains(query);
                })
                .toList();
        }

        model.addAttribute("pageTitle", "Réservations");
        model.addAttribute("currentPage", "reservations");
        model.addAttribute("reservations", reservations);
        model.addAttribute("q", q);
        return "reservations/list";
        }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("pageTitle", "Créer Réservation");
        model.addAttribute("currentPage", "reservations");
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("voyages", voyageRepository.findAll());
        model.addAttribute("clients", clientRepository.findAll());
        return "reservations/form";
    }

    @PostMapping("/save")
    public String save(Reservation reservation) {
        reservationRepository.save(reservation);
        return "redirect:/reservations";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Modifier Réservation");
        model.addAttribute("currentPage", "reservations");
        model.addAttribute("reservation", reservationRepository.findById(id).orElse(new Reservation()));
        model.addAttribute("voyages", voyageRepository.findAll());
        model.addAttribute("clients", clientRepository.findAll());
        return "reservations/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        if (detailsReservationRepository.existsByReservationId(id)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Impossible de supprimer cette réservation : elle contient des détails liés.");
        } else {
            reservationRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Réservation supprimée avec succès.");
        }
        return "redirect:/reservations";
    }
}
