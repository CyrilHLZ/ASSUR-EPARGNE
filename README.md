# Assur'Épargne

**Simulateur d'épargne fidélité pour un client d'assurance.**
Chaque année sans sinistre, l'assureur reverse un bonus (un pourcentage des cotisations) sur une épargne qui rapporte des intérêts. L'application calcule le capital obtenu au bout de N années et montre son évolution année par année.

Projet full-stack : une **API REST en Java / Spring Boot** et une **interface en React**.

### 👉 [Voir la démo en ligne](https://assur-epargne.onrender.com/)

> Hébergée sur l'offre gratuite de Render : après 15 minutes sans visite, le serveur s'endort et le premier chargement peut prendre une minute.

![Aperçu du simulateur](docs/apercu.png)

---

## Fonctionnalités

- Saisie des cotisations annuelles **auto et habitation**.
- Choix du **type de bonus** : progressif (taux de départ, hausse par année sans sinistre, plafond) ou fixe.
- Choix du **support d'épargne** : assurance vie (disponible à tout moment) ou PER (bloqué jusqu'à la retraite), avec les prélèvements sociaux de chacun.
- Réglage du **rendement**, des **versements libres**, de la **hausse annuelle des cotisations** et de la **durée** (5 à 30 ans) avec des curseurs, calcul en direct.
- **Grille des sinistres** auto et habitation : un clic sur une année déclare un sinistre, un second clic le retire.
- Résultats :
  - tuiles **capital au terme**, **bonus versés**, **intérêts** et **cotisations payées** ;
  - comparaison **avec ou sans Assur'Épargne** ;
  - **graphique** de l'évolution de l'épargne (Recharts) ;
  - **tableau** détaillé année par année, avec le taux du bonus.
- **Validation** des saisies côté API : une cotisation négative renvoie une erreur `400` avec un message par champ.

## Règle métier

Pour chaque année `n` de la simulation :

1. **Chaque contrat (auto, habitation) a son propre compteur** d'années sans sinistre. Un sinistre sur un contrat remet son compteur à 0, sans toucher à l'autre.
2. Le taux de bonus dépend du mode choisi :
   - `AUCUN` : pas de bonus ;
   - `FIXE` : un taux constant (ex. 10 %) ;
   - `VARIABLE` : `tauxDepart + increment × (annéesSansSinistre − 1)`, limité par un `plafond`.
   - Une année avec sinistre ne donne jamais de bonus.
3. Les cotisations augmentent chaque année : `cotisation de l'année n = cotisation saisie × (1 + hausse)^(n-1)`.
4. `bonus = cotisation auto de l'année × taux auto + cotisation habitation de l'année × taux habitation`
5. `capital = capital précédent × (1 + rendement) + bonus + versements libres de l'année`
6. À la sortie, les **prélèvements sociaux** ne portent que sur les intérêts : **17,2 %** en assurance vie, **18,6 %** en PER (taux 2026). L'API renvoie aussi le capital et les intérêts **nets**.

**Exemple :** 1 000 € de cotisations par an, bonus fixe de 10 %, rendement de 2 %.
L'année 1, le capital vaut 100 €. L'année 2, il vaut 100 × 1,02 + 100 = 202 €.

## Stack technique

| Partie | Technologies |
|---|---|
| Back | Java 21, Spring Boot 4, Spring Web MVC, Bean Validation, Maven |
| Tests | JUnit 5, MockMvc |
| Front | React 19, Vite, Recharts |
| Déploiement | Docker (build multi-étapes), Render |

## Architecture

```
ASSUR-EPARGNE/
├── ASSUR_EPARGNE_BACK/              API Spring Boot (port 8090)
│   └── src/main/java/fr/assurance_expargne/demo/
│       ├── Controller/   SimulationController    → POST /api/simulations/client
│       ├── Service/      ClientSimulationService → crée un Contrat par assurance, appelle le moteur
│       │                 SimulationEngine        → boucle année par année
│       │                 BonusCalculator         → taux de bonus selon le mode
│       ├── DTO/          objets de requête et de réponse (records Java)
│       ├── Model/        BonusMode (AUCUN, FIXE, VARIABLE), Contrat (cotisation + sinistres)
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

### Avec Docker (le plus simple)

Depuis la racine du dépôt :

```bash
docker build -t assur-epargne .
docker run --rm -p 9090:8090 assur-epargne
```

Ouvre ensuite `http://localhost:9090`. Le [Dockerfile](Dockerfile) construit le front, le place dans `src/main/resources/static` du back, puis lance un seul `.jar` qui sert l'interface et l'API. C'est la même image qui tourne en ligne.

### Sans Docker, en développement

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
  "cotisationAuto": 700,
  "cotisationHabitation": 300,
  "versementMensuel": 0,
  "hausseAnnuelle": 0.04,
  "sinistresAuto": [3],
  "sinistresHabitation": [],
  "rendement": 0.02,
  "duree": 3,
  "mode": "FIXE",
  "support": "ASSURANCE_VIE",
  "tauxFixe": 0.10,
  "tauxDepart": 0,
  "increment": 0,
  "plafond": 0
}
```

```json
{
  "capitalFinal": 242.57,
  "bonusTotal": 236.45,
  "versementsTotal": 0.0,
  "interetsTotal": 6.12,
  "cotisationsTotal": 3121.6,
  "support": "ASSURANCE_VIE",
  "tauxPrelevementsSociaux": 0.172,
  "prelevementsSociaux": 1.05,
  "interetsNets": 5.07,
  "capitalNet": 241.52,
  "detailParAnnee": [
    { "annee": 1, "capital": 100.0, "bonus": 100.0, "bonusAuto": 70.0, "bonusHabitation": 30.0,
      "tauxAuto": 0.1, "tauxHabitation": 0.1, "bonusCumul": 100.0, "versementsCumul": 0.0, "interets": 0.0,
      "cotisationAuto": 700.0, "cotisationHabitation": 300.0, "cotisationsCumul": 1000.0 },
    { "annee": 2, "capital": 206.0, "bonus": 104.0, "bonusAuto": 72.8, "bonusHabitation": 31.2,
      "tauxAuto": 0.1, "tauxHabitation": 0.1, "bonusCumul": 204.0, "versementsCumul": 0.0, "interets": 2.0,
      "cotisationAuto": 728.0, "cotisationHabitation": 312.0, "cotisationsCumul": 2040.0 },
    { "annee": 3, "capital": 242.57, "bonus": 32.45, "bonusAuto": 0.0, "bonusHabitation": 32.45,
      "tauxAuto": 0.0, "tauxHabitation": 0.1, "bonusCumul": 236.45, "versementsCumul": 0.0, "interets": 6.12,
      "cotisationAuto": 757.12, "cotisationHabitation": 324.48, "cotisationsCumul": 3121.6 }
  ]
}
```

À chaque année, `capital = bonusCumul + versementsCumul + interets`. Avec une hausse de 4 %, la cotisation auto passe de 700 € à 728 € puis 757,12 €. L'année 3, le sinistre auto ramène `tauxAuto` à 0, sans toucher au contrat habitation.

</details>

Si une cotisation est négative, l'API répond `400 Bad Request` :

```json
{ "cotisationAuto": "Le capital initial doit être supérieur à 0" }
```

## Pistes d'amélioration

- Utiliser `BigDecimal` pour les montants, au lieu de `double`.
- Vue « Côté assureur » : coût du bonus sur un portefeuille de clients.

## Auteur

**Cyril Houilliez** · [GitHub](https://github.com/CyrilHLZ)
