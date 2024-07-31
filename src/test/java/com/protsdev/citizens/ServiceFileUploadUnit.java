package com.protsdev.citizens;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.protsdev.citizens.storage.StorageFileNotFoundException;
import com.protsdev.citizens.storage.StorageProperties;
import com.protsdev.citizens.storage.StorageService;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ServiceFileUploadUnit {

    @Autowired
    private StorageProperties properties;

    @Autowired
    private MockMvc mockMvc;

    // IllegalState Failed to load ApplicationContext in maven test
    @MockBean
    private StorageService storageService;

    @Test
    void test_property() {
        System.out.println("-->> property: " + properties.getLocation());
    }

    @Test
    void test_save_uploaded_file() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes());

        mockMvc.perform(multipart("/api/files").file(multipartFile))
                // .andDo(print())
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "/api/files"));

        then(storageService).should().store(multipartFile);
    }

    // @Test
    // public void shouldListAllFiles() throws Exception {
    // given(storageService.loadAll())
    // .willReturn(Stream.of(Paths.get("first.txt"), Paths.get("second.txt")));

    // mockMvc.perform(get("/api/files")).andExpect(status().isOk());
    // // .andExpect(model().attribute("files",
    // // Matchers.contains("http://localhost/files/first.txt",
    // // "http://localhost/files/second.txt")));
    // }

    @Test
    public void should404WhenMissingFile() throws Exception {
        given(storageService.loadAsResource("test.txt"))
                .willThrow(StorageFileNotFoundException.class);

        mockMvc.perform(get("/api/files/test.txt")).andExpect(status().isNotFound());
    }

}
