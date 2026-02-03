package com.mmebaovola.taxibrousse.controller;

import com.mmebaovola.taxibrousse.entity.Produit;
import com.mmebaovola.taxibrousse.entity.VenteProduit;
import com.mmebaovola.taxibrousse.service.ProduitService;
import com.mmebaovola.taxibrousse.service.VenteProduitService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ventes-produits")
public class VenteProduitController {

    private final VenteProduitService venteProduitService;
    private final ProduitService produitService;

    public VenteProduitController(VenteProduitService venteProduitService, ProduitService produitService) {
        this.venteProduitService = venteProduitService;
        this.produitService = produitService;
    }

    @GetMapping
    public String list(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Model model) {

        LocalDate today = LocalDate.now();
        LocalDate fromDate;
        LocalDate toDate;

        // Si l'utilisateur a fourni une période, l'utiliser (support des bornes manquantes)
        if (from != null || to != null) {
            fromDate = (from != null) ? from : LocalDate.of(1970, 1, 1);
            toDate = (to != null) ? to : today;
        } else {
            // Par défaut afficher les 90 derniers jours pour couvrir les ventes proches des changements de mois
            fromDate = today.minusDays(90);
            toDate = today;
        }

        List<VenteProduit> ventes = venteProduitService.findByPeriode(fromDate, toDate);

        // Calculs pour chaque vente
        record VenteAvecDetails(VenteProduit vente, BigDecimal montantTotal, BigDecimal montantPaye, BigDecimal resteAPayer) {}
        List<VenteAvecDetails> ventesAvecDetails = ventes.stream()
                .map(v -> new VenteAvecDetails(
                        v,
                        venteProduitService.getMontantTotal(v.getId()),
                        venteProduitService.getMontantPaye(v.getId()),
                        venteProduitService.getResteAPayer(v.getId())
                ))
                .toList();

        BigDecimal totalCA = venteProduitService.getTotalCA(fromDate, toDate);

        model.addAttribute("ventes", ventesAvecDetails);
        model.addAttribute("totalCA", totalCA);
        model.addAttribute("from", fromDate);
        model.addAttribute("to", toDate);
        model.addAttribute("pageTitle", "Ventes Produits Extra");
        model.addAttribute("currentPage", "ventes-produits");
        return "ventes-produits/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        List<Produit> produits = produitService.findAll();

        // Ajouter le prix actuel pour chaque produit
        record ProduitAvecPrix(Produit produit, BigDecimal prixActuel) {}
        List<ProduitAvecPrix> produitsAvecPrix = produits.stream()
                .map(p -> new ProduitAvecPrix(p, produitService.getPrixActuel(p.getId())))
                .toList();

        model.addAttribute("produits", produitsAvecPrix);
        model.addAttribute("dateVente", LocalDate.now());
        model.addAttribute("pageTitle", "Nouvelle Vente");
        model.addAttribute("currentPage", "ventes-produits");
        return "ventes-produits/form";
    }

    @PostMapping("/save")
    public String save(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateVente,
            @RequestParam("produitId") List<Long> produitIds,
            @RequestParam("quantite") List<Integer> quantites,
            RedirectAttributes redirectAttributes) {

        List<VenteProduitService.VenteDetailDTO> details = new ArrayList<>();
        for (int i = 0; i < produitIds.size(); i++) {
            if (produitIds.get(i) != null && quantites.get(i) != null && quantites.get(i) > 0) {
                details.add(new VenteProduitService.VenteDetailDTO(produitIds.get(i), quantites.get(i)));
            }
        }

        if (details.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Veuillez ajouter au moins un produit.");
            return "redirect:/ventes-produits/new";
        }

        venteProduitService.createVente(dateVente, details);
        redirectAttributes.addFlashAttribute("success", "Vente enregistrée avec succès!");
        return "redirect:/ventes-produits";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        VenteProduit vente = venteProduitService.findById(id)
                .orElseThrow(() -> new RuntimeException("Vente non trouvée"));

        BigDecimal montantTotal = venteProduitService.getMontantTotal(id);
        BigDecimal montantPaye = venteProduitService.getMontantPaye(id);
        BigDecimal resteAPayer = venteProduitService.getResteAPayer(id);

        model.addAttribute("vente", vente);
        model.addAttribute("montantTotal", montantTotal);
        model.addAttribute("montantPaye", montantPaye);
        model.addAttribute("resteAPayer", resteAPayer);
        model.addAttribute("pageTitle", "Détails Vente #" + id);
        model.addAttribute("currentPage", "ventes-produits");
        return "ventes-produits/view";
    }

    @PostMapping("/paiement/{venteId}")
    public String enregistrerPaiement(
            @PathVariable Long venteId,
            @RequestParam BigDecimal montant,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datePaiement,
            RedirectAttributes redirectAttributes) {

        venteProduitService.enregistrerPaiement(venteId, montant, datePaiement);
        redirectAttributes.addFlashAttribute("success", "Paiement enregistré!");
        return "redirect:/ventes-produits/view/" + venteId;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            venteProduitService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Vente supprimée!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression.");
        }
        return "redirect:/ventes-produits";
    }

    /**
     * Rapport CA des produits par mois
     */
    @GetMapping("/rapport")
    public String rapport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Model model) {

        LocalDate today = LocalDate.now();
        LocalDate fromDate = (from != null) ? from : today.minusMonths(6);
        LocalDate toDate = (to != null) ? to : today;

        var caParMois = venteProduitService.getCAParMois(fromDate, toDate);
        var caParProduit = venteProduitService.getCAParProduit(fromDate, toDate);
        BigDecimal totalCA = venteProduitService.getTotalCA(fromDate, toDate);

        model.addAttribute("caParMois", caParMois);
        model.addAttribute("caParProduit", caParProduit);
        model.addAttribute("totalCA", totalCA);
        model.addAttribute("from", fromDate);
        model.addAttribute("to", toDate);
        model.addAttribute("pageTitle", "Rapport CA Produits");
        model.addAttribute("currentPage", "ventes-produits");
        return "ventes-produits/rapport";
    }
}
