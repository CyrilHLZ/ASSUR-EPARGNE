package fr.assurance_expargne.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.closeTo;
import org.springframework.http.MediaType;


@SpringBootTest          
@AutoConfigureMockMvc      
class SimulationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void simulationDossierRenvoie200() throws Exception {
        String json = """
            {
              "cotisationHabitation": 400, "cotisationAuto": 661,
              "versementMensuel": 0,
              "sinistresAuto": [], "sinistresHabitation": [],
              "rendement": 0.02, "duree": 20,
              "tauxFixe": 0.10, "tauxDepart": 0, "increment": 0, "plafond": 0,
              "mode": "FIXE"
            }
            """;

        mockMvc.perform(post("/api/simulations/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capitalFinal").value(closeTo(2578.0, 1.0)))
                .andExpect(jsonPath("$.detailParAnnee.length()").value(20));
    }
}
