package com.mmebaovola.taxibrousse.controller;

import com.mmebaovola.taxibrousse.entity.Personne;
import com.mmebaovola.taxibrousse.repository.PersonneRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequestMapping("/personnes")
public class PersonneController {

    private final PersonneRepository personneRepository;

    public PersonneController(PersonneRepository personneRepository) {
        this.personneRepository = personneRepository;
    }

    @GetMapping
    public String list(Model model, @RequestParam(name = "q", required = false) String q) {
        model.addAttribute("pageTitle", "Personnes");
        model.addAttribute("currentPage", "personnes");

        List<Personne> personnes = personneRepository.findAll();
        if (q != null && !q.isBlank()) {
            String search = q.toLowerCase();
            personnes = personnes.stream()
                    .filter(p -> (p.getNom() != null && p.getNom().toLowerCase().contains(search)) ||
                            (p.getPrenom() != null && p.getPrenom().toLowerCase().contains(search)) ||
                            (p.getContact() != null && p.getContact().toLowerCase().contains(search)))
                    .toList();
        }

        model.addAttribute("q", q);
        model.addAttribute("personnes", personnes);
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

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Modifier Personne");
        model.addAttribute("currentPage", "personnes");
        model.addAttribute("personne", personneRepository.findById(id).orElse(new Personne()));
        return "personnes/form";
    }
}