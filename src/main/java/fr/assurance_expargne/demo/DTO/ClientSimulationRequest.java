package fr.assurance_expargne.demo.DTO;

import java.util.Set;

import fr.assurance_expargne.demo.Model.BonusMode;

public record ClientSimulationRequest(

    double cotisationHabitation,
    double cotisationAuto,
    double cotisationSante,
    double versementMensuel,
    int dureeSimulation,
    Set<Integer> sinistresAuto,
    Set<Integer> sinistresSante,
    Set<Integer> sinistresHabitation,
    double cotisation, 
    double rendement, 
    int duree,
    double tauxFixe, 
    double tauxDepart,                              
    double increment, 
    double plafond,                  
    Set<Integer> sinistres,
    BonusMode mode
) {}
