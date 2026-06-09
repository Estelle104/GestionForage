-- Alerte 1 : DEMANDE_CREEE (1) → DEVIS_ETUDE_CREE (2) — intervalle [0, 480 min]
INSERT INTO parametre (id_status1, id_status2, debut_minute, fin_minute, alerte)
VALUES (1, 2, 480.00, 600.00, 1);

INSERT INTO parametre (id_status1, id_status2, debut_minute, fin_minute, alerte)
VALUES (1, 2, 1440.00, 2880.00, 2);

-- Alerte 3 : DEVIS_ETUDE_ACCEPTE (3) → DEVIS_FORAGE_CREE (4) — intervalle [0, 2880 min]
INSERT INTO parametre (id_status1, id_status2, debut_minute, fin_minute, alerte)
VALUES (2, 3, 240.00, 300.00, 1);

-- Alerte 4 : DEVIS_FORAGE_CREE (4) → DEVIS_FORAGE_ACCEPTE (5) — intervalle [0, 1440 min]
INSERT INTO parametre (id_status1, id_status2, debut_minute, fin_minute, alerte)
VALUES (2, 3, 360.00, 480.00, 2);

-- Alerte 3 : DEVIS_ETUDE_ACCEPTE (3) → DEVIS_FORAGE_CREE (4) — intervalle [0, 2880 min]
INSERT INTO parametre (id_status1, id_status2, debut_minute, fin_minute, alerte)
VALUES (3, 4, 60.00, 120.00, 1);

-- Alerte 4 : DEVIS_FORAGE_CREE (4) → DEVIS_FORAGE_ACCEPTE (5) — intervalle [0, 1440 min]
INSERT INTO parametre (id_status1, id_status2, debut_minute, fin_minute, alerte)
VALUES (3, 4, 180.00, 240.00, 2);


-- Alerte 3 : DEVIS_ETUDE_ACCEPTE (3) → DEVIS_FORAGE_CREE (4) — intervalle [0, 2880 min]
INSERT INTO parametre (id_status1, id_status2, debut_minute, fin_minute, alerte)
VALUES (4, 5, 240.00, 480.00, 1);

-- Alerte 4 : DEVIS_FORAGE_CREE (4) → DEVIS_FORAGE_ACCEPTE (5) — intervalle [0, 1440 min]
INSERT INTO parametre (id_status1, id_status2, debut_minute, fin_minute, alerte)
VALUES (4, 5, 600.00, 720.00, 2);


-- Alerte 3 : DEVIS_ETUDE_ACCEPTE (3) → DEVIS_FORAGE_CREE (4) — intervalle [0, 2880 min]
INSERT INTO parametre (id_status1, id_status2, debut_minute, fin_minute, alerte)
VALUES (5, 6, 1200.00, 1800.00, 1);

-- Alerte 4 : DEVIS_FORAGE_CREE (4) → DEVIS_FORAGE_ACCEPTE (5) — intervalle [0, 1440 min]
INSERT INTO parametre (id_status1, id_status2, debut_minute, fin_minute, alerte)
VALUES (5, 6, 1801.00, 3600.00, 2);


-- Alerte 3 : DEVIS_ETUDE_ACCEPTE (3) → DEVIS_FORAGE_CREE (4) — intervalle [0, 2880 min]
INSERT INTO parametre (id_status1, id_status2, debut_minute, fin_minute, alerte)
VALUES (6, 7, 3600.00, 4200.00, 1);

-- Alerte 4 : DEVIS_FORAGE_CREE (4) → DEVIS_FORAGE_ACCEPTE (5) — intervalle [0, 1440 min]
INSERT INTO parametre (id_status1, id_status2, debut_minute, fin_minute, alerte)
VALUES (6, 7, 4800.00, 6000.00, 2);
