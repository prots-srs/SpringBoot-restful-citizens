package com.protsdev.citizens;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.protsdev.citizens.models.Citizen;
import com.protsdev.citizens.repositories.CitizenRepository;
import com.protsdev.citizens.services.ImportJsonToDb;
import com.protsdev.citizens.storage.StorageProperties;

@Configuration
public class LoadResourses {

    private static final Logger log = LoggerFactory.getLogger(LoadResourses.class);

    @Bean
    CommandLineRunner initDatabase(ImportJsonToDb importJsonToDb, CitizenRepository citizenRepository) {
        return args -> {

            // init DB
            List<Citizen> ci = citizenRepository.findAll();
            if (ci.size() == 0) {
                importJsonToDb.run("demo.json");
            }

            // check DB
            ci = citizenRepository.findAll();
            ci.forEach(c -> log.info(String.format("-->> citizen: id=%s, name=%s", c.getId(), c.getNames())));
        };
    }

    @Bean
    CommandLineRunner showUserDir(StorageProperties storageProperties) {
        return args -> {
            log.info("-->> user.dir: " + System.getProperty("user.dir"));
            log.info("-->> user.home: " + System.getProperty("user.home"));
            log.info("-->> storage: " + storageProperties.getLocation());
        };
    }
}
