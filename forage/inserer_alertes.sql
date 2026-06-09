-- Configuration des 7 alertes pour les transitions de statuts
-- Les durées_minute sont à adapter selon votre besoin

-- Alerte 1: DEMANDE_CREEE (1) → DEVIS_ETUDE_CREE (2)
INSERT INTO parametre (id_status1, id_status2, duree_minute, alerte)
VALUES (1, 2, 480.00, 1);
-- 480 minutes = 8 heures

-- Alerte 2: DEVIS_ETUDE_CREE (2) → DEVIS_ETUDE_ACCEPTE (3)
INSERT INTO parametre (id_status1, id_status2, duree_minute, alerte)
VALUES (2, 3, 1440.00, 1);
-- 1440 minutes = 24 heures

-- Alerte 3: DEVIS_ETUDE_ACCEPTE (3) → DEVIS_ETUDE_REFUSE (4)
INSERT INTO parametre (id_status1, id_status2, duree_minute, alerte)
VALUES (3, 4, 2880.00, 1);
-- 2880 minutes = 48 heures

-- Alerte 4: DEVIS_ETUDE_REFUSE (4) → DEVIS_FORAGE_CREE (5)
INSERT INTO parametre (id_status1, id_status2, duree_minute, alerte)
VALUES (4, 5, 1440.00, 1);
-- 1440 minutes = 24 heures

-- Alerte 5: DEVIS_FORAGE_CREE (5) → DEVIS_FORAGE_ACCEPTE (6)
INSERT INTO parametre (id_status1, id_status2, duree_minute, alerte)
VALUES (5, 6, 2880.00, 1);
-- 2880 minutes = 48 heures

-- Alerte 6: DEVIS_FORAGE_ACCEPTE (6) → DEVIS_FORAGE_REFUSE (7)
INSERT INTO parametre (id_status1, id_status2, duree_minute, alerte)
VALUES (6, 7, 1440.00, 1);
-- 1440 minutes = 24 heures

-- Alerte 7: DEVIS_FORAGE_REFUSE (7) → DEMANDE_TERMINEE (8)
INSERT INTO parametre (id_status1, id_status2, duree_minute, alerte)
VALUES (7, 8, 5760.00, 1);
-- 5760 minutes = 96 heures (4 jours)

-- Note: Adaptez les durées_minute selon votre besoin
-- Les alertes deviennent ACTIVES quand duree_travail (dans status_demande) > duree_minute (ci-dessus)
