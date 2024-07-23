package com.protsdev.citizens;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import java.util.Map;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "management.port=0" })
// @AutoConfigureMockMvc
public class ActuatorApplicationTest {

    @LocalServerPort
    private int port;

    @Value("${local.management.port}")
    private int mgt;

    @Autowired
    private TestRestTemplate testRestTemplate;

    // @Autowired
    // private MockMvc mockMvc;

    /*
     * root
     */
    @Test
    public void should_return_401_when_sending_request_to_management_endpoint() throws Exception {
        @SuppressWarnings("rawtypes")
        ResponseEntity<Map> entity = testRestTemplate.getForEntity(
                "http://localhost:" + mgt + "/actuator", Map.class);

        then(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    /*
     * Health
     */
    @Test
    public void should_return_200_health_status_up() throws Exception {
        var entity = testRestTemplate.getForEntity("http://localhost:" + mgt + "/actuator/health", String.class);
        then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(entity.getBody());
        then(rootNode.get("status").textValue()).isEqualTo("UP");
    }

    /*
     * Beans
     */
    @Test
    public void should_return_401_when_anonimous_access_beans() throws Exception {
        var entity = testRestTemplate.getForEntity("http://localhost:" + mgt + "/actuator/beans", String.class);

        then(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void should_return_200_when_anonimous_access_beans() throws Exception {
        var entity = testRestTemplate
                .withBasicAuth("admin", "_admin_")
                .getForEntity("http://localhost:" + mgt + "/actuator/beans", String.class);

        then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void should_return_403_when_user_access_beans() throws Exception {
        var entity = testRestTemplate
                .withBasicAuth("user", "_user_")
                .getForEntity("http://localhost:" + mgt + "/actuator/beans", String.class);

        then(entity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
