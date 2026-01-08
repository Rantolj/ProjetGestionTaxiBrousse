CREATE TABLE personnes 
(
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    contact VARCHAR(100)
);

CREATE TABLE clients 
(
    id SERIAL PRIMARY KEY,
    personne_id INTEGER REFERENCES personnes(id)
);

CREATE TABLE chauffeurs 
(
    id SERIAL PRIMARY KEY,
    personne_id INTEGER REFERENCES personnes(id)
);

CREATE TABLE arrets
(
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL -- 'TANA'
);

CREATE TABLE trajets
(
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL, -- 'TANA - MAJUNGA'
    distance DECIMAL(10,2) NOT NULL
);

CREATE TABLE trajet_details
(
    id SERIAL PRIMARY KEY,
    trajet_id INTEGER REFERENCES trajets(id),
    arret_id INTEGER REFERENCES arrets(id),
    ordre INTEGER NOT NULL
);

CREATE TABLE categories_tb
(
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL -- 'Standard', 'VIP'
);

CREATE TABLE taxi_brousses
(
    id SERIAL PRIMARY KEY,
    immatriculation VARCHAR(20) NOT NULL,
    categorie_id INTEGER REFERENCES categories_tb(id),
    nbr_places INTEGER NOT NULL,
    charge_max DECIMAL(5,2) NOT NULL DEFAULT 0,
    consommation DECIMAL(5,2) NOT NULL DEFAULT 0, -- L/100km
    disposition_places VARCHAR(255) NOT NULL -- xxoo/oooo/ooxo/ooxo/ooxo/oooo
);

CREATE TABLE frais
(
    id SERIAL PRIMARY KEY,
    trajet_id INTEGER REFERENCES trajets(id),
    categorie_id INTEGER REFERENCES categories_tb(id),
    montant DECIMAL(10,2) NOT NULL DEFAULT 0,
    date_effective DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE voyages 
(
    id SERIAL PRIMARY KEY,
    taxi_brousse_id INTEGER REFERENCES taxi_brousses(id),
    chauffeur_id INTEGER REFERENCES chauffeurs(id),
    trajet_id INTEGER REFERENCES trajets(id),
    date_depart TIMESTAMP NOT NULL
);

CREATE TABLE reservations 
(
    id SERIAL PRIMARY KEY,
    voyage_id INTEGER REFERENCES voyages(id),
    client_id INTEGER REFERENCES clients(id),
    montant_total DECIMAL(10,2) NOT NULL, -- dénormalisation
    date_reservation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE details_reservations 
(
    id SERIAL PRIMARY KEY,
    reservation_id INTEGER REFERENCES reservations(id),
    numero_place VARCHAR(10) NOT NULL
);

CREATE TABLE paiements
(
    id SERIAL PRIMARY KEY,
    reservation_id INTEGER REFERENCES reservations(id),
    montant_paye DECIMAL(10,2) NOT NULL,
    date_paiement TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE configurations
(
    id SERIAL PRIMARY KEY,
    cle VARCHAR(100) NOT NULL,
    valeur VARCHAR(255) NOT NULL
);