package com.mmebaovola.taxibrousse.controller;

import com.mmebaovola.taxibrousse.entity.Personne;
import com.mmebaovola.taxibrousse.repository.PersonneRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/personnes")
public class PersonneController {

    private final PersonneRepository personneRepository;

    public PersonneController(PersonneRepository personneRepository) {
        this.personneRepository = personneRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pageTitle", "Personnes");
        model.addAttribute("currentPage", "personnes");
        model.addAttribute("personnes", personneRepository.findAll());
        return "personnes/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("pageTitle", "Créer Personne");
        model.addAttribute("currentPage", "personnes");
        model.addAttribute("personne", new Personne());
        return "personnes/form";
    }

    @PostMapping("/save")
    public String save(Personne personne) {
        personneRepository.save(personne);
        return "redirect:/personnes";
    }
}