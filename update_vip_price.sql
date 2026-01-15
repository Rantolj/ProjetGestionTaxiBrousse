-- Script: mise à jour du prix VIP
-- Objectif: fixer le tarif VIP à 180000 Ar

-- 1) Tarifs par trajet (utilisé par le formulaire de réservation + CA Max)
-- a) Mettre VIP à 180000 pour TOUS les trajets
UPDATE tarifs_places
SET montant = 90000.00
WHERE UPPER(type_place) = 'STANDARD';

-- b) Variante: ne modifier que pour un trajet donné (décommenter et adapter)
-- UPDATE tarifs_places
-- SET montant = 180000.00
-- WHERE trajet_id = 2 AND UPPER(type_place) = 'VIP';

-- 2) Prix par type dans categories_places (optionnel)
-- Si vous utilisez aussi prix_par_type côté métier, gardez-le cohérent.
UPDATE categories_places
SET prix_par_type = 90000.00
WHERE UPPER(type) = 'STANDARD';
