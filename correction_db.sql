-- Option 1: Recréer complètement la base (RECOMMANDÉ)
-- Dans psql ou pgAdmin, exécutez ces commandes:

-- Se connecter à postgres (ou votre superuser)
-- DROP DATABASE IF EXISTS taxibrousse;
-- CREATE DATABASE taxibrousse;
-- \c taxibrousse

-- Puis exécuter schema.sql et data.sql

-- Option 2: Corriger la base existante
-- Si vous voulez garder des données, exécutez seulement:

-- Corriger la colonne charge_max
ALTER TABLE taxi_brousses ALTER COLUMN charge_max TYPE DECIMAL(7,2);

-- Vider seulement les tables problématiques
DELETE FROM voyages;
DELETE FROM taxi_brousses;

-- Puis réinsérer les données depuis data.sql (sections 8, 10, 11, 12, 13)