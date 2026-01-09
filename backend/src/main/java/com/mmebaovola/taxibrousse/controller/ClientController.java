package com.mmebaovola.taxibrousse.controller;

import com.mmebaovola.taxibrousse.entity.Client;
import com.mmebaovola.taxibrousse.repository.ClientRepository;
import com.mmebaovola.taxibrousse.repository.PersonneRepository;
import com.mmebaovola.taxibrousse.repository.ReservationRepository;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private final ClientRepository clientRepository;
    private final PersonneRepository personneRepository;
    private final ReservationRepository reservationRepository;

    public ClientController(ClientRepository clientRepository, PersonneRepository personneRepository,
            ReservationRepository reservationRepository) {
        this.clientRepository = clientRepository;
        this.personneRepository = personneRepository;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pageTitle", "Clients");
        model.addAttribute("currentPage", "clients");
        model.addAttribute("clients", clientRepository.findAll());
        return "clients/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("pageTitle", "Créer Client");
        model.addAttribute("currentPage", "clients");
        model.addAttribute("client", new Client());
        model.addAttribute("personnes", personneRepository.findAll());
        return "clients/form";
    }

    @PostMapping("/save")
    public String save(Client client) {
        clientRepository.save(client);
        return "redirect:/clients";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Modifier Client");
        model.addAttribute("currentPage", "clients");
        model.addAttribute("client", clientRepository.findById(id).orElse(new Client()));
        model.addAttribute("personnes", personneRepository.findAll());
        return "clients/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (reservationRepository.existsByClientId(id)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Impossible de supprimer ce client : il existe des réservations associées.");
        } else {
            clientRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Client supprimé avec succès.");
        }
        return "redirect:/clients";
    }
}
