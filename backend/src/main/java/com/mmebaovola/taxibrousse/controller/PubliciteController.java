package com.mmebaovola.taxibrousse.controller;

import com.mmebaovola.taxibrousse.dto.AnnonceurDetailsDto;
import com.mmebaovola.taxibrousse.dto.CaResponseDto;
import com.mmebaovola.taxibrousse.entity.PaiementAnnonceur;
import com.mmebaovola.taxibrousse.entity.SocietePublicitaire;
import com.mmebaovola.taxibrousse.service.PubliciteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/publicite")
public class PubliciteController {

	private final PubliciteService publiciteService;

	public PubliciteController(PubliciteService publiciteService) {
		this.publiciteService = publiciteService;
	}

	@GetMapping("/ca")
	public String caReport(@RequestParam(value = "from", required = false) String from,
			@RequestParam(value = "to", required = false) String to,
			Model model) {
		DateTimeFormatter df = DateTimeFormatter.ISO_DATE;
		LocalDate toDate = (to == null || to.isEmpty()) ? LocalDate.now().plusDays(1)
				: LocalDate.parse(to, df).plusDays(1);
		LocalDate fromDate = (from == null || from.isEmpty()) ? LocalDate.now().withDayOfMonth(1)
				: LocalDate.parse(from, df);

		CaResponseDto ca = publiciteService.calculateCaForPeriod(fromDate, toDate);
		List<AnnonceurDetailsDto> details = publiciteService.getDetailedCaWithPayments(fromDate, toDate);

		// Calculer les totaux
		BigDecimal totalPaye = details.stream()
				.map(AnnonceurDetailsDto::getMontantPaye)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal totalSolde = details.stream()
				.map(AnnonceurDetailsDto::getSolde)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		model.addAttribute("ca", ca);
		model.addAttribute("details", details);
		model.addAttribute("totalPaye", totalPaye);
		model.addAttribute("totalSolde", totalSolde);
		model.addAttribute("from", fromDate);
		model.addAttribute("to", toDate.minusDays(1));
		model.addAttribute("nbAnnonceurs", details.size());
		model.addAttribute("totalDiffusions", details.stream().mapToLong(AnnonceurDetailsDto::getNbDiffusions).sum());
		model.addAttribute("currentPage", "publicite-ca");
		return "reports/ca_publicite";
	}

	@GetMapping("/ca/json")
	@ResponseBody
	public CaResponseDto caReportJson(@RequestParam(value = "from", required = false) String from,
			@RequestParam(value = "to", required = false) String to) {
		DateTimeFormatter df = DateTimeFormatter.ISO_DATE;
		LocalDate toDate = (to == null || to.isEmpty()) ? LocalDate.now().plusDays(1)
				: LocalDate.parse(to, df).plusDays(1);
		LocalDate fromDate = (from == null || from.isEmpty()) ? LocalDate.now().withDayOfMonth(1)
				: LocalDate.parse(from, df);

		return publiciteService.calculateCaForPeriod(fromDate, toDate);
	}

	@GetMapping("/paiements")
	public String paiementsList(@RequestParam(value = "annonceurId", required = false) Long annonceurId,
			@RequestParam(value = "from", required = false) String from,
			@RequestParam(value = "to", required = false) String to,
			Model model) {
		DateTimeFormatter df = DateTimeFormatter.ISO_DATE;
		LocalDate toDate = (to == null || to.isEmpty()) ? LocalDate.now().plusDays(1)
				: LocalDate.parse(to, df).plusDays(1);
		LocalDate fromDate = (from == null || from.isEmpty()) ? LocalDate.of(2020, 1, 1) : LocalDate.parse(from, df);

		List<PaiementAnnonceur> paiements = publiciteService.getPayments(annonceurId, fromDate, toDate);
		BigDecimal totalPaiements = publiciteService.getTotalPayments(annonceurId, fromDate, toDate);
		List<SocietePublicitaire> annonceurs = publiciteService.getAllAnnonceurs();

		model.addAttribute("paiements", paiements);
		model.addAttribute("totalPaiements", totalPaiements);
		model.addAttribute("annonceurs", annonceurs);
		model.addAttribute("selectedAnnonceurId", annonceurId);
		model.addAttribute("from", fromDate);
		model.addAttribute("to", toDate.minusDays(1));
		model.addAttribute("currentPage", "publicite-paiements");
		model.addAttribute("pageTitle", "Paiements des annonceurs");
		return "publicite/paiements";
	}

	@PostMapping("/paiements/ajouter")
	public String ajouterPaiement(
			@RequestParam("annonceurId") Long annonceurId,
			@RequestParam("montantPaye") BigDecimal montantPaye,
			@RequestParam("datePaiement") String datePaiement,
			RedirectAttributes redirectAttributes) {
		try {
			LocalDate date = LocalDate.parse(datePaiement);
			publiciteService.savePaiement(annonceurId, montantPaye, date);
			redirectAttributes.addFlashAttribute("success", "Paiement ajouté avec succès");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Erreur lors de l'ajout du paiement: " + e.getMessage());
		}
		return "redirect:/publicite/paiements";
	}
}
