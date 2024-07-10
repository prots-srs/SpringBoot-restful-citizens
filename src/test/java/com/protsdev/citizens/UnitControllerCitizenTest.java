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
 * Testing controller for citizens.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UnitControllerCitizenTest {
  private final String API_URI = "/api/citizen";

  @Autowired
  private MockMvc mockMvc;

  @Test
  void test_get_current_user() throws Exception {
    MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
    form.add("firstName", "Gomez");
    form.add("familyName", "Addams");
    form.add("birthDay", "1970-03-30");
    form.add("gender", "MALE");
    form.add("citizenship", "USA");

    mockMvc.perform(get(API_URI).params(form))
        // .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.citizen").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.hashCode").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._links").exists());
  }

  @Test
  void test_get_missing_user() throws Exception {

    MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
    form.add("firstName", "Garrold");
    form.add("familyName", "Simpson");
    form.add("birthDay", "1983-09-18");
    form.add("gender", "MALE");
    form.add("citizenship", "CANADA");

    mockMvc.perform(get(API_URI).params(form))
        // .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self").exists());
  }

  @Test
  void test_get_wrong_fields() throws Exception {
    MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
    form.add("firstName", "Gomez");
    form.add("familyName", "Addams");
    form.add("birthDay", "1970-03-300");
    form.add("gender", "MALEs");
    form.add("citizenship", "USAss");

    mockMvc.perform(get(API_URI).params(form))
        // .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self").exists());
  }

  @Test
  void test_get_nullable_fields() throws Exception {
    MultiValueMap<String, String> form = new LinkedMultiValueMap<>();

    mockMvc.perform(get(API_URI).params(form))
        // .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate").exists())
        // .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate.error")
        // .value("Fields 'firstName,familyName,birthDay,gender,citizenship' have null
        // value"))
        .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith("/api/citizen")).exists());
  }

  @Test
  void test_create_user() throws Exception {
    MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
    form.add("firstName", "Garrold");
    form.add("familyName", "Simpson");
    form.add("birthDay", "1983-09-18");
    form.add("gender", "MALE");
    form.add("citizenship", "UK");

    mockMvc.perform(post(API_URI).params(form))
        // .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.citizen").exists())
        // .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(9))
        .andExpect(MockMvcResultMatchers.jsonPath("$.hashCode").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._links").exists());
  }

  @Test
  void test_update_user() throws Exception {
    MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
    form.add("firstName", "Gomez");
    form.add("familyName", "Addams");
    form.add("birthDay", "1970-03-30");
    form.add("gender", "MALE");
    form.add("citizenship", "USA");

    var citizenMock = mockMvc.perform(get(API_URI).params(form));// .andDo(print());

    ObjectMapper mapper = new ObjectMapper();
    JsonNode rootNode = mapper.readTree(citizenMock.andReturn().getResponse().getContentAsString());

    MultiValueMap<String, String> formUpdate = new LinkedMultiValueMap<>();
    formUpdate.add("deathDay", "2024-02-13");
    formUpdate.add("hashCode", rootNode.get("hashCode").toString());

    mockMvc.perform(post(API_URI + "/" + rootNode.get("id").toString()).params(formUpdate))
        // .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.citizen").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.citizen.lifeDays").value("1970-03-30 - 2024-02-13"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(rootNode.get("id").toString()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.hashCode").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$._links").exists());
  }

}