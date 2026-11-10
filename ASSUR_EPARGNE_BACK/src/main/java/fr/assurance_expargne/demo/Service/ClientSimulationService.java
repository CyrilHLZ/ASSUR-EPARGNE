package fr.assurance_expargne.demo.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import fr.assurance_expargne.demo.DTO.ClientSimulationRequest;
import fr.assurance_expargne.demo.DTO.ClientSimulationResponse;
import fr.assurance_expargne.demo.DTO.YearResult;

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
        req.versementMensuel(), req.rendement(), req.duree(),
        req.mode(), req.tauxFixe(), req.tauxDepart(),
        req.increment(), req.plafond());


        // 4. Capital final = celui de la dernière année
        double capitalFinal = detail.get(detail.size() - 1).capital();

        // 5. Total des bonus = somme des bonus de chaque année
        double bonusTotal = 0;
        for (YearResult an : detail) {
            bonusTotal += an.bonus();
        }

        return new ClientSimulationResponse(capitalFinal, bonusTotal, detail);
    }
}
