-- CREATE DATABASE forage_db;

-- TABLE CLIENT
CREATE TABLE client (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(150) NOT NULL,
    email VARCHAR(150) UNIQUE,
    telephone VARCHAR(30)
);


-- TABLE REGION
CREATE TABLE region (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(150) NOT NULL
);


-- TABLE DISTRICT
CREATE TABLE district (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(150) NOT NULL,
    id_region INTEGER NOT NULL,

    CONSTRAINT fk_district_region
        FOREIGN KEY (id_region)
        REFERENCES region(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);


-- TABLE COMMUNE
CREATE TABLE commune (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(150) NOT NULL,
    id_district INTEGER NOT NULL,

    CONSTRAINT fk_commune_district
        FOREIGN KEY (id_district)
        REFERENCES district(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);


-- TABLE STATUS
CREATE TABLE status (
    id SERIAL PRIMARY KEY,
    libele VARCHAR(100) NOT NULL UNIQUE,
    sigle VARCHAR(100)
);

CREATE TABLE type (
    id SERIAL PRIMARY KEY,
    libele VARCHAR(100) NOT NULL UNIQUE
);


-- TABLE DEMANDE
CREATE TABLE demande (
    id SERIAL PRIMARY KEY,
    reference VARCHAR(100) NOT NULL UNIQUE,
    id_client INTEGER NOT NULL,
    id_commune INTEGER NOT NULL,
    lieu_forage TEXT NOT NULL,
    date_demande TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_status INTEGER NOT NULL,

    CONSTRAINT fk_demande_client
        FOREIGN KEY (id_client)
        REFERENCES client(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_demande_commune
        FOREIGN KEY (id_commune)
        REFERENCES commune(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,


    CONSTRAINT fk_demande_status
        FOREIGN KEY (id_status)
        REFERENCES status(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);


-- TABLE STATUS_DEMANDE
CREATE TABLE status_demande (
    id SERIAL PRIMARY KEY,
    id_demande INTEGER NOT NULL,
    id_status INTEGER NOT NULL,
    date_status TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    duree_travail DECIMAL(10,2) DEFAULT 0.00, -- en minutes

    CONSTRAINT fk_status_demande_demande
        FOREIGN KEY (id_demande)
        REFERENCES demande(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_status_demande_status
        FOREIGN KEY (id_status)
        REFERENCES status(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE devis(
    id SERIAL PRIMARY KEY,
    id_demande INT NOT NULL,
    id_type INT NOT NULL,
    created_at TIMESTAMP ,
    observation TEXT,

    CONSTRAINT fk_devis_demande
        FOREIGN KEY (id_demande)
        REFERENCES demande(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_type_devis
        FOREIGN KEY (id_type)
        REFERENCES type(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE details_devis(
    id SERIAL PRIMARY KEY,
    designation TEXT NOT NULL,
    id_devis INT NOT NULL,
    quantite DECIMAL(10,2) NOT NULL,
    unite VARCHAR(20) NOT NULL,
    prix_unitaire DECIMAL(10,2) NOT NULL,
    montant_par_ligne DECIMAL(10,2), -- automatique

    CONSTRAINT fk_details_devis_devis
        FOREIGN KEY (id_devis)
        REFERENCES devis(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- CREATE TABLE parametre(
--     id_status1 INT NOT NULL,
--     id_status2 INT NOT NULL,   
--     duree_minute DECIMAL(10,2) NOT NULL,
--     alerte INT,
--     PRIMARY KEY (id_status1, id_status2),
--     CONSTRAINT fk_parametre_status1
--         FOREIGN KEY (id_status1)
--         REFERENCES status(id)
--         ON DELETE RESTRICT
--         ON UPDATE CASCADE,
--     CONSTRAINT fk_parametre_status2
--         FOREIGN KEY (id_status2)
--         REFERENCES status(id)
--         ON DELETE RESTRICT
--         ON UPDATE CASCADE
-- );

CREATE TABLE parametre (
    id_status1    INT            NOT NULL,
    id_status2    INT            NOT NULL,
    debut_minute  DECIMAL(10,2)  NOT NULL,
    fin_minute    DECIMAL(10,2)  NOT NULL,
    alerte        INT,

    PRIMARY KEY (id_status1, id_status2),

    CONSTRAINT fk_parametre_status1
        FOREIGN KEY (id_status1) REFERENCES status(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT fk_parametre_status2
        FOREIGN KEY (id_status2) REFERENCES status(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,

    CONSTRAINT chk_intervalle
        CHECK (fin_minute > debut_minute)
);

-- 1. Supprimer l'ancienne clé primaire
ALTER TABLE parametre DROP CONSTRAINT parametre_pkey;

-- 2. Ajouter une colonne id auto-incrémentée
ALTER TABLE parametre ADD COLUMN id SERIAL;

-- 3. Définir la nouvelle clé primaire sur id
ALTER TABLE parametre ADD CONSTRAINT parametre_pkey PRIMARY KEY (id);

-- 4. Ajouter un index unique pour éviter les doublons d'intervalles identiques
CREATE UNIQUE INDEX uq_parametre_intervalle 
ON parametre (id_status1, id_status2, debut_minute, fin_minute);