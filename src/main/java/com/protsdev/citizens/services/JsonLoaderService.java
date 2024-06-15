package com.protsdev.citizens.services;

import java.util.Optional;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.protsdev.citizens.jsons.FullJson;

/*
 TO READ
 https://www.baeldung.com/spring-boot-clean-architecture
 https://www.baeldung.com/java-jackson-jsonnode-collection
 https://www.baeldung.com/jackson-object-mapper-tutorial
 https://www.baeldung.com/java-optional
 https://www.oracle.com/technical-resources/articles/java/java8-optional.html
 * read demo data from demo file
 */

@Service
public class JsonLoaderService {

    private static final Logger log = LoggerFactory.getLogger(JsonLoaderService.class);
    private static final String FILE_PATH = "src\\main\\resources\\";
    private ObjectMapper mapper = new ObjectMapper();

    public Optional<FullJson> readJsonFromFile(String fileName) {
        String jsonString = readDataFromFile(fileName);
        Optional<FullJson> json = Optional.empty();

        if (jsonString.length() > 0) {
            try {
                JsonNode rootNode = mapper.readTree(jsonString);
                json = Optional.of(mapper.readValue(rootNode.traverse(), FullJson.class));
            } catch (Exception e) {
                log.error("Json read value: " + e.getMessage());
            }
        }

        return json;

    }

    private String readDataFromFile(String fileName) {
        String fileBody = "";
        /* Java 11 */
        try {
            fileBody = Files.readString(Paths.get(FILE_PATH + fileName));

        } catch (IOException e) {
            log.error("Read file: " + e.getMessage());
        }

        /* Java 8 */
        // try (InputStream demoData = Files.newInputStream(Paths.get(DEMO_FILE))) {
        // int i;

        // do {
        // i = demoData.read();
        // if (i != -1) {
        // fileBody = fileBody + (char) i;
        // }

        // } while (i != -1);
        // } catch (Exception e) {
        // log.error("-->> Read file: " + e.getMessage());
        // }

        return fileBody;
    }
}
