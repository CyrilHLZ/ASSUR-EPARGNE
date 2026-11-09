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

    public List<YearResult> simuler(double cotisation, double versementMensuel, double rendement, int duree,
                                    BonusMode mode, double tauxFixe, double tauxDepart,
                                    double increment, double plafond,
                                    Set<Integer> sinistres) {
        List<YearResult> resultats = new ArrayList<>();
        double capital = 0;
        int anneesSansSinistre = 0;
        double versementAnnuel = versementMensuel * 12;

        for (int annee = 1; annee <= duree; annee++) {

            // Sinistre → compteur remis à zéro, sinon +1
            if (sinistres.contains(annee)) {
                anneesSansSinistre = 0;
            } else {
                anneesSansSinistre++;
            }

            double taux = calculator.taux(mode, tauxFixe, tauxDepart,
                                          increment, plafond, anneesSansSinistre);
            double bonus = cotisation * taux;
            capital = capital * (1 + rendement) + bonus + versementAnnuel;
            resultats.add(new YearResult(annee, capital, bonus));
        }
        return resultats;
    }
}

