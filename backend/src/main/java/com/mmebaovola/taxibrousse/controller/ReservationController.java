package com.mmebaovola.taxibrousse.controller;

import com.mmebaovola.taxibrousse.entity.Reservation;
import com.mmebaovola.taxibrousse.repository.ClientRepository;
import com.mmebaovola.taxibrousse.repository.ReservationRepository;
import com.mmebaovola.taxibrousse.repository.VoyageRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final VoyageRepository voyageRepository;
    private final ClientRepository clientRepository;

    public ReservationController(ReservationRepository reservationRepository, VoyageRepository voyageRepository,
            ClientRepository clientRepository) {
        this.reservationRepository = reservationRepository;
        this.voyageRepository = voyageRepository;
        this.clientRepository = clientRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pageTitle", "Réservations");
        model.addAttribute("currentPage", "reservations");
        model.addAttribute("reservations", reservationRepository.findAll());
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
    public String delete(@PathVariable Long id) {
        reservationRepository.deleteById(id);
        return "redirect:/reservations";
    }
}
