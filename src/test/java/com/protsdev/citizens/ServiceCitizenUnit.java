package com.protsdev.citizens;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.protsdev.citizens.domain.CitizenService;
import com.protsdev.citizens.dto.CitizenRequest;
import com.protsdev.citizens.enums.Citizenship;
import com.protsdev.citizens.enums.Gender;
import com.protsdev.citizens.models.Citizen;
import com.protsdev.citizens.repositories.CitizenRepository;

@SpringBootTest
public class ServiceCitizenUnit {

    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired
    private CitizenService citizenService;

    @Test
    void view_all_citizens() {
        List<Citizen> ci = citizenRepository.findAll();
        ci.forEach(c -> System.out.println("-->> ci: " + c.getNames()));
    }

    @Test
    void find_user_ok() {

        var citizen = new CitizenRequest();
        citizen.setFirstName("Gomez");
        citizen.setFamilyName("Addams");
        citizen.setBirthDate(LocalDate.parse("1970-03-30"));
        citizen.setGender(Gender.MALE);
        citizen.setCitizenship(Citizenship.USA);

        Optional<Citizen> citizenPersisted = citizenService.fetch(citizen);
        if (citizenPersisted.isPresent()) {
            System.out.println("-->> citizen ok: " + citizenPersisted.get().getNames());
        }

        assertThat(citizenPersisted.isPresent()).isTrue();
        System.out.println("-->> error ok: " + citizenService.getLastError());
    }

    @Test
    void find_user_ok_2() {

        var citizen = new CitizenRequest();
        citizen.setFirstName("Morticia");
        citizen.setFamilyName("Addams");
        citizen.setBirthDate(LocalDate.parse("1973-04-28"));
        citizen.setGender(Gender.FEMALE);
        citizen.setCitizenship(Citizenship.USA);

        Optional<Citizen> citizenPersisted = citizenService.fetch(citizen);
        if (citizenPersisted.isPresent()) {
            System.out.println("-->> citizen ok2: " + citizenPersisted.get().getNames());
        }

        assertThat(citizenPersisted.isPresent()).isTrue();
        System.out.println("-->> error ok2: " + citizenService.getLastError());
    }

    @Test
    void find_user_example_wrong() {

        var citizen = new CitizenRequest();
        citizen.setFirstName("Morticia");
        citizen.setFamilyName("Addams");
        citizen.setBirthDate(LocalDate.parse("1973-04-28"));
        citizen.setGender(Gender.FEMALE);
        citizen.setCitizenship(Citizenship.USA);

        Optional<Citizen> citizenPersisted = citizenService.fetchByExample(citizen);
        if (citizenPersisted.isPresent()) {
            System.out.println("-->> citizen example ok: " + citizenPersisted.get().getNames());
        }

        assertThat(citizenPersisted.isPresent()).isFalse();
        System.out.println("-->> error example: " + citizenService.getLastError());
    }
}
