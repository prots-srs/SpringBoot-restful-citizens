package com.protsdev.citizens;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CitizenControllerTests {
    private final String API_URI = "/api/family";

    @Autowired
    private MockMvc mockMvc;

    MultiValueMap<String, String> getCitizenFieldsA() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("firstName", "Gomez");
        form.add("familyName", "Addams");
        form.add("birthDay", "1970-03-30");
        form.add("gender", "MALE");
        form.add("citizenship", "USA");

        return form;
    }

    @Test
    void get_nuclear_family_test() throws Exception {
        mockMvc.perform(get(API_URI + "/nuclear").params(getCitizenFieldsA()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
