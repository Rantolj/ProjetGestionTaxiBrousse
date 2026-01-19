package com.mmebaovola.taxibrousse.controller;

import com.mmebaovola.taxibrousse.entity.TaxiBrousse;
import com.mmebaovola.taxibrousse.repository.TaxiBrousseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        // If redirected back with a temporary taxi (validation error), do not overwrite
        // it
        if (!model.containsAttribute("taxi")) {
            model.addAttribute("taxi", new TaxiBrousse());
        }
        return "taxibrousses/form";
    }

    @PostMapping("/save")
    public String save(TaxiBrousse taxi, RedirectAttributes redirectAttributes) {
        String disp = taxi.getDispositionPlaces();
        int seats = 0;
        if (disp != null && !disp.isBlank()) {
            for (char c : disp.toCharArray()) {
                char uc = Character.toUpperCase(c);
                if (uc == 'P' || uc == 'S' || uc == 'V' || uc == 'O') {
                    seats++;
                }
            }
        }
        if (taxi.getNbrPlaces() == null || taxi.getNbrPlaces() != seats) {
            taxi.setNbrPlaces(seats);
            redirectAttributes.addFlashAttribute("warningMessage",
                    "Le nombre de places a été ajusté à " + seats + " en fonction de la disposition.");
        }

        // Ensure non-null numeric values so DB defaults are not bypassed with explicit
        // nulls
        if (taxi.getChargeMax() == null) {
            taxi.setChargeMax(0.0);
        }
        if (taxi.getConsommation() == null) {
            taxi.setConsommation(0.0);
        }

        // Basic validation
        if (taxi.getImmatriculation() == null || taxi.getImmatriculation().isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage", "L'immatriculation est requise.");
            // preserve entered values
            redirectAttributes.addFlashAttribute("taxiTemp", taxi);
            return "redirect:/taxibrousses/create";
        }

        taxiBrousseRepository.save(taxi);
        redirectAttributes.addFlashAttribute("successMessage", "Taxi-brousse enregistré avec succès.");
        return "redirect:/taxibrousses";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Modifier Taxi-brousse");
        model.addAttribute("currentPage", "taxibrousses");
        model.addAttribute("taxi", taxiBrousseRepository.findById(id).orElse(new TaxiBrousse()));
        return "taxibrousses/form";
    }
}