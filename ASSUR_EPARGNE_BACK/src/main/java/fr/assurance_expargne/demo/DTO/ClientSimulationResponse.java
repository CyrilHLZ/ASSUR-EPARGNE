package fr.assurance_expargne.demo.DTO;

import java.util.List;

public record ClientSimulationResponse(
        double capitalFinal,
        double bonusTotal,            
        List<YearResult> detailParAnnee   
) {}


