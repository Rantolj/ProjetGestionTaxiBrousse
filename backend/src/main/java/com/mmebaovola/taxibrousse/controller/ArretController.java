package com.mmebaovola.taxibrousse.controller;

import com.mmebaovola.taxibrousse.entity.Arret;
import com.mmebaovola.taxibrousse.repository.ArretRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/arrets")
public class ArretController {

    private final ArretRepository arretRepository;

    public ArretController(ArretRepository arretRepository) {
        this.arretRepository = arretRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pageTitle", "Arrêts");
        model.addAttribute("currentPage", "arrets");
        model.addAttribute("arrets", arretRepository.findAll());
        return "arrets/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("pageTitle", "Créer Arrêt");
        model.addAttribute("currentPage", "arrets");
        model.addAttribute("arret", new Arret());
        return "arrets/form";
    }

    @PostMapping("/save")
    public String save(Arret arret) {
        arretRepository.save(arret);
        return "redirect:/arrets";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Modifier Arrêt");
        model.addAttribute("currentPage", "arrets");
        model.addAttribute("arret", arretRepository.findById(id).orElse(new Arret()));
        return "arrets/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        arretRepository.deleteById(id);
        return "redirect:/arrets";
    }
}
