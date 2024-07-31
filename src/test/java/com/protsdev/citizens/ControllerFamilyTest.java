package com.protsdev.citizens;

// import static org.assertj.core.api.Assertions.assertThat;cc
import static org.mockito.ArgumentMatchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.protsdev.citizens.domain.FamilyService;

/*
 * TODO avoid demo data check
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
public class ControllerFamilyTest {

    private final String URI_API_FAMILY = "/api/family";
    private final String URI_API_FAMILY_NUCLEAR = "/api/family/nuclear";
    private final String URI_API_FAMILY_EXTENDED = "/api/family/extended";

    @Autowired
    FamilyService citizenRelations;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void index() throws Exception {
        mockMvc.perform(get(URI_API_FAMILY))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith(URI_API_FAMILY)).exists())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$._links.nuclear", endsWith("/api/family/nuclear")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.extended", endsWith("/api/family/extended"))
                        .exists());
    }

    @Test
    void where_nullable_inputs_to_nuclear() throws Exception {

        mockMvc.perform(get(URI_API_FAMILY_NUCLEAR))
                // .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith("/api/family/nuclear")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate").exists());

    }

    @Test
    void where_nullable_inputs_to_extended() throws Exception {

        mockMvc.perform(get(URI_API_FAMILY_EXTENDED))
                // .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith("/api/family/extended")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate").exists());

    }

    @Test
    void create_where_wrong_inputs() throws Exception {
        mockMvc.perform(get(URI_API_FAMILY_NUCLEAR).params(getRequestForm("WrongFields")))
                // .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate").exists());

        mockMvc.perform(get(URI_API_FAMILY_EXTENDED).params(getRequestForm("WrongFields")))
                // .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate").exists());

    }

    @Test
    void nuclear_adult_family_full() throws Exception {

        mockMvc.perform(get(URI_API_FAMILY_NUCLEAR).params(getRequestForm("Gomez")))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith("/api/family/nuclear")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.index", endsWith("/api/family")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.family").exists());
    }

    @Test
    void nuclear_adult_family_for_lonely() throws Exception {
        mockMvc.perform(get(URI_API_FAMILY_NUCLEAR).params(getRequestForm("Wednesday")))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith("/api/family/nuclear")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.index", endsWith("/api/family")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.family").exists());
    }

    @Test
    void nuclear_child_family() throws Exception {
        mockMvc.perform(get(URI_API_FAMILY_NUCLEAR).params(getRequestForm("Oprah")))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith("/api/family/nuclear")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.index", endsWith("/api/family")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.family.citizen").exists());
    }

    @Test
    void extended_family() throws Exception {
        mockMvc.perform(get(URI_API_FAMILY_EXTENDED).params(getRequestForm("Oprah")))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith("/api/family/extended")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.index", endsWith("/api/family")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.family.citizen").exists());
    }

    /*
     * helper
     * test citizendata
     */
    private MultiValueMap<String, String> getRequestForm(String citizen) {
        MultiValueMap<String, String> citizenForm = new LinkedMultiValueMap<>();
        switch (citizen) {
            case "WrongFields":
                citizenForm.add("firstName", "Gomez");
                citizenForm.add("familyName", "Addams");
                citizenForm.add("birthDate", "1970-03-300");
                citizenForm.add("gender", "MALEg");
                citizenForm.add("citizenship", "USAv");
                break;

            case "Gomez":
                citizenForm.add("firstName", "Gomez");
                citizenForm.add("familyName", "Addams");
                citizenForm.add("birthDate", "1970-03-30");
                citizenForm.add("gender", "MALE");
                citizenForm.add("citizenship", "USA");
                break;

            case "Oprah":
                citizenForm.add("firstName", "Oprah");
                citizenForm.add("familyName", "Addams");
                citizenForm.add("birthDate", "2009-05-14");
                citizenForm.add("gender", "FEMALE");
                citizenForm.add("citizenship", "USA");
                break;

            case "Wednesday":
                citizenForm.add("firstName", "Wednesday");
                citizenForm.add("familyName", "Addams");
                citizenForm.add("birthDate", "1995-02-16");
                citizenForm.add("gender", "FEMALE");
                citizenForm.add("citizenship", "USA");
                break;

            default:
                break;
        }

        return citizenForm;
    }
}