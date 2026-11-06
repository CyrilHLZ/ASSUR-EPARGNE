package fr.assurance_expargne.demo.DTO;

import java.util.Set;

public record ClientSimulationRequest(

    double cotisationHabibation,
    double cotisationAuto,
    double cotisationSante,
    double versementMensuel,
    int dureeSimulation,
    Set<Integer> sinistresAuto,
    Set<Integer> sinistresSante,
    Set<Integer> sinistresHabitation

) {}
