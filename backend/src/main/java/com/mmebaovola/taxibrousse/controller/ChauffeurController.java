package com.mmebaovola.taxibrousse.controller;

import com.mmebaovola.taxibrousse.entity.Chauffeur;
import com.mmebaovola.taxibrousse.repository.ChauffeurRepository;
import com.mmebaovola.taxibrousse.repository.PersonneRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/chauffeurs")
public class ChauffeurController {

    private final ChauffeurRepository chauffeurRepository;
    private final PersonneRepository personneRepository;

    public ChauffeurController(ChauffeurRepository chauffeurRepository, PersonneRepository personneRepository) {
        this.chauffeurRepository = chauffeurRepository;
        this.personneRepository = personneRepository;
    }

    @GetMapping
    public String list(Model model, @RequestParam(name = "q", required = false) String q) {
        model.addAttribute("pageTitle", "Chauffeurs");
        model.addAttribute("currentPage", "chauffeurs");

        List<Chauffeur> chauffeurs = chauffeurRepository.findAll();
        if (q != null && !q.isBlank()) {
            String search = q.toLowerCase();
            chauffeurs = chauffeurs.stream()
                    .filter(c -> c.getPersonne() != null && ((c.getPersonne().getNom() != null
                            && c.getPersonne().getNom().toLowerCase().contains(search)) ||
                            (c.getPersonne().getPrenom() != null
                                    && c.getPersonne().getPrenom().toLowerCase().contains(search))
                            ||
                            (c.getPersonne().getContact() != null
                                    && c.getPersonne().getContact().toLowerCase().contains(search))))
                    .toList();
        }

        model.addAttribute("q", q);
        model.addAttribute("chauffeurs", chauffeurs);
        return "chauffeurs/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("pageTitle", "Créer Chauffeur");
        model.addAttribute("currentPage", "chauffeurs");
        model.addAttribute("chauffeur", new Chauffeur());
        model.addAttribute("personnes", personneRepository.findAll());
        return "chauffeurs/form";
    }

    @PostMapping("/save")
    public String save(Chauffeur chauffeur) {
        chauffeurRepository.save(chauffeur);
        return "redirect:/chauffeurs";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Modifier Chauffeur");
        model.addAttribute("currentPage", "chauffeurs");
        model.addAttribute("chauffeur", chauffeurRepository.findById(id).orElse(new Chauffeur()));
        model.addAttribute("personnes", personneRepository.findAll());
        return "chauffeurs/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        chauffeurRepository.deleteById(id);
        return "redirect:/chauffeurs";
    }
}
