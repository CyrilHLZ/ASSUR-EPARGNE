package fr.assurance_expargne.demo.DTO;

import java.util.Set;

import fr.assurance_expargne.demo.Model.BonusMode;
import jakarta.validation.constraints.DecimalMin;

public record ClientSimulationRequest(

    @DecimalMin (value = "0.0", message = "Le capital initial doit être supérieur à 0")
    double cotisationHabitation,

    @DecimalMin (value = "0.0", message = "Le capital initial doit être supérieur à 0")
    double cotisationAuto,

    @DecimalMin (value = "0.0", message = "Le versement mensuel ne peut pas être négatif")
    double versementMensuel,
    Set<Integer> sinistresAuto,
    Set<Integer> sinistresHabitation, 
    double rendement, 
    int duree,
    double tauxFixe, 
    double tauxDepart,                              
    double increment, 
    double plafond,                 
    BonusMode mode
) {}
