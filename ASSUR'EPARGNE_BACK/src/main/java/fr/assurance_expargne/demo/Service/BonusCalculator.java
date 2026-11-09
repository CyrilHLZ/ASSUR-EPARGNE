package fr.assurance_expargne.demo.Service;

import fr.assurance_expargne.demo.Model.BonusMode;

public class BonusCalculator {

    public double taux(BonusMode mode, double tauxFixe, double tauxDepart,
                       double increment, double plafond, int annee) {

        if (mode == BonusMode.AUCUN) { return 0.0; }
        if (annee <= 0) { return 0.0; }
        if (mode == BonusMode.FIXE) { return tauxFixe; }
        double calcul = tauxDepart + increment * (annee - 1);
        return Math.min(calcul, plafond);
    }
}
