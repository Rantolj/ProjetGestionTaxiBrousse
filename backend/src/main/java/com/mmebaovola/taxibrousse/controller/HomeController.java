package com.mmebaovola.taxibrousse.controller;

import com.mmebaovola.taxibrousse.repository.PersonneRepository;
import com.mmebaovola.taxibrousse.repository.VoyageRepository;
import com.mmebaovola.taxibrousse.repository.ReservationRepository;
import com.mmebaovola.taxibrousse.repository.TaxiBrousseRepository;
import com.mmebaovola.taxibrousse.repository.ClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final VoyageRepository voyageRepository;
    private final ReservationRepository reservationRepository;
    private final TaxiBrousseRepository taxiBrousseRepository;
    private final ClientRepository clientRepository;

    public HomeController(VoyageRepository voyageRepository,
            ReservationRepository reservationRepository,
            TaxiBrousseRepository taxiBrousseRepository,
            ClientRepository clientRepository) {
        this.voyageRepository = voyageRepository;
        this.reservationRepository = reservationRepository;
        this.taxiBrousseRepository = taxiBrousseRepository;
        this.clientRepository = clientRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("pageTitle", "Tableau de bord");
        model.addAttribute("currentPage", "dashboard");
        model.addAttribute("voyagesCount", voyageRepository.count());
        model.addAttribute("reservationsCount", reservationRepository.count());
        model.addAttribute("taxis", taxiBrousseRepository.count());
        model.addAttribute("clientsCount", clientRepository.count());
        return "index";
    }
}
