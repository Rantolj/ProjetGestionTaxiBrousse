# Gestion Taxi-Brousse (Backend)

Projet Spring Boot pour la gestion des taxi-brousses à Madagascar.

## Ce que j'ai initialisé ✅
- Projet Maven Spring Boot (Java 17) avec dépendances Web, Thymeleaf, JPA et PostgreSQL
- `application.properties` avec configuration pour exécuter `schema.sql` et `data.sql` au démarrage
- `schema.sql` et `data.sql` (basés sur votre fichier fourni)
- Classe principale `TaxiBrousseApplication`

## Démarrage
1. Assurez-vous d'avoir une base PostgreSQL `taxibrousse` et mettez à jour les identifiants dans `application.properties`.
2. Depuis le dossier `backend`, lancez :

   mvn spring-boot:run

3. L'application écoutera sur `http://localhost:8080`.

## Prochaines étapes proposées
- Générer les entités JPA et les repositories
- Créer services et controllers
- Ajouter templates Thymeleaf pour les pages principales

Si vous voulez, je m'occupe maintenant des entités JPA et des repositories. Dites-moi si vous préférez que j'ajoute aussi des CRUD endpoints et des templates Thymeleaf directement.