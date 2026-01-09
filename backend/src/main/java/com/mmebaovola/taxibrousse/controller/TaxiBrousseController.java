package com.mmebaovola.taxibrousse.controller;

import com.mmebaovola.taxibrousse.entity.TaxiBrousse;
import com.mmebaovola.taxibrousse.repository.TaxiBrousseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/taxibrousses")
public class TaxiBrousseController {

    private final TaxiBrousseRepository taxiBrousseRepository;

    public TaxiBrousseController(TaxiBrousseRepository taxiBrousseRepository) {
        this.taxiBrousseRepository = taxiBrousseRepository;
    }

    @GetMapping
    public String list(Model model, @RequestParam(name = "q", required = false) String q) {
        List<TaxiBrousse> taxis = taxiBrousseRepository.findAll();

        if (q != null && !q.trim().isEmpty()) {
            String query = q.toLowerCase();
            taxis = taxis.stream()
                    .filter(t -> {
                        String immatriculation = t.getImmatriculation() != null
                                ? t.getImmatriculation().toLowerCase()
                                : "";
                

                        return immatriculation.contains(query);
                    })
                    .toList();
        }

        model.addAttribute("pageTitle", "Taxi-brousses");
        model.addAttribute("currentPage", "taxibrousses");
        model.addAttribute("taxis", taxis);
        model.addAttribute("q", q);
        return "taxibrousses/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("pageTitle", "Créer Taxi-brousse");
        model.addAttribute("currentPage", "taxibrousses");
        model.addAttribute("taxi", new TaxiBrousse());
        return "taxibrousses/form";
    }

    @PostMapping("/save")
    public String save(TaxiBrousse taxi) {
        taxiBrousseRepository.save(taxi);
        return "redirect:/taxibrousses";
    }
}