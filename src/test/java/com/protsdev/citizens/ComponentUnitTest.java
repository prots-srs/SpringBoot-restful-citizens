package com.protsdev.citizens;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import com.protsdev.citizens.repositories.CitizenRepository;
import com.protsdev.citizens.services.ImportJsonToDb;
import com.protsdev.citizens.services.JsonLoaderService;
import com.protsdev.citizens.services.VerifyFields;

@SpringBootTest
public class ComponentUnitTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void check_IoC_load_beans() {
        assertNotNull(applicationContext.getBean(JsonLoaderService.class));
        assertNotNull(applicationContext.getBean(ImportJsonToDb.class));
        assertNotNull(applicationContext.getBean(CitizenRepository.class));
        assertNotNull(applicationContext.getBean(LoadDatabase.class));
        assertNotNull(applicationContext.getBean(VerifyFields.class));
    }

}
