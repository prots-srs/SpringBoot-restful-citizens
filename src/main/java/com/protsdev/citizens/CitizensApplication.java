package com.protsdev.citizens;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.protsdev.citizens.storage.StorageProperties;
import com.protsdev.citizens.storage.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class CitizensApplication {

    public static void main(String[] args) {
        // SpringApplication.run(CitizensApplication.class, args);
        // or
        SpringApplication application = new SpringApplication(CitizensApplication.class);

        boolean setProfile = false;
        for (int s = 0; s < args.length; s++) {
            if (args[s].equals("dev") && !setProfile) {
                application.setAdditionalProfiles("h2");
                setProfile = true;
            }
        }

        if (!setProfile) {
            application.setAdditionalProfiles("postgres");
        }
        // application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return args -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}
