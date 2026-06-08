# Gestion Forage
## Technologie
- Spring mvc
- postgres

## Database
    forage_db

## Table
Client
```
    id
    nom
    email
    telephone
```

Demande
```
    id
    reference
    id_client (mais client en spring)
    lieu_forage
    id_commune (mais commune en spring)
    date_demande (avec heure et minute)
    status_demande 
```

Status
```
    id
    libele
```

Status_Demande
```
    id
    id_demande (mais demande en spring)
    id_status (mais status en spring)
    date_status
```

Region
```
    id
    nom
```

District 
```
    id
    nom
    Region
```

Commune
```
    id
    nom
    District
```

Devis
```
    id
    id_demande
    id_type
    created_at
    observation
```

Details_Devis
```
    id
    description
    id_devis
    quantite
    unite
    prix_unitaire
    montant_par_ligne
```

## 05/05/26
### Objectif
- Creer un formulaire d'entree de demande

### Autre a faire
- structurer le model spring mvc
- creer les scripts de bases
- connexion avec la base
- les dependances

### Table
- Demande
- Status_Demande
- Status
- Commune

### Fonction
- CRUD pour demande
- read pour les autres tables
- validation des formulaires (require...)
- transaction entre demande et status_demande
    - quand on insere dans demande -> on met aussi dans status_demande (non reciproque)


## Sprint 2 - 08/05/26 
- Creation de devis
- Interface pour entree de details -> sortie : devis (cree)
    - Details: 
        qte, 
        unite, 
        prix_unitaire, 
        designation, 
        montant_par_ligne, 
        status

- a rectifier

## 12/05/26

### Devis
- id
- id_demande (FK)
- created_at
- id_type
- observation
    -> enregistrer -> enregistre dans devis et dans demande_status (status= devis_forage_cree par defaut , donc il faut inserer dans status tous les status qu'un devis peut avoir selon le type de devis (etude/forage))
- (sans montant mais a calculer automatiquement selon devis et details_devis)

***Remarque :***
- un devis a un type (devis etude / devis forage)
- un demande peut avoir plusieurs devis
- je dois aussi unifier le status donc il n'y a plus de status_devis mais seulement status
- tous les status doit etre tous au niveau demande
    -> devis n'a plus de status
    -> faire une vue pour savoir le status d'un devis (vue entre le status et le id_type dans devis pour avoir l'id du type tout en sachant sa valeur)


### Interface de creation de devis (create devis)
Formulaire de creation:

- Demande (onblur):ex : dmd01-> faire ajax pour prendre l'information de ce demande afin de les afficher en bas de demande onblur :
    - client
    - date
    - lieu
- Type devis
    - etude
    - forage
- observation
***Remarque :*** un type est associe a un status -> donc, faire un enum pour lier un type a un status


### Creation de Details_devis
- id_devis
- libele
- qte
- pu
- montant (calcul automatique)

Enregistre -> Devis, demande_status, details_devis (transaction)

### Comment ?
- pour la demande, quand on saisie un demande, on affiche dessous le client pour ce demande, le lieu et la date de creation de ce demande via ajax lors de onblur


- On cree un devis, il y a un bouton ajouter details, il y a des champs pour le details devis, on garde chaque details sur une variable js, et ainsi de suite et a la fin il y a un bouton enregistrer



## 19/05/26
IL y aura 2 interface :
- Ajout de status_demande
    - demande
    - date
    - observation
    - status (liste deroulante)

- Modification de status_demande
    - Demande ( liste deroulante )
    - Date
    - Observation
    - Status ( liste deroulante )


## Explication Aaron
interface 

Date ajout status

demande
ajax maka ny info momba anle demande

date : null = date jour avec minutes
Observation
Statut 





Modif
demande azo ovaina

date 

observation

Statut
 liste deroulante ny Statut mifanaraka amle demande



 =============================================================

 Suite 

 ao anaty demandeStatus lasa miampy colonne ray oe DureeTravaillee [OK]
 lister les demandeStatus par ordre croissant [OK]
 lister les demandeStatus par demande dans l'ordre croissant [OK]

 io D.T io calculena en minutes [OK]

 duree en minutes entre 2 status [OK]

 de ny fanaovana calcul:

 jour ouvrable ihany no raisina anaovana calcul (weekend tsy raisina) [OK]
 de ny ora fiasana: 08h->16h [OK]

 antsoina le fonction namboarina teo rehefa manao ajout demandeStatus vaovao, modification demandeStatus [OK]
 , ajout demande [OK]
 , modif demande [OK]
 , ajout devis [OK]
 , modif devis [OK]

update status set libelle = 'Devis Etude Cree' where libelle = 'Devis cree';

insert into status (libelle) values ('Devis Etude Accepte');
insert into status (libelle) values ('Devis Etude Refuse');

insert into status (libelle) values ('Devis Forage Cree');
insert into status (libelle) values ('Devis Forage Accepte');
insert into status (libelle) values ('Devis Forage Refuse');


manamboatra table Parametre [OK]
id_Status1
id_Status2
duree en minutes
Alerte

Modele [OK]

Repository [OK]

Service [WIP]

Ny zavatra affichena zany: liste demande misy Alerte [WIP]


Manamboatra api mande iray maka ny liste Demande misy alerte de triena 

atao sur php



------------------------------------------------------------------------------------------------

Manamboatra script de Reinitialisation 


Le couleur amle alerte atao hex de amzay afaka lokoina par rapport anzay le alerte