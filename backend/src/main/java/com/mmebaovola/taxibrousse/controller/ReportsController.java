package com.mmebaovola.taxibrousse.controller;

import com.mmebaovola.taxibrousse.repository.ReservationRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/reports")
public class ReportsController {

    private final ReservationRepository reservationRepository;
    private final com.mmebaovola.taxibrousse.repository.TrajetRepository trajetRepository;

    public ReportsController(ReservationRepository reservationRepository,
            com.mmebaovola.taxibrousse.repository.TrajetRepository trajetRepository) {
        this.reservationRepository = reservationRepository;
        this.trajetRepository = trajetRepository;
    }

    public record TaxiDailyTurnoverView(Long taxiId, String immatriculation, LocalDate jour, double total) {
    }

    public record VoyageTurnoverView(
            Long voyageId,
            String immatriculation,
            String trajetNom,
            LocalDateTime dateDepart,
            long reservationCount,
            double total) {
    }

    public record TrajetTurnoverView(
            Long trajetId,
            String trajetNom,
            long reservationCount,
            double total) {
    }

    @GetMapping("/turnover")
    public String turnover(
            Model model,
            @RequestParam(name = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(name = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(name = "trajetId", required = false) Long trajetId) {
        LocalDate today = LocalDate.now();
        LocalDate fromDate = (from != null) ? from : today.minusDays(30);
        LocalDate toDate = (to != null) ? to : today;

        if (toDate.isBefore(fromDate)) {
            LocalDate tmp = fromDate;
            fromDate = toDate;
            toDate = tmp;
        }

        LocalDateTime startInclusive = fromDate.atStartOfDay();
        LocalDateTime endExclusive = toDate.plusDays(1).atStartOfDay();

        List<TaxiDailyTurnoverView> taxiDaily = reservationRepository
                .findTurnoverByTaxiPerDay(startInclusive, endExclusive)
                .stream()
                .map(r -> new TaxiDailyTurnoverView(
                        r.getTaxiId(),
                        r.getImmatriculation(),
                        r.getJour() != null ? r.getJour().toLocalDate() : null,
                        r.getTotal() != null ? r.getTotal() : 0.0))
                .toList();

        // CA par trajet (au lieu de par voyage) - permet de filtrer par trajetId
        List<TrajetTurnoverView> trajetsCA = reservationRepository
                .findTurnoverByTrajet(startInclusive, endExclusive, trajetId)
                .stream()
                .map(r -> new TrajetTurnoverView(
                        r.getTrajetId(),
                        r.getTrajetNom(),
                        r.getReservationCount() != null ? r.getReservationCount() : 0L,
                        r.getTotal() != null ? r.getTotal() : 0.0))
                .toList();

        // Ancien tableau de voyages (optionnel) reste inchangé si besoin
        List<VoyageTurnoverView> voyages = reservationRepository
                .findTurnoverByVoyage(startInclusive, endExclusive)
                .stream()
                .map(r -> new VoyageTurnoverView(
                        r.getVoyageId(),
                        r.getImmatriculation(),
                        r.getTrajetNom(),
                        r.getDateDepart(),
                        r.getReservationCount() != null ? r.getReservationCount() : 0L,
                        r.getTotal() != null ? r.getTotal() : 0.0))
                .toList();

        double totalCA = taxiDaily.stream().mapToDouble(TaxiDailyTurnoverView::total).sum();
        long voyagesCount = voyages.size();

        model.addAttribute("pageTitle", "Chiffre d'affaires");
        model.addAttribute("currentPage", "reports-turnover");

        model.addAttribute("from", fromDate);
        model.addAttribute("to", toDate);
        model.addAttribute("trajetId", trajetId);
        model.addAttribute("trajets", trajetRepository.findAll());

        model.addAttribute("totalCA", totalCA);
        model.addAttribute("voyagesCount", voyagesCount);
        model.addAttribute("taxiDaily", taxiDaily);
        model.addAttribute("voyages", voyages);
        model.addAttribute("trajetsCA", trajetsCA);

        return "reports/turnover";
    }
}
