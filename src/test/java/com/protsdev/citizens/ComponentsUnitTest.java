package com.protsdev.citizens;

// import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import com.protsdev.citizens.controllers.CitizenController;
import com.protsdev.citizens.controllers.CitizenModelAssembler;
import com.protsdev.citizens.controllers.MarriageController;
import com.protsdev.citizens.controllers.RootController;
import com.protsdev.citizens.domain.FamilyService;
import com.protsdev.citizens.domain.CitizenService;
import com.protsdev.citizens.domain.MarriageService;
import com.protsdev.citizens.repositories.CitizenRepository;
import com.protsdev.citizens.repositories.MarriageRepository;
import com.protsdev.citizens.repositories.ParenthoodRepository;
// import com.protsdev.citizens.services.ImportJsonToDb;
// import com.protsdev.citizens.services.JsonLoaderService;

@SpringBootTest
public class ComponentsUnitTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void check_IoC_load_beans_h2() {
        // assertNotNull(applicationContext.getBean(JsonLoaderService.class));
        // assertNotNull(applicationContext.getBean(ImportJsonToDb.class));
        // assertNotNull(applicationContext.getBean(LoadDatabase.class));

        assertNotNull(applicationContext.getBean(CitizenRepository.class));
        assertNotNull(applicationContext.getBean(MarriageRepository.class));
        assertNotNull(applicationContext.getBean(ParenthoodRepository.class));

        assertNotNull(applicationContext.getBean(CitizenService.class));
        assertNotNull(applicationContext.getBean(FamilyService.class));
        assertNotNull(applicationContext.getBean(MarriageService.class));

        assertNotNull(applicationContext.getBean(CitizenModelAssembler.class));

        assertNotNull(applicationContext.getBean(RootController.class));
        assertNotNull(applicationContext.getBean(CitizenController.class));
        assertNotNull(applicationContext.getBean(MarriageController.class));
    }
}
