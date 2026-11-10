package fr.assurance_expargne.demo.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import fr.assurance_expargne.demo.DTO.YearResult;
import fr.assurance_expargne.demo.Model.BonusMode;

@Service 
public class SimulationEngine {
    

    private final BonusCalculator calculator = new BonusCalculator();

    public List<YearResult> simuler(Contrat auto, Contrat habitation,
                                double versementMensuel, double rendement, int duree,
                                BonusMode mode, double tauxFixe, double tauxDepart,
                                double increment, double plafond) {
    List<YearResult> resultats = new ArrayList<>();
    double capital = 0;
    int sansSinistreAuto = 0;
    int sansSinistreHabitation = 0;
    double versementAnnuel = versementMensuel * 12;

    for (int annee = 1; annee <= duree; annee++) {

        // Un compteur par contrat : sinistre → 0, sinon +1
        if (auto.sinistres().contains(annee)) {
            sansSinistreAuto = 0;
        } else {
            sansSinistreAuto++;
        }
        if (habitation.sinistres().contains(annee)) {
            sansSinistreHabitation = 0;
        } else {
            sansSinistreHabitation++;
        }

        double tauxAuto = calculator.taux(mode, tauxFixe, tauxDepart,
                                          increment, plafond, sansSinistreAuto);
        double tauxHabitation = calculator.taux(mode, tauxFixe, tauxDepart,
                                                increment, plafond, sansSinistreHabitation);

        double bonusAuto = auto.cotisation() * tauxAuto;
        double bonusHabitation = habitation.cotisation() * tauxHabitation;
        double bonus = bonusAuto + bonusHabitation;

        capital = capital * (1 + rendement) + bonus + versementAnnuel;
        resultats.add(new YearResult(annee, capital, bonus, bonusAuto, bonusHabitation));
    }
    return resultats;
}

}

