package fr.assurance_expargne.demo.Service;

import java.util.ArrayList;
import java.util.List;

import fr.assurance_expargne.demo.Model.BonusMode;

public class BonusCalculator {
    
        public List<Double> capitalParAnnee(double cotisation, double tauxBonus,
                                        double rendement, int duree) {
        List<Double> resultats = new ArrayList<>();
        double capital = 0;

        for (int annee = 1; annee <= duree; annee++) {
            double bonus = cotisation * tauxBonus;
            capital = capital * (1 + rendement) + bonus;
            resultats.add(rendement);
        }
        return resultats;
    }

        public double taux(BonusMode mode, double tauxFixe, double tauxDepart,
                    double increment, double plafond, int annee) {

        if (mode == BonusMode.AUCUN) { return 0.0; }
        if (annee <= 0) { return 0.0; }
        if (mode == BonusMode.FIXE) { return tauxFixe; }
        double calcul = tauxDepart + increment * (annee - 1);
        return Math.min(calcul, plafond);
    }

}
