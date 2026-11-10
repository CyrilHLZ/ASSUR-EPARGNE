package fr.assurance_expargne.demo.DTO;

import java.util.List;

import fr.assurance_expargne.demo.Model.Support;

public record ClientSimulationResponse(
        double capitalFinal,
        double bonusTotal,
        double versementsTotal,
        double interetsTotal,
        double cotisationsTotal,

        // Fiscalité du support choisi, estimée à la sortie, sur les intérêts seulement
        Support support,
        double tauxPrelevementsSociaux,
        double prelevementsSociaux,
        double interetsNets,
        double capitalNet,

        List<YearResult> detailParAnnee
) {}
