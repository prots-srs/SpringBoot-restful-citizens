package com.protsdev.citizens;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.protsdev.citizens.enums.Citizenship;
import com.protsdev.citizens.enums.Gender;
import com.protsdev.citizens.models.Citizen;
import com.protsdev.citizens.repositories.CitizenRepository;

@SpringBootTest
public class ImportToDatabaseTest {

    private Integer COUNT_citizens = 8;

    @Autowired
    CitizenRepository citizenRepository;

    @Test
    void import_persist_test() {
        // citizens
        List<Citizen> citizens = citizenRepository.findAll();

        assertThat(citizens.size()).isEqualTo(COUNT_citizens);
        assertThat(citizens.get(0).getNames().getFirstName()).isEqualTo("Gomez");
        assertThat(citizens.get(0).getMarriages().size()).isEqualTo(1);
        assertThat(citizens.get(0).getParenthoods().size()).isEqualTo(3);

        citizens.forEach(ci -> {
            System.out.println("citizen id:" + ci.getId());

            ci.getMarriages().forEach(ma -> {
                System.out.println("marriage id:" + ma.getId());
                System.out.println("marriage date:" + ma.getDateRights().getStartDay());
            });

            ci.getParenthoods().forEach(pa -> {
                System.out.println("parenthood id:" + pa.getId());
                System.out.println("parenthood date:" + pa.getDateRights().getStartDay());
            });
        });
    }

    @Test
    void check_find_citizen() {
        List<Citizen> citizensPersisted = citizenRepository
                .findByNamesFamilyNameAndNamesFirstNameAndDays_BirthDayAndGenderAndCitizenship(
                        "Addams",
                        "Gomez",
                        // Date.valueOf("1970-03-30"),
                        LocalDate.parse("1970-03-30"),
                        Gender.MALE,
                        Citizenship.USA);

        citizensPersisted.forEach(ci -> System.out.println(ci.getId()));

        assertThat(citizensPersisted.size()).isEqualTo(1);

        Citizen citizenPersisted = citizensPersisted.get(0);

        assertThat(citizenPersisted.getNames().getFamilyName()).isEqualTo("Addams");
        assertThat(citizenPersisted.getNames().getFirstName()).isEqualTo("Gomez");
        assertThat(citizenPersisted.getDays().getBirthDay())
                .isEqualTo(Date.valueOf("1970-03-30"));
        assertThat(citizenPersisted.getGender()).isEqualTo(Gender.MALE);
        assertThat(citizenPersisted.getCitizenship()).isEqualTo(Citizenship.USA);

        assertThat(citizenPersisted.getMarriages().size()).isEqualTo(1);
    }

}
