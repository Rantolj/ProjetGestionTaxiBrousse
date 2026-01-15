-- Données de test pour le système de taxi-brousse à Madagascar
-- Inserts dans l'ordre des dépendances

-- 1. Personnes
INSERT INTO personnes (nom, prenom, contact) VALUES
('Rakoto', 'Jean', '+261 34 12 345 67'),
('Rabe', 'Marie', '+261 33 98 765 43'),
('Andrianaivo', 'Paul', '+261 32 45 678 90'),
('Razafindrakoto', 'Sophie', '+261 34 56 789 01'),
('Randrianarisoa', 'Michel', '+261 33 67 890 12'),
('Ramanantsoa', 'Antoinette', '+261 32 78 901 23'),
('Andriamahazo', 'Pierre', '+261 34 89 012 34'),
('Ravelojaona', 'Lucie', '+261 33 90 123 45'),
('Rasolofomanana', 'Jacques', '+261 32 01 234 56'),
('Ranaivoson', 'Claire', '+261 34 12 345 78');

-- 2. Clients (référencent personnes)
INSERT INTO clients (personne_id) VALUES
(1), (2), (3), (4), (5);

-- 3. Chauffeurs (référencent personnes)
INSERT INTO chauffeurs (personne_id) VALUES
(6), (7), (8), (9), (10);

-- 4. Arrêts (avec indication si c'est une gare routière)
INSERT INTO arrets (nom, est_gare) VALUES
('Gare routière Fasankarana (Tana)', true),
('Gare routière Majunga', true),
('Gare routière Ambolomandinika Toamasina', true),
('Gare routière Diego-Suarez', true),
('Gare routière Fianarantsoa', true),
('Gare routière Toliara', true),
('Antsirabe', false),
('Morondava', false),
('Ambatondrazaka', false),
('Sambava', false),
('Moramanga', false),
('Brickaville', false);

-- 5. Trajets
INSERT INTO trajets (nom, distance) VALUES
('Tana - Majunga', 570.0),
('Fasankarana - Ambolomandinika Toamasina', 365.0),
('Tana - Diego', 1100.0),
('Tana - Fianarantsoa', 410.0),
('Tana - Toliara', 950.0),
('Majunga - Diego', 530.0),
('Toamasina - Fianarantsoa', 620.0);

-- 6. Trajet_details (arrêts dans l'ordre pour chaque trajet)
-- Trajet 1: Tana - Majunga (arrêts intermédiaires)
INSERT INTO trajet_details (trajet_id, arret_id, ordre) VALUES
(1, 1, 1), -- Gare Fasankarana (Tana)
(1, 7, 2), -- Antsirabe
(1, 2, 3); -- Gare Majunga

-- Trajet 2: Fasankarana - Ambolomandinika Toamasina (la demande du client)
INSERT INTO trajet_details (trajet_id, arret_id, ordre) VALUES
(2, 1, 1), -- Gare routière Fasankarana
(2, 11, 2), -- Moramanga
(2, 12, 3), -- Brickaville
(2, 3, 4); -- Gare routière Ambolomandinika Toamasina

-- Trajet 3: Tana - Diego
INSERT INTO trajet_details (trajet_id, arret_id, ordre) VALUES
(3, 1, 1), -- Tana
(3, 7, 2), -- Antsirabe
(3, 5, 3), -- Fianarantsoa
(3, 4, 4); -- Diego

-- Trajet 4: Tana - Fianarantsoa
INSERT INTO trajet_details (trajet_id, arret_id, ordre) VALUES
(4, 1, 1), -- Tana
(4, 5, 2); -- Fianarantsoa

-- Trajet 5: Tana - Toliara
INSERT INTO trajet_details (trajet_id, arret_id, ordre) VALUES
(5, 1, 1), -- Tana
(5, 5, 2), -- Fianarantsoa
(5, 6, 3); -- Toliara

-- Trajet 6: Majunga - Diego
INSERT INTO trajet_details (trajet_id, arret_id, ordre) VALUES
(6, 2, 1), -- Majunga
(6, 4, 2); -- Diego

-- Trajet 7: Toamasina - Fianarantsoa
INSERT INTO trajet_details (trajet_id, arret_id, ordre) VALUES
(7, 3, 1), -- Toamasina
(7, 5, 2); -- Fianarantsoa

-- 7. Catégories
INSERT INTO categories_tb (nom) VALUES
('Standard'),
('VIP');

-- 8. Taxi-brousses
-- Disposition: V = VIP, P = PREMIUM, S = STANDARD, x = couloir/espace
INSERT INTO taxi_brousses (immatriculation, categorie_id, nbr_places, charge_max, consommation, disposition_places) VALUES
-- Taxi 1: 25 places (2 VIP + 4 PREMIUM + 19 STANDARD)
('1234-TAB', 1, 25, 3500.00, 8.5, 'xxVV/PPPP/SSxS/SSxS/SSxS/SSxS/SSxS/SSSS'),
-- Taxi 2 VIP (catégorie taxi): 16 places (2 VIP + 4 PREMIUM + 10 STANDARD)
('5678-TAB', 2, 16, 2800.00, 7.2, 'xxVV/PPPP/SSxS/SSxS/SSSS'),
-- Taxi 3: 28 places (2 VIP + 4 PREMIUM + 22 STANDARD)
('9012-TAB', 1, 28, 4200.00, 9.0, 'xxVV/PPPP/SSxS/SSxS/SSxS/SSxS/SSxS/SSxS/SSSS'),
-- Taxi 4 (donnée demandée): 18 places (2 VIP + 6 PREMIUM + 10 STANDARD)
('3456-TAB', 1, 18, 3000.00, 7.8, 'xxVV/PPPP/PPSS/SSxS/SSSS/xxSx');

-- 8bis. Categories de places par taxi-brousse (STANDARD et PREMIUM)
-- Taxi 1 (1234-TAB, 25 places): 2 VIP + 4 PREMIUM + 19 STANDARD
INSERT INTO categories_places (taxi_brousse_id, type, nbr_places_type, prix_par_type) VALUES
(1, 'VIP', 2, 180000.00),
(1, 'PREMIUM', 4, 140000.00),
(1, 'STANDARD', 19, 80000.00);


-- Taxi 2 (5678-TAB, 16 places): 2 VIP + 4 PREMIUM + 10 STANDARD
INSERT INTO categories_places (taxi_brousse_id, type, nbr_places_type, prix_par_type) VALUES
(2, 'VIP', 2, 180000.00),
(2, 'PREMIUM', 4, 160000.00),
(2, 'STANDARD', 10, 100000.00);

-- Taxi 3 (9012-TAB, 28 places): 2 VIP + 4 PREMIUM + 22 STANDARD
INSERT INTO categories_places (taxi_brousse_id, type, nbr_places_type, prix_par_type) VALUES
(3, 'VIP', 2, 180000.00),
(3, 'PREMIUM', 4, 140000.00),
(3, 'STANDARD', 22, 80000.00);

-- Taxi 4 (3456-TAB, 18 places): 2 VIP + 6 PREMIUM + 10 STANDARD
INSERT INTO categories_places (taxi_brousse_id, type, nbr_places_type, prix_par_type) VALUES
(4, 'VIP', 2, 180000.00),
(4, 'PREMIUM', 6, 140000.00),
(4, 'STANDARD', 10, 80000.00);

-- 8ter. Tarifs des places par trajet (par type de place)
-- Trajet 2: Fasankarana - Toamasina (la demande du client avec prix premium/standard)
INSERT INTO tarifs_places (trajet_id, type_place, montant, date_effective) VALUES
(2, 'VIP', 180000.00, '2024-01-01'),
(2, 'PREMIUM', 140000.00, '2024-01-01'),
(2, 'STANDARD', 80000.00, '2024-01-01');

-- Trajet 1: Tana - Majunga
INSERT INTO tarifs_places (trajet_id, type_place, montant, date_effective) VALUES
(1, 'VIP', 180000.00, '2024-01-01'),
(1, 'PREMIUM', 140000.00, '2024-01-01'),
(1, 'STANDARD', 80000.00, '2024-01-01');

-- Trajet 3: Tana - Diego
INSERT INTO tarifs_places (trajet_id, type_place, montant, date_effective) VALUES
(3, 'VIP', 180000.00, '2024-01-01'),
(3, 'PREMIUM', 140000.00, '2024-01-01'),
(3, 'STANDARD', 90000.00, '2024-01-01');

-- Trajet 4: Tana - Fianarantsoa
INSERT INTO tarifs_places (trajet_id, type_place, montant, date_effective) VALUES
(4, 'VIP', 180000.00, '2024-01-01'),
(4, 'PREMIUM', 140000.00, '2024-01-01'),
(4, 'STANDARD', 90000.00, '2024-01-01');

-- Trajet 5: Tana - Toliara
INSERT INTO tarifs_places (trajet_id, type_place, montant, date_effective) VALUES
(5, 'VIP', 180000.00, '2024-01-01'),
(5, 'PREMIUM', 140000.00, '2024-01-01'),
(5, 'STANDARD', 90000.00, '2024-01-01');

-- Trajet 6: Majunga - Diego
INSERT INTO tarifs_places (trajet_id, type_place, montant, date_effective) VALUES
(6, 'VIP', 180000.00, '2024-01-01'),
(6, 'PREMIUM', 140000.00, '2024-01-01'),
(6, 'STANDARD', 90000.00, '2024-01-01');

-- Trajet 7: Toamasina - Fianarantsoa
INSERT INTO tarifs_places (trajet_id, type_place, montant, date_effective) VALUES
(7, 'VIP', 180000.00, '2024-01-01'),
(7, 'PREMIUM', 140000.00, '2024-01-01'),
(7, 'STANDARD', 9000.00, '2024-01-01');

-- 9. Frais (tarifs par trajet et catégorie)
INSERT INTO frais (trajet_id, categorie_id, montant, date_effective) VALUES
(1, 1, 25000.00, '2024-01-01'), -- Tana-Majunga Standard
(1, 2, 35000.00, '2024-01-01'), -- Tana-Majunga VIP
(2, 1, 15000.00, '2024-01-01'), -- Tana-Toamasina Standard
(2, 2, 22000.00, '2024-01-01'), -- Tana-Toamasina VIP
(3, 1, 45000.00, '2024-01-01'), -- Tana-Diego Standard
(3, 2, 60000.00, '2024-01-01'), -- Tana-Diego VIP
(4, 1, 20000.00, '2024-01-01'), -- Tana-Fianarantsoa Standard
(4, 2, 28000.00, '2024-01-01'), -- Tana-Fianarantsoa VIP
(5, 1, 40000.00, '2024-01-01'), -- Tana-Toliara Standard
(5, 2, 55000.00, '2024-01-01'), -- Tana-Toliara VIP
(6, 1, 30000.00, '2024-01-01'), -- Majunga-Diego Standard
(6, 2, 42000.00, '2024-01-01'), -- Majunga-Diego VIP
(7, 1, 25000.00, '2024-01-01'), -- Toamasina-Fianarantsoa Standard
(7, 2, 35000.00, '2024-01-01'); -- Toamasina-Fianarantsoa VIP

-- 10. Voyages
-- Un meme voyage (trajet + date/heure) peut etre effectue par PLUSIEURS VOITURES
INSERT INTO voyages (taxi_brousse_id, chauffeur_id, trajet_id, date_depart) VALUES
-- Voyage 1: Tana-Majunga aujourd'hui
(1, 1, 1, '2026-01-15 08:00:00'), -- Taxi 1 (1234-TAB), Chauffeur 1, Tana-Majunga

-- VOYAGE DEMANDE: Fasankarana vers Ambolomandinika le 14 janvier a 14h
-- 3 voitures differentes font ce meme voyage a la meme heure
(1, 2, 2, '2026-01-14 14:00:00'), -- Voyage 2: Voiture 1 (1234-TAB, 25 places) - Chauffeur 2
(2, 3, 2, '2026-01-14 14:00:00'), -- Voyage 3: Voiture 2 (5678-TAB, 16 places) - Chauffeur 3
(3, 4, 2, '2026-01-14 14:00:00'), -- Voyage 4: Voiture 3 (9012-TAB, 28 places) - Chauffeur 4

-- Autres voyages futurs
(3, 3, 3, '2026-01-17 07:00:00'), -- Voyage 5: Taxi 3, Chauffeur 3, Tana-Diego
(1, 4, 4, '2026-01-18 10:00:00'), -- Voyage 6: Taxi 1, Chauffeur 4, Tana-Fianarantsoa
(2, 5, 5, '2026-01-19 06:00:00'), -- Voyage 7: Taxi 2, Chauffeur 5, Tana-Toliara

-- Voyages passés pour les statistiques de CA
(1, 1, 2, '2026-01-05 06:00:00'), -- Voyage 8: Tana-Toamasina (passé)
(2, 2, 1, '2026-01-07 08:00:00'), -- Voyage 9: Tana-Majunga (passé)
(3, 3, 2, '2026-01-10 14:00:00'); -- Voyage 10: Tana-Toamasina (passé)

-- Voyage 11: même trajet/date pour la voiture demandée (Taxi 4)
INSERT INTO voyages (taxi_brousse_id, chauffeur_id, trajet_id, date_depart) VALUES
(4, 5, 2, '2026-01-14 14:00:00');

-- 11. Reservations avec montants calculés selon les types de places
-- Les montants sont basés sur: VIP / PREMIUM / STANDARD du trajet

-- Voyage 1 (Tana-Majunga): 2 VIP + 1 PREMIUM = 2*180000 + 160000 = 520000 Ar
INSERT INTO reservations (voyage_id, client_id, montant_total, date_reservation) VALUES
(1, 1, 520000.00, '2026-01-10 14:00:00');

-- Voyage 2 (Tana-Toamasina, Taxi 1): 1 VIP + 2 PREMIUM = 180000 + 2*140000 = 460000 Ar
INSERT INTO reservations (voyage_id, client_id, montant_total, date_reservation) VALUES
(2, 2, 460000.00, '2026-01-11 15:00:00');

-- Voyage 3 (Tana-Toamasina, Taxi 2): 2 VIP = 2*180000 = 360000 Ar
INSERT INTO reservations (voyage_id, client_id, montant_total, date_reservation) VALUES
(3, 3, 360000.00, '2026-01-12 10:00:00');

-- Voyage 4 (Tana-Toamasina, Taxi 3): 3 PREMIUM = 3*140000 = 420000 Ar
INSERT INTO reservations (voyage_id, client_id, montant_total, date_reservation) VALUES
(4, 4, 420000.00, '2026-01-12 11:00:00');

-- Voyage 5 (Tana-Diego): 1 VIP + 1 STANDARD = 280000 + 150000 = 430000 Ar
INSERT INTO reservations (voyage_id, client_id, montant_total, date_reservation) VALUES
(5, 5, 430000.00, '2026-01-13 17:00:00');

-- Voyage 8 (passé Tana-Toamasina): plusieurs réservations pour statistiques
INSERT INTO reservations (voyage_id, client_id, montant_total, date_reservation) VALUES
(8, 1, 320000.00, '2026-01-03 10:00:00'), -- 1 VIP + 1 PREMIUM
(8, 2, 280000.00, '2026-01-03 11:00:00'); -- 2 PREMIUM

-- Voyage 9 (passé Tana-Majunga): réservations pour statistiques
INSERT INTO reservations (voyage_id, client_id, montant_total, date_reservation) VALUES
(9, 3, 360000.00, '2026-01-05 09:00:00'), -- 2 VIP
(9, 4, 180000.00, '2026-01-05 10:00:00'); -- 2 STANDARD

-- Voyage 10 (passé Tana-Toamasina): réservations pour statistiques
INSERT INTO reservations (voyage_id, client_id, montant_total, date_reservation) VALUES
(10, 5, 500000.00, '2026-01-08 12:00:00'), -- 2 VIP + 1 PREMIUM
(10, 1, 160000.00, '2026-01-08 13:00:00'); -- 2 STANDARD

-- 12. Details des reservations (places avec indication du type)
-- Reservation 1 (Voyage 1 Tana-Majunga): A1, A2 (VIP), B1 (PREMIUM)
INSERT INTO details_reservations (reservation_id, numero_place) VALUES
(1, 'A1'),  -- VIP
(1, 'A2'),  -- VIP
(1, 'B1');  -- PREMIUM

-- Reservation 2 (Voyage 2): A1 (VIP), B1, B2 (PREMIUM)
INSERT INTO details_reservations (reservation_id, numero_place) VALUES
(2, 'A1'),  -- VIP
(2, 'B1'),  -- PREMIUM
(2, 'B2');  -- PREMIUM

-- Reservation 3 (Voyage 3): A1, A2 (VIP)
INSERT INTO details_reservations (reservation_id, numero_place) VALUES
(3, 'A1'),  -- VIP
(3, 'A2');  -- VIP

-- Reservation 4 (Voyage 4): B1, B2, B3 (PREMIUM)
INSERT INTO details_reservations (reservation_id, numero_place) VALUES
(4, 'B1'),  -- PREMIUM
(4, 'B2'),  -- PREMIUM
(4, 'B3');  -- PREMIUM

-- Reservation 5 (Voyage 5 Tana-Diego): A1 (VIP), C1 (STANDARD)
INSERT INTO details_reservations (reservation_id, numero_place) VALUES
(5, 'A1'),  -- VIP
(5, 'C1');  -- STANDARD

-- Reservations passées pour statistiques
INSERT INTO details_reservations (reservation_id, numero_place) VALUES
(6, 'A1'), (6, 'B1'),  -- Res 6
(7, 'B2'), (7, 'B3'),  -- Res 7
(8, 'A1'), (8, 'A2'),  -- Res 8
(9, 'C1'), (9, 'C2'),  -- Res 9
(10, 'A1'), (10, 'A2'), (10, 'B1'),  -- Res 10
(11, 'C1'), (11, 'C2'); -- Res 11

-- 13. Paiements (paiements complets et partiels)
INSERT INTO paiements (reservation_id, montant_paye, date_paiement) VALUES
-- Paiements complets
(1, 520000.00, '2026-01-10 14:30:00'),
(2, 460000.00, '2026-01-11 15:30:00'),
(3, 360000.00, '2026-01-12 10:30:00'),
(5, 430000.00, '2026-01-13 17:30:00'),

-- Paiement partiel (acompte)
(4, 100000.00, '2026-01-12 11:30:00'),
(4, 320000.00, '2026-01-12 18:00:00'), -- complément

-- Paiements des réservations passées
(6, 320000.00, '2026-01-03 10:30:00'),
(7, 280000.00, '2026-01-03 11:30:00'),
(8, 360000.00, '2026-01-05 09:30:00'),
(9, 180000.00, '2026-01-05 10:30:00'),
(10, 500000.00, '2026-01-08 12:30:00'),
(11, 160000.00, '2026-01-08 13:30:00');

-- 14. Configurations
INSERT INTO configurations (cle, valeur) VALUES
('devise', 'MGA'),
('prix_litre_carburant', '5000'),
('delai_annulation', '24h'),
('commission_reservation', '5'),
('tva', '20');