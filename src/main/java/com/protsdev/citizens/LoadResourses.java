package com.protsdev.citizens;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.protsdev.citizens.services.ImportJsonToDb;

@Configuration
public class LoadResourses {

    private static final Logger log = LoggerFactory.getLogger(LoadResourses.class);

    @Bean
    @Profile("h2")
    CommandLineRunner initDatabase(ImportJsonToDb importJsonToDb) {
        return args -> {
            importJsonToDb.run("demo.json");
        };
    }

    @Bean
    CommandLineRunner showUserDir() {
        return args -> {
            log.info("-->> user.dir: " + System.getProperty("user.dir"));
            log.info("-->> user.home: " + System.getProperty("user.home"));
        };
    }
}
