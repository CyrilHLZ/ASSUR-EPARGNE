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
              "versementMensuel": 0, "hausseAnnuelle": 0,
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

    @Test
    void dureeZeroRenvoie400() throws Exception {
        String json = """
            {
            "cotisationHabitation": 400, "cotisationAuto": 661,
            "versementMensuel": 0, "hausseAnnuelle": 0,
            "sinistresAuto": [], "sinistresHabitation": [],
            "rendement": 0.02, "duree": 0,
            "tauxFixe": 0.10, "tauxDepart": 0, "increment": 0, "plafond": 0,
            "mode": "FIXE"
            }
            """;

        mockMvc.perform(post("/api/simulations/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.duree").exists());
    }

        @Test
    void sansModeNiSinistresRenvoie400() throws Exception {
        
        String json = """
            {
            "cotisationHabitation": 400, "cotisationAuto": 661,
            "versementMensuel": 0, "hausseAnnuelle": 0,
            "rendement": 0.02, "duree": 20,
            "tauxFixe": 0.10, "tauxDepart": 0, "increment": 0, "plafond": 0
            }
            """;

        mockMvc.perform(post("/api/simulations/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mode").exists())
                .andExpect(jsonPath("$.sinistresAuto").exists())
                .andExpect(jsonPath("$.sinistresHabitation").exists());
    }

    @Test
    void plafondSousTauxDepartRenvoie400() throws Exception {
        // Progressif : départ à 5 %, plafond à 3 % → incohérent
        String json = """
            {
            "cotisationHabitation": 400, "cotisationAuto": 661,
            "versementMensuel": 0, "hausseAnnuelle": 0,
            "sinistresAuto": [], "sinistresHabitation": [],
            "rendement": 0.02, "duree": 20,
            "tauxFixe": 0, "tauxDepart": 0.05, "increment": 0.005, "plafond": 0.03,
            "mode": "VARIABLE"
            }
            """;

        mockMvc.perform(post("/api/simulations/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.plafondCoherent").exists());
    }

    @Test
    void incrementNegatifRenvoie400() throws Exception {
        String json = """
            {
            "cotisationHabitation": 400, "cotisationAuto": 661,
            "versementMensuel": 0, "hausseAnnuelle": 0,
            "sinistresAuto": [], "sinistresHabitation": [],
            "rendement": 0.02, "duree": 20,
            "tauxFixe": 0, "tauxDepart": 0.05, "increment": -0.005, "plafond": 0.10,
            "mode": "VARIABLE"
            }
            """;

        mockMvc.perform(post("/api/simulations/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.increment").exists());
    }

    @Test
    void reponseContientLesTotaux() throws Exception {
        String json = """
            {
            "cotisationHabitation": 0, "cotisationAuto": 1000,
            "versementMensuel": 100, "hausseAnnuelle": 0,
            "sinistresAuto": [], "sinistresHabitation": [],
            "rendement": 0.02, "duree": 2,
            "tauxFixe": 0.10, "tauxDepart": 0, "increment": 0, "plafond": 0,
            "mode": "FIXE"
            }
            """;

        mockMvc.perform(post("/api/simulations/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capitalFinal").value(closeTo(2626.0, 0.01)))
                .andExpect(jsonPath("$.versementsTotal").value(closeTo(2400.0, 0.01)))
                .andExpect(jsonPath("$.interetsTotal").value(closeTo(26.0, 0.01)))
                .andExpect(jsonPath("$.detailParAnnee[1].interets").value(closeTo(26.0, 0.01)));
    }

    @Test
    void hausseTropForteRenvoie400() throws Exception {
        String json = """
            {
            "cotisationHabitation": 400, "cotisationAuto": 661,
            "versementMensuel": 0,
            "sinistresAuto": [], "sinistresHabitation": [],
            "rendement": 0.02, "duree": 20,
            "tauxFixe": 0, "tauxDepart": 0.05, "increment": 0.005, "plafond": 0.10,
            "hausseAnnuelle": 0.20,
            "mode": "VARIABLE"
            }
            """;

        mockMvc.perform(post("/api/simulations/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.hausseAnnuelle").exists());
    }
}
