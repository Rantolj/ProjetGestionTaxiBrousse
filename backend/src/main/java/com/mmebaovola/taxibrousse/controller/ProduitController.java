package com.mmebaovola.taxibrousse.controller;

import com.mmebaovola.taxibrousse.entity.Produit;
import com.mmebaovola.taxibrousse.entity.PrixProduit;
import com.mmebaovola.taxibrousse.service.ProduitService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/produits")
public class ProduitController {

    private final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @GetMapping
    public String list(Model model) {
        List<Produit> produits = produitService.findAll();

        // Ajouter le prix actuel pour chaque produit
        record ProduitAvecPrix(Produit produit, BigDecimal prixActuel) {}
        List<ProduitAvecPrix> produitsAvecPrix = produits.stream()
                .map(p -> new ProduitAvecPrix(p, produitService.getPrixActuel(p.getId())))
                .toList();

        model.addAttribute("produits", produitsAvecPrix);
        model.addAttribute("pageTitle", "Produits Extra");
        model.addAttribute("currentPage", "produits");
        return "produits/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("produit", new Produit());
        model.addAttribute("pageTitle", "Nouveau Produit");
        model.addAttribute("currentPage", "produits");
        return "produits/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam String libelle,
                       @RequestParam BigDecimal prix,
                       RedirectAttributes redirectAttributes) {
        produitService.createWithPrice(libelle, prix);
        redirectAttributes.addFlashAttribute("success", "Produit créé avec succès!");
        return "redirect:/produits";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Produit produit = produitService.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        BigDecimal prixActuel = produitService.getPrixActuel(id);
        List<PrixProduit> historiquePrix = produitService.getHistoriquePrix(id);

        model.addAttribute("produit", produit);
        model.addAttribute("prixActuel", prixActuel);
        model.addAttribute("historiquePrix", historiquePrix);
        model.addAttribute("pageTitle", "Modifier Produit");
        model.addAttribute("currentPage", "produits");
        return "produits/form";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String libelle,
                         @RequestParam(required = false) BigDecimal nouveauPrix,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEffective,
                         RedirectAttributes redirectAttributes) {
        Produit produit = produitService.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        produit.setLibelle(libelle);
        produitService.save(produit);

        if (nouveauPrix != null) {
            produitService.updatePrice(id, nouveauPrix, dateEffective);
        }

        redirectAttributes.addFlashAttribute("success", "Produit mis à jour!");
        return "redirect:/produits";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            produitService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Produit supprimé!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Impossible de supprimer ce produit (ventes existantes).");
        }
        return "redirect:/produits";
    }
}
