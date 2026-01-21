-- ==============================================================================
-- TARIFS PAR CATÉGORIE DE PASSAGER - Documentation Base de Données
-- ==============================================================================
-- Date: 16/01/2026
-- Description: Gestion des réductions tarifaires selon la catégorie de passager
-- ==============================================================================

-- TABLES CONCERNÉES:
-- ==================
-- 1. details_reservations - Stocke la catégorie de passager pour chaque place réservée
-- 2. tarifs_places - Contient les tarifs de base par type de place (STANDARD, PREMIUM, VIP)

-- COLONNE CLÉE DANS details_reservations:
-- =======================================
-- passager_categorie VARCHAR(50) NOT NULL DEFAULT 'ADULTE'
-- Valeurs possibles: ADULTE, ENFANT, JEUNE, SENIOR, ETUDIANT

-- RÈGLES DE RÉDUCTION APPLIQUÉES (côté métier JavaScript & Java):
-- ===============================================================
-- | Catégorie  | Réduction    | Remarque                        |
-- |------------|--------------|----------------------------------|
-- | ADULTE     | 0%           | Tarif plein                      |
-- | ENFANT     | 50%          | Uniquement sur STANDARD (50000Ar)|
-- | SENIOR     | 20%          | Sur tous les types de places     |
-- | JEUNE      | 10%          | Sur tous les types de places     |
-- | ETUDIANT   | 15%          | Sur tous les types de places     |

-- EXEMPLE DE CALCUL:
-- ==================
-- Tarif STANDARD base: 100 000 Ar
-- - ADULTE: 100 000 Ar (100%)
-- - ENFANT: 50 000 Ar (tarif fixe)
-- - SENIOR: 80 000 Ar (100 000 * 0.80)
-- - JEUNE: 90 000 Ar (100 000 * 0.90)
-- - ETUDIANT: 85 000 Ar (100 000 * 0.85)

-- Tarif VIP base: 200 000 Ar
-- - ADULTE: 200 000 Ar (100%)
-- - SENIOR: 160 000 Ar (200 000 * 0.80)
-- - JEUNE: 180 000 Ar (200 000 * 0.90)
-- - ETUDIANT: 170 000 Ar (200 000 * 0.85)

-- REQUÊTES UTILES:
-- ================

-- Voir les réservations par catégorie de passager
SELECT 
    r.id AS reservation_id,
    p.nom || ' ' || p.prenom AS client,
    dr.numero_place,
    dr.type_place,
    dr.passager_categorie,
    r.montant_total
FROM reservations r
JOIN clients c ON r.client_id = c.id
JOIN personnes p ON c.personne_id = p.id
JOIN details_reservations dr ON dr.reservation_id = r.id
ORDER BY r.date_reservation DESC;

-- Statistiques des réservations par catégorie
SELECT 
    passager_categorie,
    COUNT(*) AS nombre_places,
    type_place
FROM details_reservations
GROUP BY passager_categorie, type_place
ORDER BY passager_categorie, type_place;

-- Vérifier les tarifs actuels
SELECT 
    t.nom AS trajet,
    tp.type_place,
    tp.montant,
    tp.date_effective
FROM tarifs_places tp
JOIN trajets t ON tp.trajet_id = t.id
WHERE tp.date_effective <= CURRENT_DATE
ORDER BY t.nom, tp.type_place;

-- OPTIONNEL: Créer une table de réductions (si on veut les stocker en base)
-- =========================================================================
-- Cette approche permet de modifier les réductions sans changer le code

-- CREATE TABLE IF NOT EXISTS reductions_passager (
--     id SERIAL PRIMARY KEY,
--     categorie VARCHAR(50) NOT NULL UNIQUE,
--     reduction_percent DECIMAL(5,2) NOT NULL DEFAULT 0,
--     description VARCHAR(255),
--     date_effective DATE NOT NULL DEFAULT CURRENT_DATE
-- );

-- INSERT INTO reductions_passager (categorie, reduction_percent, description) VALUES
-- ('ADULTE', 0, 'Tarif plein'),
-- ('ENFANT', 50, 'Tarif enfant - uniquement sur STANDARD'),
-- ('SENIOR', 20, 'Tarif senior -20%'),
-- ('JEUNE', 10, 'Tarif jeune -10%'),
-- ('ETUDIANT', 15, 'Tarif étudiant -15%');

-- ==============================================================================
-- FIN DU SCRIPT
-- ==============================================================================
