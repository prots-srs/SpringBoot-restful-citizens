package com.protsdev.citizens;

import static org.mockito.ArgumentMatchers.endsWith;
// import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

// import com.protsdev.citizens.storage.StorageService;

/**
 * Testing controller for citizens.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerCitizenTest {
    private final String API_URI = "/api/citizen";

    @Autowired
    private MockMvc mockMvc;

    // @Autowired
    // @MockBean
    // private StorageService storageService;

    @Test
    void get_missing_user() throws Exception {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("firstName", "Abc");
        form.add("familyName", "Zml");
        form.add("birthDate", "1955-01-01");
        form.add("gender", "MALE");
        form.add("citizenship", "CANADA");

        mockMvc.perform(get(API_URI).params(form))
                // .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith("/api/citizen")).exists());
    }

    @Test
    void get_nullable_wrong_fields() throws Exception {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        mockMvc.perform(get(API_URI).params(form))
                // .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.formTemplate").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith("/api/citizen")).exists());

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
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith("/api/citizen")).exists());
    }

    @Test
    void create_read_update_read_user() throws Exception {
        // form data
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("firstName", "Garrold");
        form.add("familyName", "Simpson");
        form.add("birthDate", "1983-09-18");
        form.add("gender", "MALE");
        form.add("citizenship", "UK");

        // create user
        mockMvc.perform(multipart(API_URI).params(form))
                // .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith("/api/citizen")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.citizen").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.citizen.name").value("Garrold Simpson"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.citizen.lifeDays").value("1983-09-18"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.citizen.citizenship").value("UK"));

        // control read
        mockMvc.perform(get(API_URI).params(form))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.citizen").exists());

        // add update data
        form.add("deathDate", "2024-02-13");
        form.add("newFamilyName", "Smith");
        form.add("newCitizenship", "CANADA");

        MockMultipartFile multipartFile = new MockMultipartFile(
                "avatar",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes());

        // update user
        mockMvc.perform(multipart(API_URI).file(multipartFile).params(form))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith("/api/citizen")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.citizen").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.citizen.name").value("Garrold Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.citizen.lifeDays").value("1983-09-18 - 2024-02-13"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.citizen.citizenship").value("CANADA"));

        // then(storageService).should().store(multipartFile);

        form.set("familyName", "Smith");
        form.set("citizenship", "CANADA");

        // control read
        mockMvc.perform(get(API_URI).params(form))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/hal+json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$._links.self", endsWith("/api/citizen")).exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.citizen").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.citizen.name").value("Garrold Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.citizen.lifeDays").value("1983-09-18 - 2024-02-13"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.citizen.citizenship").value("CANADA"));
    }

}