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
(2, 'PREMIUM', 4, 140000.00),
(2, 'STANDARD', 10, 80000.00);

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
(7, 'STANDARD', 90000.00, '2024-01-01');






-- 15. Annonceurs (publicités) et leurs diffusions
-- Insérer annonceurs
INSERT INTO annonceurs (nom) VALUES ('Vaniala'), ('Lewis');
INSERT INTO annonceurs (nom) VALUES ('Socobis'), ('Jejoo');

-- Diffusions publicitaires (contrats): coût unitaire 100000 Ar
INSERT INTO diffusions_publicitaires (annonceur_id, prix_unitaire, note)
VALUES
  ((SELECT id FROM annonceurs WHERE nom='Vaniala' LIMIT 1), 100000.00, 'Contrat janvier 2026'),
  ((SELECT id FROM annonceurs WHERE nom='Lewis'  LIMIT 1), 100000.00, 'Contrat janvier 2026');


INSERT INTO categories_tb (nom) VALUES
('Standard'),
('VIP');

-- 2) Taxi 1244-TBK (si absent)
INSERT INTO taxi_brousses (immatriculation, categorie_id, nbr_places, charge_max, consommation, disposition_places)
SELECT '1244-TBK', 1, 25, 3500.00, 8.5, 'xxVV/PPPP/SSxS/SSxS/SSSS'
WHERE NOT EXISTS (SELECT 1 FROM taxi_brousses WHERE immatriculation = '1244-TBK');

-- 2bis) Categories de places pour taxi 1244-TBK (prix STANDARD = 50,000 Ar)
INSERT INTO categories_places (taxi_brousse_id, type, nbr_places_type, prix_par_type)
SELECT tb.id, 'VIP', 2, 180000.00
FROM taxi_brousses tb
WHERE tb.immatriculation = '1244-TBK'
  AND NOT EXISTS (SELECT 1 FROM categories_places cp WHERE cp.taxi_brousse_id = tb.id AND cp.type = 'VIP');

INSERT INTO categories_places (taxi_brousse_id, type, nbr_places_type, prix_par_type)
SELECT tb.id, 'PREMIUM', 4, 100000.00
FROM taxi_brousses tb
WHERE tb.immatriculation = '1244-TBK'
  AND NOT EXISTS (SELECT 1 FROM categories_places cp WHERE cp.taxi_brousse_id = tb.id AND cp.type = 'PREMIUM');

INSERT INTO categories_places (taxi_brousse_id, type, nbr_places_type, prix_par_type)
SELECT tb.id, 'STANDARD', 19, 50000.00
FROM taxi_brousses tb
WHERE tb.immatriculation = '1244-TBK'
  AND NOT EXISTS (SELECT 1 FROM categories_places cp WHERE cp.taxi_brousse_id = tb.id AND cp.type = 'STANDARD');

  -- 3) Voyages (three voyages requested)
INSERT INTO voyages (taxi_brousse_id, chauffeur_id, trajet_id, date_depart)
SELECT
  (SELECT id FROM taxi_brousses WHERE immatriculation = '1244-TBK' LIMIT 1),
  1, -- use chauffeur_id 1 (tu peux changer si nécessaire)
  (SELECT id FROM trajets WHERE nom = 'TNR - Toamasina' LIMIT 1),
  '2026-01-20 10:00:00'
WHERE NOT EXISTS (
  SELECT 1 FROM voyages v
  WHERE v.taxi_brousse_id = (SELECT id FROM taxi_brousses WHERE immatriculation = '1244-TBK' LIMIT 1)
    AND v.date_depart = '2026-01-20 10:00:00'
);

INSERT INTO voyages (taxi_brousse_id, chauffeur_id, trajet_id, date_depart)
SELECT
  (SELECT id FROM taxi_brousses WHERE immatriculation = '1244-TBK' LIMIT 1),
  1,
  (SELECT id FROM trajets WHERE nom = 'TNR - Toamasina' LIMIT 1),
  '2026-01-21 10:00:00'
WHERE NOT EXISTS (
  SELECT 1 FROM voyages v
  WHERE v.taxi_brousse_id = (SELECT id FROM taxi_brousses WHERE immatriculation = '1244-TBK' LIMIT 1)
    AND v.date_depart = '2026-01-21 10:00:00'
);

INSERT INTO voyages (taxi_brousse_id, chauffeur_id, trajet_id, date_depart)
SELECT
  (SELECT id FROM taxi_brousses WHERE immatriculation = '1244-TBK' LIMIT 1),
  1,
  (SELECT id FROM trajets WHERE nom = 'TNR - Toamasina' LIMIT 1),
  '2026-01-21 15:00:00'
WHERE NOT EXISTS (
  SELECT 1 FROM voyages v
  WHERE v.taxi_brousse_id = (SELECT id FROM taxi_brousses WHERE immatriculation = '1244-TBK' LIMIT 1)
    AND v.date_depart = '2026-01-21 15:00:00'
);

-- 6) Contrats (diffusions_publicitaires) pour Socobis et Jejoo (prix unitaires choisis)
INSERT INTO diffusions_publicitaires (annonceur_id, prix_unitaire, note)
SELECT (SELECT id FROM annonceurs WHERE nom='Socobis' LIMIT 1), 100000.00, 'Contrat test Socobis'
WHERE NOT EXISTS (SELECT 1 FROM diffusions_publicitaires dp WHERE dp.annonceur_id = (SELECT id FROM annonceurs WHERE nom='Socobis' LIMIT 1));

INSERT INTO diffusions_publicitaires (annonceur_id, prix_unitaire, note)
SELECT (SELECT id FROM annonceurs WHERE nom='Jejoo' LIMIT 1), 100000.00, 'Contrat test Jejoo'
WHERE NOT EXISTS (SELECT 1 FROM diffusions_publicitaires dp WHERE dp.annonceur_id = (SELECT id FROM annonceurs WHERE nom='Jejoo' LIMIT 1));



-- 20 Jan 2026 10:00 -> Vaniala 1, Lewis 1 (use JOINs so we don't insert NULLs)
INSERT INTO details_diffusion (diffusion_publicitaire_id, voyage_id, nb_diffusions, montant_total, date_diffusion)
SELECT dp.id, v.id, 1, dp.prix_unitaire, CAST(v.date_depart AS DATE)
FROM diffusions_publicitaires dp
JOIN annonceurs a ON a.id = dp.annonceur_id AND a.nom = 'Vaniala'
JOIN voyages v ON v.date_depart = '2026-01-20 10:00:00'
WHERE NOT EXISTS (
  SELECT 1 FROM details_diffusion dd WHERE dd.voyage_id = v.id AND dd.diffusion_publicitaire_id = dp.id
);

INSERT INTO details_diffusion (diffusion_publicitaire_id, voyage_id, nb_diffusions, montant_total, date_diffusion)
SELECT dp.id, v.id, 1, dp.prix_unitaire, CAST(v.date_depart AS DATE)
FROM diffusions_publicitaires dp
JOIN annonceurs a ON a.id = dp.annonceur_id AND a.nom = 'Lewis'
JOIN voyages v ON v.date_depart = '2026-01-20 10:00:00'
WHERE NOT EXISTS (
  SELECT 1 FROM details_diffusion dd WHERE dd.voyage_id = v.id AND dd.diffusion_publicitaire_id = dp.id
);

-- 21 Jan 2026 10:00 -> Socobis 2, Jejoo 1
INSERT INTO details_diffusion (diffusion_publicitaire_id, voyage_id, nb_diffusions, montant_total, date_diffusion)
SELECT (SELECT id FROM diffusions_publicitaires WHERE annonceur_id = (SELECT id FROM annonceurs WHERE nom='Socobis' LIMIT 1) LIMIT 1),
       (SELECT id FROM voyages WHERE date_depart = '2026-01-21 10:00:00' LIMIT 1),
       2,
       2 * (SELECT prix_unitaire FROM diffusions_publicitaires WHERE annonceur_id = (SELECT id FROM annonceurs WHERE nom='Socobis' LIMIT 1) LIMIT 1),
       '2026-01-21'
WHERE NOT EXISTS (
  SELECT 1 FROM details_diffusion dd
  WHERE dd.voyage_id = (SELECT id FROM voyages WHERE date_depart = '2026-01-21 10:00:00' LIMIT 1)
    AND dd.diffusion_publicitaire_id = (SELECT id FROM diffusions_publicitaires WHERE annonceur_id = (SELECT id FROM annonceurs WHERE nom='Socobis' LIMIT 1) LIMIT 1)
);

INSERT INTO details_diffusion (diffusion_publicitaire_id, voyage_id, nb_diffusions, montant_total, date_diffusion)
SELECT (SELECT id FROM diffusions_publicitaires WHERE annonceur_id = (SELECT id FROM annonceurs WHERE nom='Jejoo' LIMIT 1) LIMIT 1),
       (SELECT id FROM voyages WHERE date_depart = '2026-01-21 10:00:00' LIMIT 1),
       1,
       1 * (SELECT prix_unitaire FROM diffusions_publicitaires WHERE annonceur_id = (SELECT id FROM annonceurs WHERE nom='Jejoo' LIMIT 1) LIMIT 1),
       '2026-01-21'
WHERE NOT EXISTS (
  SELECT 1 FROM details_diffusion dd
  WHERE dd.voyage_id = (SELECT id FROM voyages WHERE date_depart = '2026-01-21 10:00:00' LIMIT 1)
    AND dd.diffusion_publicitaire_id = (SELECT id FROM diffusions_publicitaires WHERE annonceur_id = (SELECT id FROM annonceurs WHERE nom='Jejoo' LIMIT 1) LIMIT 1)
);

-- 21 Jan 2026 15:00 -> 0 pub (aucune insertion)


-- 2) Détail des diffusions pour le voyage 20 Jan 2026 10:00 (Vaniala 1, Lewis 1)
INSERT INTO details_diffusion (diffusion_publicitaire_id, voyage_id, nb_diffusions, montant_total, date_diffusion)
SELECT dp.id,
       v.id,
       1,
       1 * dp.prix_unitaire,
       CAST(v.date_depart AS DATE)
FROM diffusions_publicitaires dp
JOIN annonceurs a ON a.id = dp.annonceur_id
JOIN voyages v ON v.date_depart = '2026-01-20 10:00:00'
WHERE a.nom = 'Vaniala'
  AND NOT EXISTS (
    SELECT 1 FROM details_diffusion dd
    WHERE dd.voyage_id = v.id AND dd.diffusion_publicitaire_id = dp.id
  );

INSERT INTO details_diffusion (diffusion_publicitaire_id, voyage_id, nb_diffusions, montant_total, date_diffusion)
SELECT dp.id,
       v.id,
       1,
       1 * dp.prix_unitaire,
       CAST(v.date_depart AS DATE)
FROM diffusions_publicitaires dp
JOIN annonceurs a ON a.id = dp.annonceur_id
JOIN voyages v ON v.date_depart = '2026-01-20 10:00:00'
WHERE a.nom = 'Lewis'
  AND NOT EXISTS (
    SELECT 1 FROM details_diffusion dd
    WHERE dd.voyage_id = v.id AND dd.diffusion_publicitaire_id = dp.id
  );

-- (Optionnel) Exemple de paiement pour tester le prorata (Vaniala paie 40% = 40% du total dû)
INSERT INTO paiements_annonceurs (annonceur_id, montant_paye, date_paiement)
SELECT a.id, (SELECT COALESCE(SUM(dd.montant_total),0) FROM details_diffusion dd WHERE dd.diffusion_publicitaire_id IN (SELECT id FROM diffusions_publicitaires WHERE annonceur_id = a.id) ) * 0.4, CURRENT_DATE
FROM annonceurs a
WHERE a.nom = 'Vaniala';


-- Détails des diffusions (liées aux voyages)
-- Voyage 1 (id=1): Tana-Majunga du 15 janvier 2026
INSERT INTO details_diffusion (diffusion_publicitaire_id, voyage_id, nb_diffusions, montant_total, date_diffusion) VALUES
(1, 1, 10, 1000000.00, '2026-01-15'),  -- Vaniala: 10 diffusions x 100000 = 1 000 000 Ar
(2, 1, 5, 500000.00, '2026-01-15');    -- Lewis: 5 diffusions x 100000 = 500 000 Ar

-- Voyage 2 (id=2): Tana-Toamasina du 15 janvier 2026 (si existe)
INSERT INTO details_diffusion (diffusion_publicitaire_id, voyage_id, nb_diffusions, montant_total, date_diffusion) VALUES
(1, 2, 8, 800000.00, '2026-01-15'),    -- Vaniala: 8 diffusions x 100000 = 800 000 Ar
(2, 2, 3, 300000.00, '2026-01-15');    -- Lewis: 3 diffusions x 100000 = 300 000 Ar

-- Voyage 3 (id=3)
INSERT INTO details_diffusion (diffusion_publicitaire_id, voyage_id, nb_diffusions, montant_total, date_diffusion) VALUES
(1, 3, 5, 500000.00, '2026-01-16'),    -- Vaniala: 5 diffusions x 100000 = 500 000 Ar
(2, 3, 4, 400000.00, '2026-01-16');    -- Lewis: 4 diffusions x 100000 = 400 000 Ar


-- 4. Arrêts (avec indication si c'est une gare routière)
-- Insère les arrêts demandés si ils n'existent pas
INSERT INTO arrets (nom, est_gare)
SELECT 'Gare routière Fasankarana (Tana)', TRUE
WHERE NOT EXISTS (SELECT 1 FROM arrets WHERE nom = 'Gare routière Fasankarana (Tana)');

INSERT INTO arrets (nom, est_gare)
SELECT 'Gare routière Majunga', TRUE
WHERE NOT EXISTS (SELECT 1 FROM arrets WHERE nom = 'Gare routière Majunga');

INSERT INTO arrets (nom, est_gare)
SELECT 'Gare routière Ambolomandinika Toamasina', TRUE
WHERE NOT EXISTS (SELECT 1 FROM arrets WHERE nom = 'Gare routière Ambolomandinika Toamasina');

-- Alias / code aéroport (optionnel)
INSERT INTO arrets (nom, est_gare)
SELECT 'TNR - Toamasina', TRUE
WHERE NOT EXISTS (SELECT 1 FROM arrets WHERE nom = 'TNR - Toamasina');

-- Ajout d'arrêts intermédiaires fréquents (optionnel)
INSERT INTO arrets (nom, est_gare)
SELECT 'Moramanga', FALSE
WHERE NOT EXISTS (SELECT 1 FROM arrets WHERE nom = 'Moramanga');

INSERT INTO arrets (nom, est_gare)
SELECT 'Brickaville', FALSE
WHERE NOT EXISTS (SELECT 1 FROM arrets WHERE nom = 'Brickaville');



--23/01/25
-- 1) Trajet TNR - Toamasina (si absent)
INSERT INTO trajets (nom, distance)
SELECT 'TNR - Toamasina', 365.0
WHERE NOT EXISTS (SELECT 1 FROM trajets WHERE nom = 'TNR - Toamasina');

-- Ajout des arrêts pour le trajet (Fasankarana id=1 -> Toamasina id=3)
INSERT INTO trajet_details (trajet_id, arret_id, ordre)
SELECT t.id, 1, 1
FROM trajets t
WHERE t.nom = 'TNR - Toamasina'
  AND NOT EXISTS (SELECT 1 FROM trajet_details td WHERE td.trajet_id = t.id AND td.ordre = 1);

INSERT INTO trajet_details (trajet_id, arret_id, ordre)
SELECT t.id, 3, 2
FROM trajets t
WHERE t.nom = 'TNR - Toamasina'
  AND NOT EXISTS (SELECT 1 FROM trajet_details td WHERE td.trajet_id = t.id AND td.ordre = 2);

-- 2ter) Tarifs pour le trajet TNR - Toamasina (prix STANDARD = 50,000 Ar)
INSERT INTO tarifs_places (trajet_id, type_place, montant, date_effective)
SELECT t.id, 'VIP', 180000.00, '2024-01-01'
FROM trajets t
WHERE t.nom = 'TNR - Toamasina'
  AND NOT EXISTS (SELECT 1 FROM tarifs_places tp WHERE tp.trajet_id = t.id AND tp.type_place = 'VIP');

INSERT INTO tarifs_places (trajet_id, type_place, montant, date_effective)
SELECT t.id, 'PREMIUM', 100000.00, '2024-01-01'
FROM trajets t
WHERE t.nom = 'TNR - Toamasina'
  AND NOT EXISTS (SELECT 1 FROM tarifs_places tp WHERE tp.trajet_id = t.id AND tp.type_place = 'PREMIUM');

INSERT INTO tarifs_places (trajet_id, type_place, montant, date_effective)
SELECT t.id, 'STANDARD', 50000.00, '2024-01-01'
FROM trajets t
WHERE t.nom = 'TNR - Toamasina'
  AND NOT EXISTS (SELECT 1 FROM tarifs_places tp WHERE tp.trajet_id = t.id AND tp.type_place = 'STANDARD');



-- 4) Réservations (tickets vendus)
-- prix billet adulte économique = 50 000 Ar
-- Voyage 20 Jan 2026 10:00 -> 40 billets => 40 * 50000 = 2 000 000
INSERT INTO reservations (voyage_id, client_id, montant_total, date_reservation)
SELECT (SELECT id FROM voyages WHERE date_depart = '2026-01-20 10:00:00' LIMIT 1),
       (SELECT id FROM clients LIMIT 1),
       2000000.00,
       '2026-01-18'
WHERE NOT EXISTS (SELECT 1 FROM reservations r WHERE r.voyage_id = (SELECT id FROM voyages WHERE date_depart = '2026-01-20 10:00:00' LIMIT 1) AND r.montant_total = 2000000.00);

-- Voyage 21 Jan 2026 10:00 -> 30 billets => 1 500 000
INSERT INTO reservations (voyage_id, client_id, montant_total, date_reservation)
SELECT (SELECT id FROM voyages WHERE date_depart = '2026-01-21 10:00:00' LIMIT 1),
       (SELECT id FROM clients LIMIT 1),
       1500000.00,
       '2026-01-19'
WHERE NOT EXISTS (SELECT 1 FROM reservations r WHERE r.voyage_id = (SELECT id FROM voyages WHERE date_depart = '2026-01-21 10:00:00' LIMIT 1) AND r.montant_total = 1500000.00);

-- Voyage 21 Jan 2026 15:00 -> 50 billets => 2 500 000
INSERT INTO reservations (voyage_id, client_id, montant_total, date_reservation)
SELECT (SELECT id FROM voyages WHERE date_depart = '2026-01-21 15:00:00' LIMIT 1),
       (SELECT id FROM clients LIMIT 1),
       2500000.00,
       '2026-01-19'
WHERE NOT EXISTS (SELECT 1 FROM reservations r WHERE r.voyage_id = (SELECT id FROM voyages WHERE date_depart = '2026-01-21 15:00:00' LIMIT 1) AND r.montant_total = 2500000.00);


--29/01/2026
-- Produits
INSERT INTO produits (libelle) VALUES
  ('Eau 50cl'),
  ('Tablette chocolat');

-- Prix (même jour pour simplifier)
INSERT INTO prix_produits (produit_id, prix, date_effective) VALUES
  (1, 5000,  '2026-01-21'),
  (2, 10000, '2026-01-21');

-- Ventes (3 ventes sur la période)
INSERT INTO ventes_produits (date_vente) VALUES
  ('2026-01-26');  -- vente 1

-- Détails des ventes
-- Vente 1 : 10 eaux -> 10 * 5000 = 50 000 Ar
INSERT INTO vente_produit_details (vente_id, produit_id, quantite, prix_unitaire) VALUES
  (1, 1, 150, 5000);

-- Vente 2 : 5 eaux + 3 chocolats -> 5*5000 + 3*10000 = 55 000 Ar
INSERT INTO vente_produit_details (vente_id, produit_id, quantite, prix_unitaire) VALUES
  (2, 1, 5, 5000),
  (2, 2, 3, 10000);

-- Vente 3 : 4 chocolats -> 4*10000 = 40 000 Ar
INSERT INTO vente_produit_details (vente_id, produit_id, quantite, prix_unitaire) VALUES
  (3, 2, 4, 10000);

-- Paiements produits (pour tester facturé vs encaissé)
-- Vente 1 : payée totalement
INSERT INTO paiements_produits (vente_id, montant_paye, date_paiement) VALUES
  (1, 450000, '2026-01-20');

-- Vente 2 : payée partiellement (25 000 sur 55 000)
INSERT INTO paiements_produits (vente_id, montant_paye, date_paiement) VALUES
  (2, 25000, '2026-01-21');

-- Vente 3 : rien encore payé

-- Résultat attendu sur la période 20/01/2026 – 21/01/2026 :
-- CA produits FACTURÉ   = 50 000 + 55 000 + 40 000 = 145 000 Ar
-- CA produits ENCAISSÉ  = 50 000 + 25 000         = 75 000 Ar
-- Reste à encaisser     = 145 000 - 75 000        = 70 000 Ar

