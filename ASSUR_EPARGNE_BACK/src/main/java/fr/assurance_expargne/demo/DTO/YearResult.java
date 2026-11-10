package fr.assurance_expargne.demo.DTO;

public record YearResult(
        int annee,
        double capital,

        // Bonus de l'année, au total et par contrat
        double bonus,
        double bonusAuto,
        double bonusHabitation,

        // Taux appliqué à chaque contrat cette année-là (0.05 = 5 %)
        double tauxAuto,
        double tauxHabitation,

        // Cumuls depuis l'année 1 : capital = bonusCumul + versementsCumul + interets
        double bonusCumul,
        double versementsCumul,
        double interets
) {}
