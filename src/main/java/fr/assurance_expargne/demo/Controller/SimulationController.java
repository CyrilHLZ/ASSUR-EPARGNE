package fr.assurance_expargne.demo.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.assurance_expargne.demo.DTO.ClientSimulationRequest;
import fr.assurance_expargne.demo.DTO.ClientSimulationResponse;
import fr.assurance_expargne.demo.Service.ClientSimulationService;

@RestController
@RequestMapping("/api/simulations")
public class SimulationController {

    private final ClientSimulationService simulationService;

    public SimulationController(ClientSimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/client")
    public ClientSimulationResponse simulerClient(@RequestBody ClientSimulationRequest req) {
        return simulationService.simuler(req);
    }
}
