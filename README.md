# Assur'Épargne

**Simulateur d'épargne fidélité pour un client d'assurance.**
Chaque année sans sinistre, l'assureur reverse un bonus (un pourcentage des cotisations) sur une épargne qui rapporte des intérêts. L'application calcule le capital obtenu au bout de N années et montre son évolution année par année.

Projet full-stack : une **API REST en Java / Spring Boot** et une **interface en React**.

<!-- Ajoute une capture d'écran ici : ![Aperçu](docs/apercu.png) -->

---

## Fonctionnalités

- Saisie des cotisations annuelles **auto, habitation et santé**.
- Choix de la **durée** de simulation (1 à 40 ans) avec un curseur.
- **Grille des sinistres** : un clic sur une année déclare un sinistre, un second clic le retire.
- Résultats :
  - tuiles **capital final** et **total des bonus** ;
  - **graphique** de l'évolution de l'épargne (Recharts) ;
  - **tableau** détaillé année par année.
- **Validation** des saisies côté API : une cotisation négative renvoie une erreur `400` avec un message par champ.

## Règle métier

Pour chaque année `n` de la simulation :

1. Si l'année contient un sinistre, le compteur d'années sans sinistre repart à 0. Sinon, il augmente de 1.
2. Le taux de bonus dépend du mode choisi :
   - `AUCUN` : pas de bonus ;
   - `FIXE` : un taux constant (ex. 10 %) ;
   - `VARIABLE` : `tauxDepart + increment × (annéesSansSinistre − 1)`, limité par un `plafond`.
   - Une année avec sinistre ne donne jamais de bonus.
3. `bonus = cotisations totales × taux`
4. `capital = capital précédent × (1 + rendement) + bonus`

**Exemple :** 1 000 € de cotisations par an, bonus fixe de 10 %, rendement de 2 %.
L'année 1, le capital vaut 100 €. L'année 2, il vaut 100 × 1,02 + 100 = 202 €.

## Stack technique

| Partie | Technologies |
|---|---|
| Back | Java 21, Spring Boot 4, Spring Web MVC, Bean Validation, Maven |
| Tests | JUnit 5, MockMvc |
| Front | React 19, Vite, Recharts |

## Architecture

```
ASSUR-EPARGNE/
├── ASSUR_EPARGNE_BACK/              API Spring Boot (port 8090)
│   └── src/main/java/fr/assurance_expargne/demo/
│       ├── Controller/   SimulationController    → POST /api/simulations/client
│       ├── Service/      ClientSimulationService → additionne les cotisations, regroupe les sinistres
│       │                 SimulationEngine        → boucle année par année
│       │                 BonusCalculator         → taux de bonus selon le mode
│       ├── DTO/          objets de requête et de réponse (records Java)
│       ├── Model/        BonusMode (AUCUN, FIXE, VARIABLE)
│       └── Exception/    GlobalExceptionHandler  → erreurs 400 lisibles
│
└── ASSUR_EPARGNE_FRONT/             Interface React (port 5173)
    └── src/Service/
        ├── ProfilForm.jsx       formulaire, état et appel de l'API
        ├── GrilleSinistres.jsx  une case par année, clic = sinistre
        ├── Tuiles.jsx           capital final et total des bonus
        ├── Graphique.jsx        courbe de l'épargne
        └── TableauAnnees.jsx    détail par année
```

En développement, le **proxy Vite** redirige les appels `/api` du front vers `http://localhost:8090`. Le navigateur ne voit donc qu'une seule origine.

## Lancer le projet

**Prérequis :** Java 21 et Node.js 20 ou plus récent.

**1. Le back**

```bash
cd ASSUR_EPARGNE_BACK
./mvnw spring-boot:run          # Windows : mvnw.cmd spring-boot:run
```

L'API démarre sur `http://localhost:8090`.

**2. Le front** (dans un second terminal)

```bash
cd ASSUR_EPARGNE_FRONT
npm install
npm run dev
```

Ouvre ensuite `http://localhost:5173`.

**3. Les tests**

```bash
cd ASSUR_EPARGNE_BACK
./mvnw test
```

## API

`POST /api/simulations/client`

<details>
<summary>Exemple de requête et de réponse</summary>

```json
{
  "cotisationAuto": 600,
  "cotisationHabitation": 300,
  "cotisationSante": 100,
  "versementMensuel": 0,
  "sinistresAuto": [3],
  "sinistresHabitation": [],
  "sinistresSante": [],
  "rendement": 0.02,
  "duree": 3,
  "mode": "FIXE",
  "tauxFixe": 0.10,
  "tauxDepart": 0,
  "increment": 0,
  "plafond": 0
}
```

```json
{
  "capitalFinal": 206.04,
  "bonusTotal": 200.0,
  "detailParAnnee": [
    { "annee": 1, "capital": 100.0,  "bonus": 100.0 },
    { "annee": 2, "capital": 202.0,  "bonus": 100.0 },
    { "annee": 3, "capital": 206.04, "bonus": 0.0 }
  ]
}
```

</details>

Si une cotisation est négative, l'API répond `400 Bad Request` :

```json
{ "cotisationAuto": "Le capital initial doit être supérieur à 0" }
```

## Pistes d'amélioration

- Choisir le mode de bonus, le rendement et les taux depuis l'interface.
- Gérer les sinistres habitation et santé dans la grille.
- Mise en ligne (back et front déployés).

## Auteur

**Cyril Houilliez** · [GitHub](https://github.com/CyrilHLZ)
