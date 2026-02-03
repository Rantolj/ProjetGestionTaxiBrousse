-- CREATE DATABASE taxibrousse;
-- \c taxibrousse

CREATE TABLE IF NOT EXISTS personnes
(
    id      SERIAL PRIMARY KEY,
    nom     VARCHAR(100) NOT NULL,
    prenom  VARCHAR(100) NOT NULL,
    contact VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS utilisateurs
(
    id           SERIAL PRIMARY KEY,
    personne_id  INTEGER REFERENCES personnes (id),
    email        VARCHAR(255) NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS clients
(
    id          SERIAL PRIMARY KEY,
    personne_id INTEGER REFERENCES personnes (id)
);

CREATE TABLE IF NOT EXISTS chauffeurs
(
    id          SERIAL PRIMARY KEY,
    personne_id INTEGER REFERENCES personnes (id)
);

CREATE TABLE IF NOT EXISTS arrets
(
    id       SERIAL PRIMARY KEY,
    nom      VARCHAR(100) NOT NULL, -- 'TANA'
    est_gare BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS trajets
(
    id       SERIAL PRIMARY KEY,
    nom      VARCHAR(100)   NOT NULL, -- 'TANA - MAJUNGA'q
    distance DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS trajet_details
(
    id        SERIAL PRIMARY KEY,
    trajet_id INTEGER REFERENCES trajets (id),
    arret_id  INTEGER REFERENCES arrets (id),
    ordre     INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS categories_tb
(
    id  SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL -- 'Standard', 'VIP'
);

CREATE TABLE IF NOT EXISTS taxi_brousses
(
    id                 SERIAL PRIMARY KEY,
    immatriculation    VARCHAR(20)   NOT NULL,
    categorie_id       INTEGER REFERENCES categories_tb (id),
    nbr_places         INTEGER       NOT NULL,
    charge_max         DECIMAL(7, 2) NOT NULL DEFAULT 0,
    consommation       DECIMAL(5, 2) NOT NULL DEFAULT 0, -- L/100km
    disposition_places VARCHAR(255)  NOT NULL            -- xxoo/oooo/ooxo/ooxo/ooxo/oooo
);

CREATE TABLE IF NOT EXISTS categories_places
(
    id  SERIAL PRIMARY KEY,
    taxi_brousse_id INTEGER REFERENCES taxi_brousses (id),
    type VARCHAR(50) NOT NULL, -- 'STANDARD', 'PREMIUM'
    nbr_places_type INTEGER NOT NULL,
    prix_par_type DECIMAL(15,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS tarifs_places
(
    id             SERIAL PRIMARY KEY,
    trajet_id      INTEGER REFERENCES trajets (id),
    type_place     VARCHAR(50) NOT NULL, -- 'STANDARD', 'PREMIUM'
    montant        DECIMAL(15, 2) NOT NULL,
    date_effective DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE IF NOT EXISTS frais
(
    id             SERIAL PRIMARY KEY,
    trajet_id      INTEGER REFERENCES trajets (id),
    categorie_id   INTEGER REFERENCES categories_tb (id),
    montant        DECIMAL(10, 2) NOT NULL DEFAULT 0,
    date_effective DATE           NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE IF NOT EXISTS voyages
(
    id              SERIAL PRIMARY KEY,
    taxi_brousse_id INTEGER REFERENCES taxi_brousses (id),
    chauffeur_id    INTEGER REFERENCES chauffeurs (id),
    trajet_id       INTEGER REFERENCES trajets (id),
    date_depart     TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS reservations
(
    id               SERIAL PRIMARY KEY,
    voyage_id        INTEGER REFERENCES voyages (id),
    client_id        INTEGER REFERENCES clients (id),
    montant_total    DECIMAL(10, 2) NOT NULL, -- dénormalisation
    date_reservation TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS details_reservations
(
    id             SERIAL PRIMARY KEY,
    reservation_id INTEGER REFERENCES reservations (id),
    numero_place   VARCHAR(10) NOT NULL,
    personne_id    INTEGER REFERENCES personnes (id),
    type_place     VARCHAR(50),
    is_enfant      BOOLEAN NOT NULL DEFAULT FALSE,--nouv 16/01/2026
    passager_categorie VARCHAR(50) NOT NULL DEFAULT 'ADULTE', -- ADULTE, ENFANT, JEUNE, SENIOR, ETUDIANT
    prix_unitaire  DECIMAL(15, 2) -- prix calculé avec réduction selon catégorie
);

CREATE TABLE IF NOT EXISTS paiements
(
    id             SERIAL PRIMARY KEY,
    reservation_id INTEGER REFERENCES reservations (id),
    montant_paye   DECIMAL(10, 2) NOT NULL,
    date_paiement  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS configurations
(
    id     SERIAL PRIMARY KEY,
    cle    VARCHAR(100) NOT NULL,
    valeur VARCHAR(255) NOT NULL
);

-- 1) annonceurs
CREATE TABLE IF NOT EXISTS annonceurs (
  id SERIAL PRIMARY KEY,
  nom VARCHAR(200) NOT NULL,
  contact VARCHAR(200),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2) diffusions_publicitaires (contrat/accord publicitaire avec un annonceur)
CREATE TABLE IF NOT EXISTS diffusions_publicitaires (
  id SERIAL PRIMARY KEY,
  annonceur_id INTEGER NOT NULL REFERENCES annonceurs(id),
  prix_unitaire DECIMAL(15,2) NOT NULL,    -- prix par diffusion
  note TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3) details_diffusion (diffusions effectives liées aux voyages)
CREATE TABLE IF NOT EXISTS details_diffusion (
  id SERIAL PRIMARY KEY,
  diffusion_publicitaire_id INTEGER NOT NULL REFERENCES diffusions_publicitaires(id),
  voyage_id INTEGER NOT NULL REFERENCES voyages(id),
  nb_diffusions INTEGER NOT NULL DEFAULT 1,
  montant_total DECIMAL(15,2) NOT NULL DEFAULT 0, -- calculé: nb_diffusions * prix_unitaire du contrat
  date_diffusion DATE NOT NULL DEFAULT CURRENT_DATE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table pour les paiements effectués par les annonceurs
CREATE TABLE IF NOT EXISTS paiements_annonceurs (
  id SERIAL PRIMARY KEY,
  annonceur_id INTEGER NOT NULL REFERENCES annonceurs(id),
  montant_paye DECIMAL(15,2) NOT NULL,
  date_paiement DATE NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

        
-- MIGRATION: add `passager_categorie` to existing databases and backfill from `is_enfant`.
-- Run these statements once against your existing database (psql or migration tool):
-- Add column if not exists (keeps default for new rows)
ALTER TABLE details_reservations ADD COLUMN IF NOT EXISTS passager_categorie VARCHAR(50) DEFAULT 'ADULTE';
-- Backfill based on existing flag
UPDATE details_reservations SET passager_categorie = 'ENFANT' WHERE is_enfant = TRUE;
UPDATE details_reservations SET passager_categorie = 'ADULTE' WHERE passager_categorie IS NULL;
-- Make column NOT NULL to enforce presence
ALTER TABLE details_reservations ALTER COLUMN passager_categorie SET NOT NULL;
-- Add prix_unitaire column for audit trail
ALTER TABLE details_reservations ADD COLUMN IF NOT EXISTS prix_unitaire DECIMAL(15, 2);
-- Note: after verification you may remove `is_enfant` column in a later migration.

-- =============================================================================
-- VENTE DE PRODUITS EXTRA (eau, snacks, etc.)
-- =============================================================================

-- Table des produits (libellés)
CREATE TABLE IF NOT EXISTS produits (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(200) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des prix des produits (historique des prix)
CREATE TABLE IF NOT EXISTS prix_produits (
    id SERIAL PRIMARY KEY,
    produit_id INTEGER NOT NULL REFERENCES produits(id),
    prix DECIMAL(15, 2) NOT NULL,
    date_effective DATE NOT NULL DEFAULT CURRENT_DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des ventes (en-tête de vente)
CREATE TABLE IF NOT EXISTS ventes_produits (
    id SERIAL PRIMARY KEY,
    date_vente DATE NOT NULL DEFAULT CURRENT_DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des détails de vente (lignes de vente)
CREATE TABLE IF NOT EXISTS vente_produit_details (
    id SERIAL PRIMARY KEY,
    vente_id INTEGER NOT NULL REFERENCES ventes_produits(id),
    produit_id INTEGER NOT NULL REFERENCES produits(id),
    quantite INTEGER NOT NULL DEFAULT 1,
    prix_unitaire DECIMAL(15, 2) NOT NULL,  -- prix au moment de la vente
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des paiements pour les ventes de produits
CREATE TABLE IF NOT EXISTS paiements_produits (
    id SERIAL PRIMARY KEY,
    vente_id INTEGER NOT NULL REFERENCES ventes_produits(id),
    montant_paye DECIMAL(15, 2) NOT NULL,
    date_paiement DATE NOT NULL DEFAULT CURRENT_DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


