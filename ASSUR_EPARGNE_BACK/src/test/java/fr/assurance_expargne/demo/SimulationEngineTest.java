package fr.assurance_expargne.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import fr.assurance_expargne.demo.DTO.YearResult;
import fr.assurance_expargne.demo.Model.BonusMode;
import fr.assurance_expargne.demo.Service.SimulationEngine;

public class SimulationEngineTest {

    private final SimulationEngine simulationEngine = new SimulationEngine();
    
    @Test
    void dossierFixe20Ans() {
        List<YearResult> resultats = simulationEngine.simuler(1061, 0, 0.02, 20,
                BonusMode.FIXE, 0.10, 0, 0, 0, Set.of());

        assertEquals(20, resultats.size());

        YearResult derniere = resultats.get(resultats.size() - 1);
        assertEquals(2578, derniere.capital(), 1.0);
    }

    @Test
    void versementsLibresSansBonus() {
        // 100 €/mois, aucun bonus (mode AUCUN), rendement de 2 %, 2 ans
        List<YearResult> resultats = simulationEngine.simuler(0, 100, 0.02, 2,
                BonusMode.AUCUN, 0, 0, 0, 0, Set.of());

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
        assertEquals(100 , annee1.bonusHabitation(), 0.01);  // l'habitation le perd
        assertEquals(300, resultats.get(1).bonus(), 0.01);    // 100 + (100 + 100)

        // dossierFixe20Ans : toute la cotisation sur un seul contrat, l'autre à 0
        simulationEngine.simuler(new Contrat(1061, Set.of()), new Contrat(0, Set.of()), 0, 0.02, 20,
                BonusMode.FIXE, 0.10, 0, 0, 0);

        // versementsLibresSansBonus
        simulationEngine.simuler(new Contrat(0, Set.of()), new Contrat(0, Set.of()), 100, 0.02, 2,
        BonusMode.AUCUN, 0, 0, 0, 0);
    }

}

