package com.protsdev.citizens;

import static org.mockito.ArgumentMatchers.endsWith;
// import static org.mockito.ArgumentMatchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * test marriage controller
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerParenthoodTest {
    private final String URI_API_PARENTHOOD = "/api/parenthood";
    private final String URI_API_PARENTHOOD_GIVE = "/api/parenthood/give";
    private final String URI_API_PARENTHOOD_LOST = "/api/parenthood/lost";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void get_index() throws Exception {
        mockMvc.perform(get(URI_API_PARENTHOOD))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith("/api/parenthood")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.give", endsWith("/api/parenthood/give")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.lost", endsWith("/api/parenthood/lost"))
                        .exists());
    }

    @Test
    void give_where_nullable_and_wrong_inputs() throws Exception {
        MultiValueMap<String, String> parenthoodForm = new LinkedMultiValueMap<>();
        parenthoodForm.add("birthDate", "1970-03-300");

        mockMvc.perform(post(URI_API_PARENTHOOD_GIVE).params(parenthoodForm))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith("/api/parenthood/give")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate.error").isNotEmpty());
    }

    @Test
    void lost_where_nullable_and_wrong_inputs() throws Exception {
        MultiValueMap<String, String> parenthoodForm = new LinkedMultiValueMap<>();
        parenthoodForm.add("birthDate", "1970-03-300");

        mockMvc.perform(post(URI_API_PARENTHOOD_LOST).params(parenthoodForm))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$._links.self", endsWith("/api/parenthood/lost")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate.error").isNotEmpty());
    }
}
