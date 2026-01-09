package com.mmebaovola.taxibrousse.controller;

import com.mmebaovola.taxibrousse.entity.Trajet;
import com.mmebaovola.taxibrousse.repository.TrajetRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/trajets")
public class TrajetController {

    private final TrajetRepository trajetRepository;

    public TrajetController(TrajetRepository trajetRepository) {
        this.trajetRepository = trajetRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pageTitle", "Trajets");
        model.addAttribute("currentPage", "trajets");
        model.addAttribute("trajets", trajetRepository.findAll());
        return "trajets/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("pageTitle", "Créer Trajet");
        model.addAttribute("currentPage", "trajets");
        model.addAttribute("trajet", new Trajet());
        return "trajets/form";
    }

    @PostMapping("/save")
    public String save(Trajet trajet) {
        trajetRepository.save(trajet);
        return "redirect:/trajets";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Modifier Trajet");
        model.addAttribute("currentPage", "trajets");
        model.addAttribute("trajet", trajetRepository.findById(id).orElse(new Trajet()));
        return "trajets/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        trajetRepository.deleteById(id);
        return "redirect:/trajets";
    }
}
