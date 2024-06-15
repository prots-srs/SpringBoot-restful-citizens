package com.protsdev.citizens;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.protsdev.citizens.services.ImportJsonToDb;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(ImportJsonToDb importJsonToDb) {
        return args -> {

            importJsonToDb.run("demo.json");
        };
    }
}
