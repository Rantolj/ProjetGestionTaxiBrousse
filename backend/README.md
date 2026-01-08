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

## Fonctionnalités initiales

- Structure Maven et dépendances (Spring Boot, Thymeleaf, JPA, PostgreSQL)
- Chargement automatique de `schema.sql` et `data.sql` à l'initialisation
- Entités JPA basiques pour toutes les tables
- Repositories JPA pour les entités principales
- Pages Thymeleaf : index et CRUD minimal pour `Personne`, `TaxiBrousse` et `Voyage`

## Tests

- Tests unitaires et d'intégration configurés (JUnit 5, MockMvc, DataJpaTest)
- Utilisation d'une BD en mémoire (H2) pour l'exécution des tests

## Utiliser Java 21

Le projet est maintenant configuré pour **Java 21**. Assurez-vous d'avoir JDK 21 installé et utilisé par votre IDE / Maven.

## Prochaines étapes proposées

- Ajouter services métiers et controllers supplémentaires
- Ajouter tests d'intégration plus complets
- Mettre en place configuration CI (GitHub Actions) si souhaité

Si vous voulez, je peux ajouter un job GitHub Actions pour exécuter `mvn test` sur chaque PR.
