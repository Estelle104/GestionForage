-- REGIONS (quelques régions de Madagascar)
INSERT INTO region (nom) VALUES
('Analamanga'),
('Vakinankaratra'),
('Itasy'),
('Bongolava'),
('Haute Matsiatra'),
('Amoron''i Mania'),
('Vatovavy'),
('Fitovinany'),
('Atsimo-Atsinanana'),
('Atsinanana'),
('Analanjirofo'),
('Alaotra-Mangoro'),
('Boeny'),
('Sofia'),
('Betsiboka'),
('Melaky'),
('Atsimo-Andrefana'),
('Androy'),
('Anosy'),
('Menabe'),
('Diana'),
('Sava');

-- DISTRICTS
INSERT INTO district (nom, id_region) VALUES
-- Analamanga (id=1)
('Antananarivo-Renivohitra', 1),
('Antananarivo-Avaradrano', 1),
('Antananarivo-Atsimondrano', 1),
('Ambohidratrimo', 1),
('Andramasina', 1),
('Manjakandriana', 1),
-- Vakinankaratra (id=2)
('Antsirabe I', 2),
('Antsirabe II', 2),
('Betafo', 2),
('Faratsiho', 2),
('Ambatolampy', 2),
-- Itasy (id=3)
('Miarinarivo', 3),
('Arivonimamo', 3),
('Soavinandriana', 3),
-- Bongolava (id=4)
('Tsiroanomandidy', 4),
('Fenoarivobe', 4),
-- Haute Matsiatra (id=5)
('Fianarantsoa I', 5),
('Fianarantsoa II', 5),
('Ambalavao', 5),
('Ikalamavony', 5),
-- Atsinanana (id=10)
('Toamasina I', 10),
('Toamasina II', 10),
('Brickaville', 10),
('Vatomandry', 10),
-- Boeny (id=13)
('Mahajanga I', 13),
('Mahajanga II', 13),
('Marovoay', 13),
-- Diana (id=21)
('Antsiranana I', 21),
('Antsiranana II', 21),
('Nosy Be', 21),
-- Sava (id=22)
('Sambava', 22),
('Antalaha', 22),
('Vohemar', 22);

-- COMMUNES
INSERT INTO commune (nom, id_district) VALUES
-- Antananarivo-Renivohitra (id=1)
('Antananarivo I', 1),
('Antananarivo II', 1),
('Antananarivo III', 1),
('Antananarivo IV', 1),
('Antananarivo V', 1),
('Antananarivo VI', 1),
-- Antananarivo-Avaradrano (id=2)
('Ambohimangakely', 2),
('Sabotsy Namehana', 2),
('Anjozorobe', 2),
('Talata Volonondry', 2),
-- Antananarivo-Atsimondrano (id=3)
('Androhibe', 3),
('Itaosy', 3),
('Ambohijanaka', 3),
('Fenoarivo', 3),
-- Ambohidratrimo (id=4)
('Ambohidratrimo', 4),
('Ankazobe', 4),
('Mahitsy', 4),
-- Antsirabe I (id=7)
('Antsirabe I', 7),
-- Antsirabe II (id=8)
('Antsirabe II', 8),
('Antanifotsy', 8),
('Vinaninony', 8),
-- Betafo (id=9)
('Betafo', 9),
('Mandoto', 9),
-- Miarinarivo (id=12)
('Miarinarivo', 12),
('Analavory', 12),
-- Tsiroanomandidy (id=15)
('Tsiroanomandidy', 15),
('Mahasolo', 15),
-- Fianarantsoa I (id=17)
('Fianarantsoa I', 17),
-- Fianarantsoa II (id=18)
('Fianarantsoa II', 18),
('Ampitatafika', 18),
-- Toamasina I (id=21)
('Toamasina I', 21),
-- Toamasina II (id=22)
('Toamasina II', 22),
('Ambodiriana', 22),
-- Mahajanga I (id=25)
('Mahajanga I', 25),
-- Mahajanga II (id=26)
('Mahajanga II', 26),
('Amborovy', 26),
-- Antsiranana I (id=28)
('Antsiranana I', 28),
-- Antsiranana II (id=29)
('Antsiranana II', 29),
('Ambanja', 29),
-- Nosy Be (id=30)
('Hellville', 30),
('Mahatsinjo', 30),
-- Sambava (id=31)
('Sambava', 31),
('Andapa', 31),
-- Antalaha (id=32)
('Antalaha', 32);

-- CLIENTS
INSERT INTO client (nom, email, telephone) VALUES
('Rakoto Jean', 'rakoto.jean@gmail.com', '+261 34 12 345 67'),
('Rabe Marie', 'rabe.marie@yahoo.fr', '+261 33 98 765 43'),
('Randria Paul', 'randria.paul@outlook.com', '+261 38 45 678 90'),
('Rasoa Hanta', 'rasoa.hanta@gmail.com', '+261 34 56 789 01'),
('Rakotondrabe Luc', 'rakotondrabe.luc@gmail.com', '+261 33 23 456 78'),
('Rajaonarison Nivo', 'rajaonarison.nivo@yahoo.fr', '+261 38 67 890 12'),
('Ramiandrisoa Soa', 'ramiandrisoa.soa@gmail.com', '+261 34 78 901 23'),
('Andriantsoa Eric', 'andriantsoa.eric@outlook.com', '+261 33 34 567 89'),
('Randriamanana Clotilde', 'randriamanana.clotilde@gmail.com', '+261 38 89 012 34'),
('Rasoamanarivo Fanja', 'rasoamanarivo.fanja@gmail.com', '+261 34 90 123 45'),
('Rakotoarisoa Tiana', 'rakotoarisoa.tiana@yahoo.fr', '+261 33 01 234 56'),
('Ratsimbazafy Herizo', 'ratsimbazafy.herizo@gmail.com', '+261 38 12 345 67'),
('Ramanantsoa Vonjy', 'ramanantsoa.vonjy@outlook.com', '+261 34 23 456 78'),
('Andriamaharo Sylvie', 'andriamaharo.sylvie@gmail.com', '+261 33 45 678 90'),
('Razafindrakoto Jules', 'razafindrakoto.jules@gmail.com', '+261 38 56 789 01'),
('Rakotondravony Annie', 'rakotondravony.annie@yahoo.fr', '+261 34 67 890 12'),
('Rabemananjara Fidy', 'rabemananjara.fidy@gmail.com', '+261 33 78 901 23'),
('Andrianjafy Mamy', 'andrianjafy.mamy@outlook.com', '+261 38 90 123 45'),
('Rasolofonaivo Brice', 'rasolofonaivo.brice@gmail.com', '+261 34 01 234 56'),
('Rakotoniaina Lala', 'rakotoniaina.lala@gmail.com', '+261 33 12 345 67');


-- INSERTION DES STATUS DE BASE


-- INSERTION DES STATUS DE BASE
INSERT INTO status(libele) VALUES
('DEMANDE_CREEE'),
('DEVIS_ETUDE-CREE'),
('DEVIS_ETUDE_ACCEPTE'),
('DEVIS_ETUDE_REFUSE'),
('DEVIS_FORAGE_CREE'),
('DEVIS_FORAGE_ACCEPTE'),
('DEVIS_FORAGE_REFUSE'),
('DEMANDE_TERMINEE');

-- INDEX UTILES
CREATE INDEX idx_demande_client
ON demande(id_client);

CREATE INDEX idx_demande_commune
ON demande(id_commune);

CREATE INDEX idx_status_demande_demande
ON status_demande(id_demande);

CREATE INDEX idx_status_demande_status
ON status_demande(id_status);


INSERT INTO type(libele) VALUES
('FORAGE'),
('ETUDE');