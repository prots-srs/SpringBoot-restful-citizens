package com.protsdev.citizens;

import static org.mockito.ArgumentMatchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerRootTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void root_load() throws Exception {
        mockMvc.perform(get("/api"))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith("/api")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.citizen", endsWith("/api/citizen")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.marriage", endsWith("/api/marriage")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.family", endsWith("/api/family")).exists());
    }
}
