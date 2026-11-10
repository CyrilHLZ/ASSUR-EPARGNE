package fr.assurance_expargne.demo.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.assurance_expargne.demo.DTO.ClientSimulationRequest;
import fr.assurance_expargne.demo.DTO.ClientSimulationResponse;
import fr.assurance_expargne.demo.DTO.YearResult;
import fr.assurance_expargne.demo.Model.Contrat;

@Service
public class ClientSimulationService {

    private final SimulationEngine engine;

    public ClientSimulationService(SimulationEngine engine) {
        this.engine = engine;
    }

    public ClientSimulationResponse simuler(ClientSimulationRequest req) {
        // 1. Un contrat par assurance, chacun avec ses propres sinistres
        Contrat auto = new Contrat(req.cotisationAuto(), req.sinistresAuto());
        Contrat habitation = new Contrat(req.cotisationHabitation(), req.sinistresHabitation());

        // 2. Appeler le moteur
        List<YearResult> detail = engine.simuler(auto, habitation,
                req.versementMensuel(), req.hausseAnnuelle(), req.rendement(), req.duree(),
                req.mode(), req.tauxFixe(), req.tauxDepart(),
                req.increment(), req.plafond());


        // 3. Les totaux = les cumuls de la dernière année, déjà calculés par le moteur
        YearResult derniere = detail.get(detail.size() - 1);

        // 4. Les prélèvements sociaux ne portent que sur les intérêts :
        //    les bonus et les versements ne sont pas des gains
        double taux = req.support().tauxPrelevementsSociaux();
        double prelevements = derniere.interets() * taux;

        return new ClientSimulationResponse(
                derniere.capital(),
                derniere.bonusCumul(),
                derniere.versementsCumul(),
                derniere.interets(),
                derniere.cotisationsCumul(),
                req.support(),
                taux,
                prelevements,
                derniere.interets() - prelevements,
                derniere.capital() - prelevements,
                detail);
    }
}
