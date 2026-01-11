-- Script de correction pour la base de données taxibrousse
-- À exécuter dans PostgreSQL pour corriger les erreurs

-- 1. Corriger la colonne charge_max dans taxi_brousses (si elle existe)
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns
               WHERE table_name = 'taxi_brousses' AND column_name = 'charge_max') THEN
        ALTER TABLE taxi_brousses ALTER COLUMN charge_max TYPE DECIMAL(7,2);
    END IF;
END $$;

-- 2. Vider les tables dans l'ordre inverse des dépendances pour recommencer proprement
DELETE FROM paiements;
DELETE FROM details_reservations;
DELETE FROM reservations;
DELETE FROM voyages;
DELETE FROM frais;
DELETE FROM taxi_brousses;
DELETE FROM categories_tb;
DELETE FROM trajet_details;
DELETE FROM trajets;
DELETE FROM arrets;
DELETE FROM chauffeurs;
DELETE FROM clients;
DELETE FROM utilisateurs;
DELETE FROM personnes;

-- Maintenant vous pouvez réexécuter data.sql
DELETE FROM personnes;

-- Maintenant vous pouvez réexécuter data.sql