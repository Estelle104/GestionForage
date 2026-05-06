CREATE DATABASE forage_db;

------------ 

CREATE TABLE demande(
    id SERIAL PRIMARY KEY,
    reference VARCHAR(25) UNIQUE NOT NULL,
    nom_demandeur VARCHAR(50) NOT NULL,
    lieu_forage VARCHAR(50) NOT NULL,
    date_demande TIMESTAMP NOT NULL    
);

CREATE TABLE status(
    id SERIAL PRIMARY KEY,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE status_demande(
    id SERIAL PRIMARY KEY,
    reference_demande VARCHAR(25) NOT NULL,
    status_id INT NOT NULL,
    date_status TIMESTAMP NOT NULL,
    FOREIGN KEY (reference_demande) REFERENCES demande(reference),
    FOREIGN KEY (status_id) REFERENCES status(id)
); 