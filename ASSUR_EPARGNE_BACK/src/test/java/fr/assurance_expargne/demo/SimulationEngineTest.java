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
        List<YearResult> resultats = simulationEngine.simuler(1061, 0.02, 20,
                BonusMode.FIXE, 0.10, 0, 0, 0, Set.of());

        assertEquals(20, resultats.size());

        YearResult derniere = resultats.get(resultats.size() - 1);
        assertEquals(2578, derniere.capital(), 1.0);
    }
}

