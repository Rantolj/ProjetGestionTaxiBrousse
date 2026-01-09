package com.mmebaovola.taxibrousse.controller;

import com.mmebaovola.taxibrousse.entity.Voyage;
import com.mmebaovola.taxibrousse.repository.VoyageRepository;
import com.mmebaovola.taxibrousse.repository.TrajetRepository;
import com.mmebaovola.taxibrousse.repository.ChauffeurRepository;
import com.mmebaovola.taxibrousse.repository.TaxiBrousseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/voyages")
public class VoyageController {

    private final VoyageRepository voyageRepository;
    private final TrajetRepository trajetRepository;
    private final ChauffeurRepository chauffeurRepository;
    private final TaxiBrousseRepository taxiBrousseRepository;

    public VoyageController(VoyageRepository voyageRepository,
            TrajetRepository trajetRepository,
            ChauffeurRepository chauffeurRepository,
            TaxiBrousseRepository taxiBrousseRepository) {
        this.voyageRepository = voyageRepository;
        this.trajetRepository = trajetRepository;
        this.chauffeurRepository = chauffeurRepository;
        this.taxiBrousseRepository = taxiBrousseRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pageTitle", "Voyages");
        model.addAttribute("currentPage", "voyages");
        model.addAttribute("voyages", voyageRepository.findAll());
        return "voyages/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("pageTitle", "Créer Voyage");
        model.addAttribute("currentPage", "voyages");
        model.addAttribute("voyage", new Voyage());
        model.addAttribute("trajets", trajetRepository.findAll());
        model.addAttribute("chauffeurs", chauffeurRepository.findAll());
        model.addAttribute("taxibrousses", taxiBrousseRepository.findAll());
        return "voyages/form";
    }

    @PostMapping("/save")
    public String save(Voyage voyage) {
        voyageRepository.save(voyage);
        return "redirect:/voyages";
    }
}