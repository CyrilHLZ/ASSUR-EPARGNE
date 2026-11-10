package fr.assurance_expargne.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import fr.assurance_expargne.demo.DTO.YearResult;
import fr.assurance_expargne.demo.Model.BonusMode;
import fr.assurance_expargne.demo.Model.Contrat;
import fr.assurance_expargne.demo.Service.SimulationEngine;

public class SimulationEngineTest {

    private final SimulationEngine simulationEngine = new SimulationEngine();

    @Test
    void dossierFixe20Ans() {
        // Toute la cotisation sur un seul contrat, l'autre à 0
        List<YearResult> resultats = simulationEngine.simuler(new Contrat(1061, Set.of()), new Contrat(0, Set.of()),
                0, 0.02, 20, BonusMode.FIXE, 0.10, 0, 0, 0);

        assertEquals(20, resultats.size());

        YearResult derniere = resultats.get(resultats.size() - 1);
        assertEquals(2578, derniere.capital(), 1.0);
    }

    @Test
    void versementsLibresSansBonus() {
        // 100 €/mois, aucun bonus (mode AUCUN), rendement de 2 %, 2 ans
        List<YearResult> resultats = simulationEngine.simuler(new Contrat(0, Set.of()), new Contrat(0, Set.of()),
                100, 0.02, 2, BonusMode.AUCUN, 0, 0, 0, 0);

        // Année 1 : 0 × 1,02 + 1 200 = 1 200
        assertEquals(1200, resultats.get(0).capital(), 0.01);
        // Année 2 : 1 200 × 1,02 + 1 200 = 2 424
        assertEquals(2424, resultats.get(1).capital(), 0.01);
    }

    @Test
    void sinistreHabitationNeTouchePasLeBonusAuto() {
        Contrat auto = new Contrat(1000, Set.of());
        Contrat habitation = new Contrat(1000, Set.of(1));   // sinistre l'année 1

        // Fixe 10 %, rendement 0 pour que le calcul se fasse de tête, 2 ans
        List<YearResult> resultats = simulationEngine.simuler(auto, habitation, 0, 0, 2,
                BonusMode.FIXE, 0.10, 0, 0, 0);

        YearResult annee1 = resultats.get(0);
        assertEquals(100, annee1.bonusAuto(), 0.01);        // l'auto garde son bonus
        assertEquals(0, annee1.bonusHabitation(), 0.01);    // l'habitation le perd
        assertEquals(300, resultats.get(1).capital(), 0.01); // 100 + (100 + 100)
    }

    @Test
    void cumulsEtInteretsParAnnee() {
        Contrat auto = new Contrat(1000, Set.of());
        Contrat habitation = new Contrat(0, Set.of());

        // Fixe 10 %, 100 €/mois de versements, rendement 2 %, 2 ans
        List<YearResult> resultats = simulationEngine.simuler(auto, habitation, 100, 0.02, 2,
                BonusMode.FIXE, 0.10, 0, 0, 0);

        // Année 1 : 0 × 1,02 + 100 (bonus) + 1 200 (versements) = 1 300, pas encore d'intérêts
        YearResult annee1 = resultats.get(0);
        assertEquals(1300, annee1.capital(), 0.01);
        assertEquals(100, annee1.bonusCumul(), 0.01);
        assertEquals(1200, annee1.versementsCumul(), 0.01);
        assertEquals(0, annee1.interets(), 0.01);           // 1 300 - 100 - 1 200
        assertEquals(0.10, annee1.tauxAuto(), 0.0001);

        // Année 2 : 1 300 × 1,02 + 100 + 1 200 = 2 626
        YearResult annee2 = resultats.get(1);
        assertEquals(2626, annee2.capital(), 0.01);
        assertEquals(200, annee2.bonusCumul(), 0.01);       // 100 + 100
        assertEquals(2400, annee2.versementsCumul(), 0.01); // 1 200 + 1 200
        assertEquals(26, annee2.interets(), 0.01);          // 2 626 - 200 - 2 400
    }
}
