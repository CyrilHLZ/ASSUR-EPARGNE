package fr.assurance_expargne.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import fr.assurance_expargne.demo.Model.BonusMode;
import fr.assurance_expargne.demo.Service.BonusCalculator;

class BonusCalculatorTest {

    private final BonusCalculator bonusCalculator = new BonusCalculator();

    @Test
    void progressifAnnee5() {
        double resultat = bonusCalculator.taux(BonusMode.VARIABLE, 0, 0.03, 0.005, 0.08, 5);
        assertEquals(0.05, resultat, 0.0001); // 0.03 + 0.005 × 4
    }

    @Test
    void plafondAnnee20() {
        double resultat = bonusCalculator.taux(BonusMode.VARIABLE, 0, 0.03, 0.005, 0.08, 20);
        assertEquals(0.08, resultat, 0.0001); // 0.125 calculé, bloqué au plafond
    }

    @Test
    void fixeToujoursIdentique() {
        assertEquals(0.10, bonusCalculator.taux(BonusMode.FIXE, 0.10, 0, 0, 0, 1), 0.0001);
        assertEquals(0.10, bonusCalculator.taux(BonusMode.FIXE, 0.10, 0, 0, 0, 15), 0.0001);
    }

    @Test
    void aucunBonusAnnee0() {
        double resultat = bonusCalculator.taux(BonusMode.VARIABLE, 0, 0.03, 0.005, 0.08, 0);
        assertEquals(0.0, resultat, 0.0001);
    }

    @Test
    void optionNonActivee() {
        double resultat = bonusCalculator.taux(BonusMode.AUCUN, 0.10, 0.03, 0.005, 0.08, 5);
        assertEquals(0.0, resultat, 0.0001);
    }
}
