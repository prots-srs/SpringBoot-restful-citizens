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
public class ControllerMarriageTest {
    private final String URI_API_MARRIAGE = "/api/marriage";
    private final String URI_API_MARRIAGE_CREATE = "/api/marriage/create";
    private final String URI_API_MARRIAGE_DISSOLUTION = "/api/marriage/dissolution";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void get_index() throws Exception {
        mockMvc.perform(get(URI_API_MARRIAGE))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith("/api/marriage")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.create", endsWith("/api/marriage/create")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.dissolution", endsWith("/api/marriage/dissolution"))
                        .exists());
    }

    @Test
    void create_where_nullable_and_wrong_inputs() throws Exception {
        MultiValueMap<String, String> citizenForm = new LinkedMultiValueMap<>();
        // citizenForm.add("firstNameA", "Gomez");
        citizenForm.add("familyNameA", "");
        citizenForm.add("birthDateA", "1970-03-300");
        citizenForm.add("genderA", "MALEs");
        citizenForm.add("citizenshipA", "USAs");
        citizenForm.add("firstNameB", "Morticia");
        citizenForm.add("familyNameB", "Addams");
        citizenForm.add("birthDateB", "1970-03-30");
        citizenForm.add("genderB", "FEMALE");
        citizenForm.add("citizenshipB", "USA");
        citizenForm.add("dateOfEvent", "2024-06-240");
        citizenForm.add("giveBFamilyNameA", "fales");

        mockMvc.perform(post(URI_API_MARRIAGE_CREATE).params(citizenForm))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith("/api/marriage/create")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate.error").isNotEmpty());
    }

    @Test
    void dissolution_where_nullable_and_wrong_inputs() throws Exception {
        MultiValueMap<String, String> citizenForm = new LinkedMultiValueMap<>();
        // citizenForm.add("firstNameA", "Gomez");
        citizenForm.add("familyNameA", "");
        citizenForm.add("birthDateA", "1970-03-300");
        citizenForm.add("genderA", "MALEs");
        citizenForm.add("citizenshipA", "USAs");
        citizenForm.add("firstNameB", "Morticia");
        citizenForm.add("familyNameB", "Addams");
        citizenForm.add("birthDateB", "1970-03-30");
        citizenForm.add("genderB", "FEMALE");
        citizenForm.add("citizenshipB", "USA");
        citizenForm.add("dateOfEvent", "2024-06-240");
        citizenForm.add("giveBFamilyNameA", "fales");

        mockMvc.perform(post(URI_API_MARRIAGE_DISSOLUTION).params(citizenForm))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$._links.self", endsWith("/api/marriage/dissolution")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate.error").isNotEmpty());
    }
}
