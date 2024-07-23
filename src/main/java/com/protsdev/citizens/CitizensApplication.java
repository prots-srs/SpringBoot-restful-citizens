package com.protsdev.citizens;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.protsdev.citizens.storage.StorageProperties;
import com.protsdev.citizens.storage.StorageService;

@SpringBootApplication
// (exclude = {
// SecurityAutoConfiguration.class,
// ManagementWebSecurityAutoConfiguration.class
// })
@EnableConfigurationProperties(StorageProperties.class)
public class CitizensApplication {

    public static void main(String[] args) {
        SpringApplication.run(CitizensApplication.class, args);

        // SpringApplication application = new
        // SpringApplication(CitizensApplication.class);
        // application.setBannerMode(Banner.Mode.OFF);
        // application.setAdditionalProfiles("myprofile");
        // application.run(args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return args -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}
