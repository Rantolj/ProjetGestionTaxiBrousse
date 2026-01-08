package com.mmebaovola.taxibrousse.controller;

import com.mmebaovola.taxibrousse.entity.Voyage;
import com.mmebaovola.taxibrousse.repository.VoyageRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/voyages")
public class VoyageController {

    private final VoyageRepository voyageRepository;

    public VoyageController(VoyageRepository voyageRepository) {
        this.voyageRepository = voyageRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("voyages", voyageRepository.findAll());
        return "voyages/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("voyage", new Voyage());
        return "voyages/form";
    }

    @PostMapping("/save")
    public String save(Voyage voyage) {
        voyageRepository.save(voyage);
        return "redirect:/voyages";
    }
}