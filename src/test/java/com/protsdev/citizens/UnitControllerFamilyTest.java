package com.protsdev.citizens;

// import static org.assertj.core.api.Assertions.assertThat;cc
import static org.mockito.ArgumentMatchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.protsdev.citizens.domain.FamilyService;

@SpringBootTest
@AutoConfigureMockMvc
public class UnitControllerFamilyTest {

  private final String URI_API_FAMILY = "/api/family";
  private final String URI_API_FAMILY_NUCLEAR = "/api/family/nuclear";
  private final String URI_API_FAMILY_EXTENDED = "/api/family/extended";

  @Autowired
  FamilyService citizenRelations;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void test_index() throws Exception {
    mockMvc.perform(get(URI_API_FAMILY))
        // .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith(URI_API_FAMILY)).exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.nuclear", endsWith(URI_API_FAMILY_NUCLEAR)).exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.extended", endsWith(URI_API_FAMILY_EXTENDED)).exists());
  }

  @Test
  void test_where_nulable_inputs() throws Exception {

    mockMvc.perform(get(URI_API_FAMILY_NUCLEAR))
        // .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith(URI_API_FAMILY_NUCLEAR)).exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate").exists());

    mockMvc.perform(get(URI_API_FAMILY_EXTENDED))
        // .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith(URI_API_FAMILY_EXTENDED)).exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate").exists());

  }

  @Test
  void test_create_where_wrong_inputs() throws Exception {
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
  void test_nuclear_adult_family_full() throws Exception {

    mockMvc.perform(get(URI_API_FAMILY_NUCLEAR).params(getRequestForm("Gomez")))
        // .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith(URI_API_FAMILY_NUCLEAR)).exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.index", endsWith(URI_API_FAMILY)).exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.family").exists());
  }

  @Test
  void test_nuclear_adult_family_for_lonely() throws Exception {
    mockMvc.perform(get(URI_API_FAMILY_NUCLEAR).params(getRequestForm("Wednesday")))
        // .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith(URI_API_FAMILY_NUCLEAR)).exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.index", endsWith(URI_API_FAMILY)).exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.family").exists());
  }

  @Test
  void test_nuclear_child_family() throws Exception {
    mockMvc.perform(get(URI_API_FAMILY_NUCLEAR).params(getRequestForm("Oprah")))
        // .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith(URI_API_FAMILY_NUCLEAR)).exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.index", endsWith(URI_API_FAMILY)).exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.family.citizen").exists());
  }

  @Test
  void test_extended_family() throws Exception {
    mockMvc.perform(get(URI_API_FAMILY_EXTENDED).params(getRequestForm("Oprah")))
        // .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith(URI_API_FAMILY_EXTENDED)).exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.index", endsWith(URI_API_FAMILY)).exists())
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
        citizenForm.add("birthDay", "1970-03-300");
        citizenForm.add("gender", "MALEg");
        citizenForm.add("citizenship", "USAv");
        break;

      case "Gomez":
        citizenForm.add("firstName", "Gomez");
        citizenForm.add("familyName", "Addams");
        citizenForm.add("birthDay", "1970-03-30");
        citizenForm.add("gender", "MALE");
        citizenForm.add("citizenship", "USA");
        break;

      case "Oprah":
        citizenForm.add("firstName", "Oprah");
        citizenForm.add("familyName", "Addams");
        citizenForm.add("birthDay", "2009-05-14");
        citizenForm.add("gender", "FEMALE");
        citizenForm.add("citizenship", "USA");
        break;

      case "Wednesday":
        citizenForm.add("firstName", "Wednesday");
        citizenForm.add("familyName", "Addams");
        citizenForm.add("birthDay", "1995-02-16");
        citizenForm.add("gender", "FEMALE");
        citizenForm.add("citizenship", "USA");
        break;

      default:
        break;
    }

    return citizenForm;
  }
}