package com.protsdev.citizens;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.protsdev.citizens.domain.CitizenFamilyService;
import com.protsdev.citizens.dto.FamilyNuclear;

@SpringBootTest
public class FamilyLoadTest {

    @Autowired
    CitizenFamilyService citizenRelations;

    @Test
    void fetch_nuclear_adult_family_full() {
        // FieldsDefiningCitizen inputFields = new FieldsDefiningCitizen(
        // "Gomez",
        // "Addams",
        // "1970-03-30",
        // "MALE",
        // "USA");

        // Optional<NuclearFamily> family =
        // citizenRelations.getNuclearFamily(inputFields);
        // assertThat(family.isPresent()).isTrue();

        // if (family.isPresent()) {
        // System.out.println("-->> family full: " + family.get().toString());
        // }
    }

    @Test
    void fetch_nuclear_adult_family_for_lonely() {
        // FieldsDefiningCitizen inputFields = new FieldsDefiningCitizen(
        // "Wednesday",
        // "Addams",
        // "1995-02-16",
        // "FEMALE",
        // "USA");

        // Optional<NuclearFamily> family =
        // citizenRelations.getNuclearFamily(inputFields);
        // assertThat(family.isPresent()).isTrue();

        // if (family.isPresent()) {
        // System.out.println("-->> family lonely: " + family.get().toString());
        // }
    }

    @Test
    void fetch_nuclear_child_family() {
        // FieldsDefiningCitizen inputFields = new FieldsDefiningCitizen(
        // "Oprah",
        // "Addams",
        // "2009-05-14",
        // "FEMALE",
        // "USA");

        // Optional<NuclearFamily> family =
        // citizenRelations.getNuclearFamily(inputFields);
        // assertThat(family.isPresent()).isTrue();

        // if (family.isPresent()) {
        // System.out.println("-->> family child: " + family.get().toString());
        // }
    }
}