package com.protsdev.citizens;

import static org.mockito.ArgumentMatchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * test marriage controller.
 * https://www.tabnine.com/code/java/methods/org.springframework.test.web.servlet.result.MockMvcResultMatchers/jsonPath
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UnitControllerMarriageTest {
  private final String URI_API_MARRIAGE = "/api/marriage";
  private final String URI_API_MARRIAGE_CREATE = "/api/marriage/create";
  private final String URI_API_MARRIAGE_DISSOLUTION = "/api/marriage/dissolution";
  private final String URI_API_CITIZEN = "/api/citizen";

  @Autowired
  private MockMvc mockMvc;

  @Test
  void test_index() throws Exception {
    mockMvc.perform(get(URI_API_MARRIAGE))
        // .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._links").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.create").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.dissolution").exists());
  }

  @Test
  void test_create_where_nulable_inputs() throws Exception {

    // var targetError = "[Fields
    // 'idCitizenA,hashCodeCitizenA,idCitizenB,hashCodeCitizenB,dateOfEvent' have
    // null value]";

    mockMvc.perform(post(URI_API_MARRIAGE_CREATE))
        // .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate").exists());
    // .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate.error")
    // .value(targetError));

  }

  @Test
  void test_create_where_wrong_inputs() throws Exception {
    // var targetError = "[Fields
    // 'idCitizenA,hashCodeCitizenA,idCitizenB,hashCodeCitizenB' have null value]";

    mockMvc.perform(post(URI_API_MARRIAGE_CREATE).params(getRequestForm("WrongFields")))
        // .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate").exists());
    // .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate.error")
    // .value(targetError));

  }

  @Test
  void test_create_where_partner_is_child_another_not_born() throws Exception {

    // var targetError = "[Wrong date of event of citizen Oprah Addams, Citizen
    // Justin DiCaprio is child]";

    mockMvc.perform(post(URI_API_MARRIAGE_CREATE).params(partnersDataForm("Oprah", "Justin", "2000-06-24")))
        // .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate").exists());
    // .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate.error")
    // .value(targetError));
  }

  @Test
  void test_create_where_partner_has_active_marriage() throws Exception {

    // var targetError = "[Citizen Gomez Addams has current active marriage]";

    mockMvc.perform(post(URI_API_MARRIAGE_CREATE).params(partnersDataForm("Gomez", "Emma", "2024-06-24")))
        // .andDo(print())
        .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate").exists());
    // .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate.error")
    // .value(targetError));

  }

  @Test
  void test_create_where_partners_have_nuclearfamily_relations() throws Exception {

    // var targetError = "[Citizen Chris Watson has parenthood relations with Emma
    // Watson]";

    mockMvc.perform(post(URI_API_MARRIAGE_CREATE).params(partnersDataForm("Chris", "Emma", null)))
        // .andDo(print())
        .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate").exists());
    // .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate.error")
    // .value(targetError));
  }

  @Test
  void test_create_where_partners_have_nuclearfamily_relations_sibling() throws Exception {

    // var targetError = "[Citizen Alex Watson has sibling relations with Emma
    // Watson, Citizen Alex Watson has sibling relations with Emma Watson]";

    mockMvc.perform(post(URI_API_MARRIAGE_CREATE).params(partnersDataForm("Alex", "Emma", null)))
        // .andDo(print())
        .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith("/api/marriage/create")).exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.index", endsWith("/api/marriage")).exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate").exists());
    // .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate.error")
    // .value(targetError));
  }

  @Test
  void test_create_success_marriage() throws Exception {
    mockMvc.perform(post(URI_API_MARRIAGE_CREATE).params(partnersDataForm("Justin", "Wednesday", null)))
        // .andDo(print())
        .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith("/api/marriage/create")).exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.index", endsWith("/api/marriage")).exists());
  }

  @Test
  void test_dissolution_active_marriage() throws Exception {
    mockMvc.perform(post(URI_API_MARRIAGE_DISSOLUTION).params(partnersDataForm("Gomez", "Morticia", null)))
        // .andDo(print())
        .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith("/api/marriage/dissolution")).exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.index", endsWith("/api/marriage")).exists());
  }

  /*
   * helper
   * test citizendata
   */
  private MultiValueMap<String, String> getRequestForm(String citizen) {
    MultiValueMap<String, String> citizenForm = new LinkedMultiValueMap<>();
    switch (citizen) {
      case "WrongFields":
        citizenForm.add("idCitizenA", "0000");
        citizenForm.add("hashCodeCitizenA", "");
        citizenForm.add("idCitizenB", " ");
        citizenForm.add("hashCodeCitizenB", "00");
        // citizenForm.add("dateOfEvent", "2024-06-24");
        break;

      case "Gomez":
        citizenForm.add("firstName", "Gomez");
        citizenForm.add("familyName", "Addams");
        citizenForm.add("birthDay", "1970-03-30");
        citizenForm.add("gender", "MALE");
        citizenForm.add("citizenship", "USA");
        break;

      case "Morticia":
        citizenForm.add("firstName", "Morticia");
        citizenForm.add("familyName", "Addams");
        citizenForm.add("birthDay", "1973-04-28");
        citizenForm.add("gender", "FEMALE");
        citizenForm.add("citizenship", "USA");
        break;

      case "Oprah":
        citizenForm.add("firstName", "Oprah");
        citizenForm.add("familyName", "Addams");
        citizenForm.add("birthDay", "2009-05-14");
        citizenForm.add("gender", "FEMALE");
        citizenForm.add("citizenship", "USA");
        break;

      case "Justin":
        citizenForm.add("firstName", "Justin");
        citizenForm.add("familyName", "DiCaprio");
        citizenForm.add("birthDay", "1992-05-02");
        citizenForm.add("gender", "MALE");
        citizenForm.add("citizenship", "CANADA");
        break;

      case "Wednesday":
        citizenForm.add("firstName", "Wednesday");
        citizenForm.add("familyName", "Addams");
        citizenForm.add("birthDay", "1995-02-16");
        citizenForm.add("gender", "FEMALE");
        citizenForm.add("citizenship", "USA");
        break;

      case "WrongEventDate":
        break;

      case "Emma":
        citizenForm.add("firstName", "Emma");
        citizenForm.add("familyName", "Watson");
        citizenForm.add("birthDay", "1990-04-15");
        citizenForm.add("gender", "FEMALE");
        citizenForm.add("citizenship", "UK");
        break;

      case "Chris":
        citizenForm.add("firstName", "Chris");
        citizenForm.add("familyName", "Watson");
        citizenForm.add("birthDay", "1960-10-03");
        citizenForm.add("gender", "MALE");
        citizenForm.add("citizenship", "UK");
        break;

      case "Jacqueline":
        citizenForm.add("firstName", "Jacqueline");
        citizenForm.add("familyName", "Luesby");
        citizenForm.add("birthDay", "1961-07-27");
        citizenForm.add("gender", "FEMALE");
        citizenForm.add("citizenship", "UK");
        break;

      case "Alex":
        citizenForm.add("firstName", "Alex");
        citizenForm.add("familyName", "Watson");
        citizenForm.add("birthDay", "1995-11-04");
        citizenForm.add("gender", "MALE");
        citizenForm.add("citizenship", "UK");
        break;

      default:
        break;
    }

    return citizenForm;
  }

  /*
   * helper
   * prepare to marriage request
   * two requests for defining citizens
   */
  private MultiValueMap<String, String> partnersDataForm(String partnerA, String partnerB, String dateOfEvent)
      throws Exception {

    var citizenA = mockMvc.perform(get(URI_API_CITIZEN).params(getRequestForm(partnerA)));// .andDo(print());
    var citizenB = mockMvc.perform(get(URI_API_CITIZEN).params(getRequestForm(partnerB)));// .andDo(print());

    ObjectMapper mapper = new ObjectMapper();
    JsonNode rootNodeA = mapper.readTree(citizenA.andReturn().getResponse().getContentAsString());
    JsonNode rootNodeB = mapper.readTree(citizenB.andReturn().getResponse().getContentAsString());

    MultiValueMap<String, String> partnersData = new LinkedMultiValueMap<>();
    partnersData.add("idCitizenA", rootNodeA.get("id").toString());
    partnersData.add("hashCodeCitizenA", rootNodeA.get("hashCode").toString());
    partnersData.add("idCitizenB", rootNodeB.get("id").toString());
    partnersData.add("hashCodeCitizenB", rootNodeB.get("hashCode").toString());

    if (dateOfEvent != null) {
      partnersData.add("dateOfEvent", dateOfEvent);
    }

    return partnersData;
  }

}
