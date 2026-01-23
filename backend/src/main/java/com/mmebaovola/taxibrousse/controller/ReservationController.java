package com.mmebaovola.taxibrousse.controller;

import com.mmebaovola.taxibrousse.entity.Reservation;
import com.mmebaovola.taxibrousse.entity.DetailsReservation;
import com.mmebaovola.taxibrousse.entity.Voyage;
import com.mmebaovola.taxibrousse.entity.CategoriePlace;
import com.mmebaovola.taxibrousse.entity.TarifPlace;
import com.mmebaovola.taxibrousse.entity.PassengerCategory; // added for passager categorie handling
import com.mmebaovola.taxibrousse.entity.Paiement; // added paiement entity
import com.mmebaovola.taxibrousse.repository.ClientRepository;
import com.mmebaovola.taxibrousse.repository.ReservationRepository;
import com.mmebaovola.taxibrousse.repository.VoyageRepository;
import com.mmebaovola.taxibrousse.repository.DetailsReservationRepository;
import com.mmebaovola.taxibrousse.repository.ArretRepository;
import com.mmebaovola.taxibrousse.repository.TrajetDetailRepository;
import com.mmebaovola.taxibrousse.repository.CategoriePlaceRepository;
import com.mmebaovola.taxibrousse.repository.TarifPlaceRepository;
import com.mmebaovola.taxibrousse.repository.PaiementRepository; // added paiement repo
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final VoyageRepository voyageRepository;
    private final ClientRepository clientRepository;
    private final DetailsReservationRepository detailsReservationRepository;
    private final ArretRepository arretRepository;
    private final TrajetDetailRepository trajetDetailRepository;
    private final CategoriePlaceRepository categoriePlaceRepository;
    private final TarifPlaceRepository tarifPlaceRepository;
    private final PaiementRepository paiementRepository; // ajout champ

    public ReservationController(ReservationRepository reservationRepository, VoyageRepository voyageRepository,
            ClientRepository clientRepository, DetailsReservationRepository detailsReservationRepository,
            ArretRepository arretRepository, TrajetDetailRepository trajetDetailRepository,
            CategoriePlaceRepository categoriePlaceRepository, TarifPlaceRepository tarifPlaceRepository,
            PaiementRepository paiementRepository) {
        this.reservationRepository = reservationRepository;
        this.voyageRepository = voyageRepository;
        this.clientRepository = clientRepository;
        this.detailsReservationRepository = detailsReservationRepository;
        this.arretRepository = arretRepository;
        this.trajetDetailRepository = trajetDetailRepository;
        this.categoriePlaceRepository = categoriePlaceRepository;
        this.tarifPlaceRepository = tarifPlaceRepository;
        this.paiementRepository = paiementRepository; // assignation
    }

    @GetMapping
    public String list(Model model, @RequestParam(name = "q", required = false) String q) {
        List<Reservation> reservations = reservationRepository.findAll();

        if (q != null && !q.trim().isEmpty()) {
            String query = q.toLowerCase();
            reservations = reservations.stream()
                    .filter(r -> {
                        String clientNom = r.getClient() != null && r.getClient().getPersonne() != null
                                && r.getClient().getPersonne().getNom() != null
                                        ? r.getClient().getPersonne().getNom().toLowerCase()
                                        : "";
                        String clientPrenom = r.getClient() != null && r.getClient().getPersonne() != null
                                && r.getClient().getPersonne().getPrenom() != null
                                        ? r.getClient().getPersonne().getPrenom().toLowerCase()
                                        : "";
                        String trajetNom = r.getVoyage() != null && r.getVoyage().getTrajet() != null
                                && r.getVoyage().getTrajet().getNom() != null
                                        ? r.getVoyage().getTrajet().getNom().toLowerCase()
                                        : "";
                        String taxi_brousse = r.getVoyage() != null && r.getVoyage().getTaxiBrousse() != null
                                && r.getVoyage().getTaxiBrousse().getImmatriculation() != null
                                        ? r.getVoyage().getTaxiBrousse().getImmatriculation().toLowerCase()
                                        : "";
                        return clientNom.contains(query)
                                || clientPrenom.contains(query)
                                || trajetNom.contains(query)
                                || taxi_brousse.contains(query);
                    })
                    .toList();
        }

        model.addAttribute("pageTitle", "Réservations");
        model.addAttribute("currentPage", "reservations");
        model.addAttribute("reservations", reservations);
        model.addAttribute("q", q);
        return "reservations/list";
    }

    @GetMapping("/create")
    public String createForm(Model model, @RequestParam(name = "voyageId", required = false) Long voyageId) {
        model.addAttribute("pageTitle", "Créer Réservation");
        model.addAttribute("currentPage", "reservations");

        Reservation reservation = new Reservation();
        model.addAttribute("reservation", reservation);

        List<Voyage> voyages = voyageRepository.findAll();
        model.addAttribute("voyages", voyages);
        model.addAttribute("clients", clientRepository.findAll());

        Voyage selectedVoyage = null;
        if (voyageId != null) {
            selectedVoyage = voyageRepository.findById(voyageId).orElse(null);
        } else if (!voyages.isEmpty()) {
            selectedVoyage = voyages.get(0);
        }

        if (selectedVoyage != null && selectedVoyage.getTaxiBrousse() != null
                && selectedVoyage.getTaxiBrousse().getDispositionPlaces() != null) {
            String disposition = selectedVoyage.getTaxiBrousse().getDispositionPlaces();
            List<DetailsReservation> existingDetails = detailsReservationRepository
                    .findByReservation_Voyage_Id(selectedVoyage.getId());
            Set<String> reservedSeats = new HashSet<>();
            for (DetailsReservation d : existingDetails) {
                if (d.getNumeroPlace() != null) {
                    reservedSeats.add(d.getNumeroPlace());
                }
            }

            // Récupérer les catégories de places du taxi (PREMIUM, STANDARD)
            List<CategoriePlace> categoriesPlaces = categoriePlaceRepository
                    .findByTaxiBrousseId(selectedVoyage.getTaxiBrousse().getId());

            // Récupérer les tarifs du trajet
            Map<String, BigDecimal> tarifsByType = new HashMap<>();
            if (selectedVoyage.getTrajet() != null) {
                List<TarifPlace> tarifs = tarifPlaceRepository.findByTrajetId(selectedVoyage.getTrajet().getId());
                for (TarifPlace tp : tarifs) {
                    String key = tp.getTypePlace() != null ? tp.getTypePlace().trim().toUpperCase() : null;
                    if (key != null && !key.isBlank()) {
                        tarifsByType.put(key, tp.getMontant());
                    }
                }
            }

            // Fallback: if no trajet tariffs configured, use taxi's category prices
            if (tarifsByType.isEmpty() && categoriesPlaces != null) {
                for (CategoriePlace cp : categoriesPlaces) {
                    String key = cp.getType() != null ? cp.getType().trim().toUpperCase() : null;
                    if (key != null && !key.isBlank() && cp.getPrixParType() != null) {
                        tarifsByType.put(key, cp.getPrixParType());
                    }
                }
            }

            List<List<SeatView>> seatRows = buildSeatMap(disposition, reservedSeats, categoriesPlaces, tarifsByType);
            model.addAttribute("seatRows", seatRows);
            model.addAttribute("tarifsByType", tarifsByType);
            model.addAttribute("categoriesPlaces", categoriesPlaces);
        }

        model.addAttribute("selectedVoyageId", selectedVoyage != null ? selectedVoyage.getId() : null);

        return "reservations/form";
    }

    @PostMapping("/save")
    public String save(Reservation reservation,
            @RequestParam(name = "dateReservationStr") String dateReservationStr,
            @RequestParam(name = "seatNumbers", required = false) List<String> seatNumbers,
            @RequestParam(name = "seatChildFlags", required = false) List<String> seatChildFlags,
            @RequestParam(name = "seatCategorieFlags", required = false) List<String> seatCategorieFlags,
            @RequestParam(name = "montantPaye", required = false) Double montantPaye, // nouveau param
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        if (dateReservationStr != null && !dateReservationStr.isBlank()) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(dateReservationStr);
                reservation.setDateReservation(dateTime);
            } catch (java.time.format.DateTimeParseException ex) {
                redirectAttributes.addFlashAttribute("errorMessage", "Format de date invalide pour la réservation.");
                return "redirect:/reservations/create";
            }
        }

        // Validation : la date de réservation ne doit pas être postérieure à la date de
        // départ du voyage
        Voyage voyage = null;
        if (reservation.getVoyage() != null && reservation.getVoyage().getId() != null) {
            var optVoyage = voyageRepository.findById(reservation.getVoyage().getId());
            if (optVoyage.isPresent()) {
                voyage = optVoyage.get();
                if (voyage.getDateDepart() != null && reservation.getDateReservation() != null
                        && reservation.getDateReservation().isAfter(voyage.getDateDepart())) {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "La date de réservation ne peut pas être postérieure à la date de départ du voyage.");
                    // Retourner au formulaire de création en conservant le voyage sélectionné
                    return "redirect:/reservations/create?voyageId=" + voyage.getId();
                }
            }
        }

        // parse child flags (rétrocompatibilité)
        Map<String, Boolean> childMap = new HashMap<>();
        if (seatChildFlags != null) {
            for (String f : seatChildFlags) {
                if (f == null || !f.contains(":"))
                    continue;
                String[] parts = f.split(":", 2);
                if (parts.length == 2) {
                    childMap.put(parts[0], "true".equalsIgnoreCase(parts[1]) || "1".equals(parts[1]));
                }
            }
        }

        // parse categorie flags (nouveau système)
        Map<String, PassengerCategory> categorieMap = new HashMap<>();
        if (seatCategorieFlags != null) {
            for (String f : seatCategorieFlags) {
                if (f == null || !f.contains(":"))
                    continue;
                String[] parts = f.split(":", 2);
                if (parts.length == 2) {
                    try {
                        PassengerCategory cat = PassengerCategory.valueOf(parts[1].toUpperCase());
                        categorieMap.put(parts[0], cat);
                    } catch (IllegalArgumentException e) {
                        categorieMap.put(parts[0], PassengerCategory.ADULTE);
                    }
                }
            }
        }

        // Precompute tarifs by type for this voyage date
        Map<String, BigDecimal> tarifsByType = new HashMap<>();
        if (voyage != null && voyage.getTrajet() != null && voyage.getDateDepart() != null) {
            List<TarifPlace> tps = tarifPlaceRepository.findCurrentTarifsByTrajet(voyage.getTrajet().getId(),
                    voyage.getDateDepart().toLocalDate());
            for (TarifPlace t : tps) {
                String key = t.getTypePlace() != null ? t.getTypePlace().trim().toUpperCase() : null;
                if (key != null && !key.isBlank()) {
                    tarifsByType.put(key, t.getMontant());
                }
            }
        }

        // Fallback: if no trajet tariffs configured, use taxi's category prices
        if (tarifsByType.isEmpty() && voyage != null && voyage.getTaxiBrousse() != null) {
            List<CategoriePlace> cps = categoriePlaceRepository.findByTaxiBrousseId(voyage.getTaxiBrousse().getId());
            for (CategoriePlace cp : cps) {
                String key = cp.getType() != null ? cp.getType().trim().toUpperCase() : null;
                if (key != null && !key.isBlank() && cp.getPrixParType() != null) {
                    tarifsByType.put(key, cp.getPrixParType());
                }
            }
        }

        // build seat label -> type map from taxi disposition
        Map<String, String> seatTypeMap = new HashMap<>();
        if (voyage != null && voyage.getTaxiBrousse() != null
                && voyage.getTaxiBrousse().getDispositionPlaces() != null) {
            String disposition = voyage.getTaxiBrousse().getDispositionPlaces();
            String[] rawRows = disposition.split("/");
            for (int i = 0; i < rawRows.length; i++) {
                String row = rawRows[i];
                char rowLetter = (char) ('A' + i);
                int seatIndexInRow = 0;
                for (int j = 0; j < row.length(); j++) {
                    char c = row.charAt(j);
                    if (c == 'x' || c == 'X')
                        continue;
                    seatIndexInRow++;
                    String label = rowLetter + String.valueOf(seatIndexInRow);
                    if (c == 'P' || c == 'p')
                        seatTypeMap.put(label, "PREMIUM");
                    else if (c == 'S' || c == 's' || c == 'O' || c == 'o')
                        seatTypeMap.put(label, "STANDARD");
                    else if (c == 'V' || c == 'v')
                        seatTypeMap.put(label, "VIP");
                }
            }
        }

        // calculate montant total server-side to be authoritative
        BigDecimal montantTotal = BigDecimal.ZERO;
        final BigDecimal enfantStandardPrix = BigDecimal.valueOf(50000);

        // Map pour stocker le prix calculé par place (pour le réutiliser lors de la
        // sauvegarde des détails)
        Map<String, BigDecimal> prixParPlace = new HashMap<>();

        if (seatNumbers != null && !seatNumbers.isEmpty()) {
            for (String seat : seatNumbers) {
                if (seat == null || seat.trim().isEmpty())
                    continue;
                String label = seat.trim();
                String type = seatTypeMap.getOrDefault(label, "STANDARD");
                PassengerCategory categorie = categorieMap.getOrDefault(label, PassengerCategory.ADULTE);
                BigDecimal basePrix = tarifsByType.getOrDefault(type, BigDecimal.ZERO);

                // Calculer le prix avec réduction selon la catégorie
                BigDecimal prix = calculatePrixWithReduction(basePrix, type, categorie, enfantStandardPrix);
                prixParPlace.put(label, prix);
                montantTotal = montantTotal.add(prix);
            }
        }

        reservation.setMontantTotal(montantTotal.doubleValue());

        // Validation serveur : montantPaye (s'il est fourni) ne doit pas être négatif
        // ni supérieur au montant total calculé.
        if (montantPaye != null) {
            BigDecimal mp = BigDecimal.valueOf(montantPaye);
            if (mp.compareTo(BigDecimal.ZERO) < 0) {
                redirectAttributes.addFlashAttribute("errorMessage", "Le montant payé ne peut pas être négatif.");
                return "redirect:/reservations/create" + (voyage != null && voyage.getId() != null ? "?voyageId=" + voyage.getId() : "");
            }
            if (mp.compareTo(montantTotal) > 0) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Le montant payé ne peut pas être supérieur au montant total (" + montantTotal + " Ar).");
                return "redirect:/reservations/create" + (voyage != null && voyage.getId() != null ? "?voyageId=" + voyage.getId() : "");
            }
        }

        reservationRepository.save(reservation);

        // If an initial payment amount was provided, create a Paiement record
        // (le paiement, s'il est fourni, sera enregistré après la création des détails de réservation)

        // Vérifier les places déjà réservées pour ce voyage
        Set<String> alreadyReserved = new HashSet<>();
        if (voyage != null) {
            List<DetailsReservation> existingDetails = detailsReservationRepository
                    .findByReservation_Voyage_Id(voyage.getId());
            for (DetailsReservation d : existingDetails) {
                if (d.getNumeroPlace() != null) {
                    alreadyReserved.add(d.getNumeroPlace());
                }
            }
        }

        int placesReservees = 0;
        StringBuilder placesIgnorees = new StringBuilder();

        if (seatNumbers != null && !seatNumbers.isEmpty()) {
            for (String seat : seatNumbers) {
                if (seat != null && !seat.trim().isEmpty()) {
                    String label = seat.trim();

                    // Vérifier si la place n'est pas déjà réservée
                    if (alreadyReserved.contains(label)) {
                        if (placesIgnorees.length() > 0)
                            placesIgnorees.append(", ");
                        placesIgnorees.append(label);
                        continue;
                    }

                    DetailsReservation details = new DetailsReservation();
                    details.setReservation(reservation);
                    details.setNumeroPlace(label);

                    PassengerCategory categorie = categorieMap.getOrDefault(label, PassengerCategory.ADULTE);
                    boolean isEnf = categorie == PassengerCategory.ENFANT;

                    details.setTypePlace(seatTypeMap.getOrDefault(label, "STANDARD"));
                    details.setIsEnfant(isEnf);
                    details.setPassagerCategorie(categorie);
                    details.setPrixUnitaire(prixParPlace.getOrDefault(label, BigDecimal.ZERO));
                    detailsReservationRepository.save(details);
                    placesReservees++;
                }
            }
        }

        String message = "Réservation enregistrée avec " + placesReservees + " place(s) réservée(s). Montant: "
                + montantTotal + " Ar";
        if (placesIgnorees.length() > 0) {
            message += ". Attention: places déjà réservées ignorées: " + placesIgnorees;
        }

        // Enregistrer un paiement initial si demandé (après la création des détails)
        if (montantPaye != null && montantPaye > 0) {
            try {
                Paiement p = new Paiement();
                p.setReservation(reservation);
                p.setMontantPaye(montantPaye);
                p.setDatePaiement(LocalDateTime.now());
                paiementRepository.save(p);
                message += ". Paiement initial enregistré: " + montantPaye + " Ar";
            } catch (Exception e) {
                // Ne pas bloquer la réservation si l'enregistrement du paiement échoue
                redirectAttributes.addFlashAttribute("warningMessage",
                        "Réservation enregistrée mais échec lors de l'enregistrement du paiement : " + e.getMessage());
                // continuer pour afficher la réservation
            }
        }

        redirectAttributes.addFlashAttribute("successMessage", message);
        return "redirect:/reservations";
    }

    /**
     * Calcule le prix avec réduction selon la catégorie de passager.
     * - ADULTE: tarif plein (0%)
     * - ENFANT: -50% sur STANDARD, tarif fixe 50000 Ar
     * - SENIOR: -20% sur tous les types
     * - JEUNE: -10% sur tous les types
     * - ETUDIANT: -15% sur tous les types
     */
    private BigDecimal calculatePrixWithReduction(BigDecimal basePrix, String type, PassengerCategory categorie,
            BigDecimal enfantStandardPrix) {
        switch (categorie) {
            case ENFANT:
                if ("STANDARD".equalsIgnoreCase(type)) {
                    return enfantStandardPrix;
                }
                return basePrix; // Pas de réduction enfant pour les autres types
            case SENIOR:
                // -20%
                return basePrix.multiply(BigDecimal.valueOf(0.80)).setScale(0, java.math.RoundingMode.HALF_UP);
            case JEUNE:
                // -10%
                return basePrix.multiply(BigDecimal.valueOf(0.90)).setScale(0, java.math.RoundingMode.HALF_UP);
            case ETUDIANT:
                // -15%
                return basePrix.multiply(BigDecimal.valueOf(0.85)).setScale(0, java.math.RoundingMode.HALF_UP);
            case ADULTE:
            default:
                return basePrix;
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Modifier Réservation");
        model.addAttribute("currentPage", "reservations");
        model.addAttribute("reservation", reservationRepository.findById(id).orElse(new Reservation()));
        model.addAttribute("voyages", voyageRepository.findAll());
        model.addAttribute("clients", clientRepository.findAll());
        return "reservations/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        if (detailsReservationRepository.existsByReservationId(id)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Impossible de supprimer cette réservation : elle contient des détails liés.");
        } else {
            reservationRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Réservation supprimée avec succès.");
        }
        return "redirect:/reservations";
    }

    private List<List<SeatView>> buildSeatMap(String disposition, Set<String> reservedSeats,
            List<CategoriePlace> categoriesPlaces, Map<String, BigDecimal> tarifsByType) {
        List<List<SeatView>> rows = new ArrayList<>();
        if (disposition == null || disposition.isEmpty()) {
            return rows;
        }

        // Ensure default prices if tarifsByType is missing entries
        if (!tarifsByType.containsKey("VIP")) {
            tarifsByType.put("VIP", new BigDecimal("180000"));
        }
        if (!tarifsByType.containsKey("PREMIUM")) {
            tarifsByType.put("PREMIUM", new BigDecimal("140000"));
        }
        if (!tarifsByType.containsKey("STANDARD")) {
            tarifsByType.put("STANDARD", new BigDecimal("80000"));
        }

        String[] rawRows = disposition.split("/");

        for (int i = 0; i < rawRows.length; i++) {
            String row = rawRows[i];
            char rowLetter = (char) ('A' + i);
            int seatIndexInRow = 0;
            List<SeatView> seatViews = new ArrayList<>();

            for (int j = 0; j < row.length(); j++) {
                char c = row.charAt(j);
                if (c == 'x' || c == 'X') {
                    // Couloir / espace vide
                    seatViews.add(SeatView.walkway());
                } else if (c == 'V' || c == 'v') {
                    // Place VIP
                    seatIndexInRow++;
                    String label = rowLetter + String.valueOf(seatIndexInRow);
                    boolean reserved = reservedSeats.contains(label);
                    BigDecimal prix = tarifsByType.getOrDefault("VIP", BigDecimal.ZERO);
                    seatViews.add(SeatView.seat(label, !reserved, "VIP", prix));
                } else if (c == 'P' || c == 'p') {
                    // Place PREMIUM
                    seatIndexInRow++;
                    String label = rowLetter + String.valueOf(seatIndexInRow);
                    boolean reserved = reservedSeats.contains(label);
                    BigDecimal prix = tarifsByType.getOrDefault("PREMIUM", BigDecimal.ZERO);
                    seatViews.add(SeatView.seat(label, !reserved, "PREMIUM", prix));
                } else if (c == 'S' || c == 's') {
                    // Place STANDARD
                    seatIndexInRow++;
                    String label = rowLetter + String.valueOf(seatIndexInRow);
                    boolean reserved = reservedSeats.contains(label);
                    BigDecimal prix = tarifsByType.getOrDefault("STANDARD", BigDecimal.ZERO);
                    seatViews.add(SeatView.seat(label, !reserved, "STANDARD", prix));
                } else if (c == 'o' || c == 'O') {
                    // Ancien format (compatibilité) - traité comme STANDARD
                    seatIndexInRow++;
                    String label = rowLetter + String.valueOf(seatIndexInRow);
                    boolean reserved = reservedSeats.contains(label);
                    BigDecimal prix = tarifsByType.getOrDefault("STANDARD", BigDecimal.ZERO);
                    seatViews.add(SeatView.seat(label, !reserved, "STANDARD", prix));
                } else {
                    // Autre caractère = espace vide
                    seatViews.add(SeatView.walkway());
                }
            }

            rows.add(seatViews);
        }

        return rows;
    }

    @GetMapping("/search")
    public String searchForm(Model model,
            @RequestParam(name = "departId", required = false) Long departId,
            @RequestParam(name = "arriveeId", required = false) Long arriveeId,
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "moment", required = false) String moment) {

        model.addAttribute("pageTitle", "Recherche de voyages");
        model.addAttribute("currentPage", "reservations-search");
        model.addAttribute("arrets", arretRepository.findAll());
        model.addAttribute("departId", departId);
        model.addAttribute("arriveeId", arriveeId);
        model.addAttribute("selectedDate", date);
        model.addAttribute("selectedMoment", moment);

        List<Voyage> voyagesTrouves = new ArrayList<>();

        if (departId != null && arriveeId != null && date != null && !date.isBlank()) {
            LocalDate targetDate;
            try {
                targetDate = LocalDate.parse(date); // yyyy-MM-dd
            } catch (java.time.format.DateTimeParseException ex) {
                // accept dd/MM/yyyy as fallback
                java.time.format.DateTimeFormatter alt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                try {
                    targetDate = LocalDate.parse(date, alt);
                } catch (java.time.format.DateTimeParseException ex2) {
                    model.addAttribute("errorMessage", "Format de date invalide. Utilisez yyyy-MM-dd ou dd/MM/yyyy");
                    model.addAttribute("arrets", arretRepository.findAll());
                    model.addAttribute("pageTitle", "Recherche de voyages");
                    model.addAttribute("currentPage", "reservations-search");
                    return "reservations/search";
                }
            }

            LocalTime start;
            LocalTime end;
            if ("MATIN".equalsIgnoreCase(moment)) {
                start = LocalTime.of(5, 0);
                end = LocalTime.of(11, 59);
            } else if ("MIDI".equalsIgnoreCase(moment)) {
                start = LocalTime.of(12, 0);
                end = LocalTime.of(14, 0);
            } else if ("SOIR".equalsIgnoreCase(moment)) {
                start = LocalTime.of(14, 0);
                end = LocalTime.of(23, 0);
            } else {
                start = LocalTime.MIDNIGHT;
                end = LocalTime.of(23, 59);
            }

            LocalDateTime startDateTime = LocalDateTime.of(targetDate, start);
            LocalDateTime endDateTime = LocalDateTime.of(targetDate, end);

            for (Voyage v : voyageRepository.findAll()) {
                if (v.getDateDepart() == null || v.getTrajet() == null) {
                    continue;
                }

                if (v.getDateDepart().isBefore(startDateTime) || v.getDateDepart().isAfter(endDateTime)) {
                    continue;
                }

                var details = trajetDetailRepository.findByTrajetIdOrderByOrdreAsc(v.getTrajet().getId());
                Integer ordreDepart = null;
                Integer ordreArrivee = null;

                for (var d : details) {
                    if (d.getArret() != null && d.getArret().getId() != null) {
                        if (d.getArret().getId().equals(departId)) {
                            ordreDepart = d.getOrdre();
                        }
                        if (d.getArret().getId().equals(arriveeId)) {
                            ordreArrivee = d.getOrdre();
                        }
                    }
                }

                if (ordreDepart != null && ordreArrivee != null && ordreDepart < ordreArrivee) {
                    voyagesTrouves.add(v);
                }
            }
        }

        // Group voyages by trajet + exact dateDepart to present taxis for same logical
        // voyage
        java.util.Map<String, VoyageGroup> groups = new java.util.LinkedHashMap<>();
        for (Voyage v : voyagesTrouves) {
            if (v.getTrajet() == null || v.getDateDepart() == null)
                continue;
            String key = v.getTrajet().getId() + "|" + v.getDateDepart().toString();
            VoyageGroup g = groups.get(key);
            if (g == null) {
                g = new VoyageGroup(v.getTrajet(), v.getDateDepart());
                groups.put(key, g);
            }
            // compute reserved seats and available seats for this voyage
            java.util.List<DetailsReservation> existingDetails = detailsReservationRepository
                    .findByReservation_Voyage_Id(v.getId());
            int reserved = existingDetails != null ? existingDetails.size() : 0;
            int capacity = v.getTaxiBrousse() != null && v.getTaxiBrousse().getNbrPlaces() != null
                    ? v.getTaxiBrousse().getNbrPlaces()
                    : 0;
            int available = Math.max(0, capacity - reserved);
            g.addOption(new VoyageOption(v, reserved, available, capacity));
        }

        model.addAttribute("groupedVoyages", groups.values());
        model.addAttribute("voyagesTrouves", voyagesTrouves);

        return "reservations/search";
    }

    public static class SeatView {
        private final String label;
        private final boolean seat;
        private final boolean available;
        private final String type; // VIP, PREMIUM ou STANDARD
        private final BigDecimal prix;

        private SeatView(String label, boolean seat, boolean available, String type, BigDecimal prix) {
            this.label = label;
            this.seat = seat;
            this.available = available;
            this.type = type;
            this.prix = prix;
        }

        public static SeatView walkway() {
            return new SeatView(null, false, false, null, null);
        }

        public static SeatView seat(String label, boolean available, String type, BigDecimal prix) {
            return new SeatView(label, true, available, type, prix);
        }

        public String getLabel() {
            return label;
        }

        public boolean isSeat() {
            return seat;
        }

        public boolean isAvailable() {
            return available;
        }

        public String getType() {
            return type;
        }

        public BigDecimal getPrix() {
            return prix;
        }

        public boolean isPremium() {
            return "PREMIUM".equalsIgnoreCase(type);
        }

        public boolean isVip() {
            return "VIP".equalsIgnoreCase(type);
        }
    }

    // Helper classes to represent grouped voyages and options (taxi per logical
    // voyage)
    public static class VoyageOption {
        private final Voyage voyage;
        private final int reservedSeats;
        private final int availableSeats;
        private final int capacity;

        public VoyageOption(Voyage voyage, int reservedSeats, int availableSeats, int capacity) {
            this.voyage = voyage;
            this.reservedSeats = reservedSeats;
            this.availableSeats = availableSeats;
            this.capacity = capacity;
        }

        public Voyage getVoyage() {
            return voyage;
        }

        public int getReservedSeats() {
            return reservedSeats;
        }

        public int getAvailableSeats() {
            return availableSeats;
        }

        public int getCapacity() {
            return capacity;
        }
    }

    public static class VoyageGroup {
        private final com.mmebaovola.taxibrousse.entity.Trajet trajet;
        private final java.time.LocalDateTime dateDepart;
        private final java.util.List<VoyageOption> options = new java.util.ArrayList<>();

        public VoyageGroup(com.mmebaovola.taxibrousse.entity.Trajet trajet, java.time.LocalDateTime dateDepart) {
            this.trajet = trajet;
            this.dateDepart = dateDepart;
        }

        public com.mmebaovola.taxibrousse.entity.Trajet getTrajet() {
            return trajet;
        }

        public java.time.LocalDateTime getDateDepart() {
            return dateDepart;
        }

        public java.util.List<VoyageOption> getOptions() {
            return options;
        }

        public void addOption(VoyageOption opt) {
            this.options.add(opt);
        }
    }
}
