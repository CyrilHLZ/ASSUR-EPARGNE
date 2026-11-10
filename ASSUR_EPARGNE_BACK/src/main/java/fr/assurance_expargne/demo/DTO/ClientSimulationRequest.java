package fr.assurance_expargne.demo.DTO;

import java.util.Set;

import fr.assurance_expargne.demo.Model.BonusMode;
import fr.assurance_expargne.demo.Model.Support;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public record ClientSimulationRequest(

    @DecimalMin(value = "0.0", message = "La cotisation habitation ne peut pas être négative")
    double cotisationHabitation,

    @DecimalMin(value = "0.0", message = "La cotisation auto ne peut pas être négative")
    double cotisationAuto,

    @DecimalMin (value = "0.0", message = "Le versement mensuel ne peut pas être négatif")
    double versementMensuel,

    @NotNull(message = "La liste des sinistres auto est obligatoire (mettre [] si aucun)")
    Set<Integer> sinistresAuto,

    @NotNull(message = "La liste des sinistres habitation est obligatoire (mettre [] si aucun)")
    Set<Integer> sinistresHabitation, 

    @NotNull(message = "Le mode de bonus est obligatoire : AUCUN, FIXE ou VARIABLE")
    BonusMode mode,

    @NotNull(message = "Le support d'épargne est obligatoire : ASSURANCE_VIE ou PER")
    Support support,

    @DecimalMin(value = "0.0", message = "Le rendement ne peut pas être négatif")
    @DecimalMax(value = "0.2", message = "Le rendement ne peut pas dépasser 20 %")
    double rendement,

    @Min(value = 1, message = "La durée doit être d'au moins 1 an")
    @Max(value = 40, message = "La durée ne peut pas dépasser 40 ans")
    int duree,

    double tauxFixe,
    double tauxDepart,
    @DecimalMin(value = "0.0", message = "La hausse par année sans sinistre ne peut pas être négative")
    double increment,

    @DecimalMin(value = "0.0", message = "Le plafond ne peut pas être négatif")
    double plafond,

    @DecimalMin(value = "0.0", message = "La hausse annuelle ne peut pas être négative")
    @DecimalMax(value = "0.15", message = "La hausse annuelle ne peut pas dépasser 15 %")
    double hausseAnnuelle

) {

    // Règle sur deux champs : en mode progressif, le plafond ne peut pas être sous le taux de départ
    @AssertTrue(message = "Le plafond doit être supérieur ou égal au taux de départ")
    public boolean isPlafondCoherent() {
        return mode != BonusMode.VARIABLE || plafond >= tauxDepart;
    }
}
