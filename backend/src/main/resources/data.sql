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
INSERT INTO taxi_brousses (immatriculation, categorie_id, nbr_places, charge_max, consommation, disposition_places) VALUES
('1234-TAB', 1, 25, 3500.00, 8.5, 'xxoo/oooo/ooxo/ooxo/ooxo/ooxo/ooxo/oooo'),
('5678-TAB', 2, 16, 2800.00, 7.2, 'xxoo/oooo/ooxo/ooxo/oooo'),
('9012-TAB', 1, 28, 4200.00, 9.0, 'xxoo/oooo/ooxo/ooxo/ooxo/ooxo/ooxo/ooxo/oooo');

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
(1, 1, 1, '2026-01-15 08:00:00'), -- Taxi 1, Chauffeur 1, Tana-Majunga

-- VOYAGE DEMANDE: Fasankarana vers Ambolomandinika le 14 janvier a 14h
-- 3 voitures differentes font ce meme voyage a la meme heure
(1, 2, 2, '2026-01-14 14:00:00'), -- Voiture 1 (1234-TAB, 25 places) - Chauffeur 2
(2, 3, 2, '2026-01-14 14:00:00'), -- Voiture 2 (5678-TAB, 16 places) - Chauffeur 3
(3, 4, 2, '2026-01-14 14:00:00'), -- Voiture 3 (9012-TAB, 28 places) - Chauffeur 4

-- Autres voyages
(3, 3, 3, '2026-01-17 07:00:00'), -- Taxi 3, Chauffeur 3, Tana-Diego
(1, 4, 4, '2026-01-18 10:00:00'), -- Taxi 1, Chauffeur 4, Tana-Fianarantsoa
(2, 5, 5, '2026-01-19 06:00:00'); -- Taxi 2, Chauffeur 5, Tana-Toliara

-- 11. Reservations
-- Reservations sur le voyage Fasankarana-Ambolomandinika 14h avec differentes voitures
INSERT INTO reservations (voyage_id, client_id, montant_total, date_reservation) VALUES
(1, 1, 25000.00, '2026-01-10 14:00:00'), -- Voyage Tana-Majunga, Client 1
(2, 2, 15000.00, '2026-01-11 15:00:00'), -- Voyage 14h Voiture 1 (1234-TAB), Client 2
(3, 3, 15000.00, '2026-01-12 16:00:00'), -- Voyage 14h Voiture 2 (5678-TAB), Client 3
(4, 4, 15000.00, '2026-01-12 17:00:00'), -- Voyage 14h Voiture 3 (9012-TAB), Client 4
(5, 5, 60000.00, '2026-01-13 17:00:00'); -- Voyage Tana-Diego, Client 5

-- 12. Details des reservations (places)
INSERT INTO details_reservations (reservation_id, numero_place) VALUES
(1, 'A1'),
(1, 'A2'),
(2, 'B1'),
(3, 'C1'),
(4, 'D1'),
(5, 'E1');

-- 13. Paiements
INSERT INTO paiements (reservation_id, montant_paye, date_paiement) VALUES
(1, 25000.00, '2026-01-10 14:30:00'),
(2, 25000.00, '2026-01-11 15:30:00'),
(3, 15000.00, '2026-01-12 16:30:00'),
(4, 60000.00, '2026-01-13 17:30:00'),
(5, 28000.00, '2026-01-14 18:30:00');

-- 14. Configurations
INSERT INTO configurations (cle, valeur) VALUES
('devise', 'MGA'),
('prix_litre_carburant', '5000'),
('delai_annulation', '24h');