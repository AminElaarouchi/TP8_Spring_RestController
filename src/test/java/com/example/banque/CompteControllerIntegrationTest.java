package com.example.banque;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CompteControllerIntegrationTest {
    @Autowired MockMvc mvc;

    @Test void servesJsonXmlAndOpenApi() throws Exception {
        mvc.perform(get("/banque/comptes").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].type").exists());
        mvc.perform(get("/banque/comptes").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_XML))
                .andExpect(content().string(containsString("EPARGNE")));
        mvc.perform(get("/v3/api-docs")).andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/banque/comptes']").exists());
    }

    @Test void createsAccountWithLocationAndReturns404WhenAbsent() throws Exception {
        mvc.perform(post("/banque/comptes").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"solde\":1500.0,\"dateCreation\":\"2024-11-01\",\"type\":\"COURANT\"}"))
                .andExpect(status().isCreated()).andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNumber());
        mvc.perform(get("/banque/comptes/99999")).andExpect(status().isNotFound());
    }
}
