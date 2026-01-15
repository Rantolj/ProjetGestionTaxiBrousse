-- CREATE DATABASE taxibrousse
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
    numero_place   VARCHAR(10) NOT NULL
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


