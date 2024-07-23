package com.protsdev.citizens;

import static org.assertj.core.api.Assertions.assertThat;
// import static org.assertj.guava.api.Assertions.assertThat;
// import static org.assertj.guava.api.Assertions.entry;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.protsdev.citizens.domain.MarriageService;
import com.protsdev.citizens.dto.MarriageRequest;
import com.protsdev.citizens.enums.Citizenship;
import com.protsdev.citizens.enums.Gender;
import com.protsdev.citizens.models.Citizen;
import com.protsdev.citizens.models.Marriage;
import com.protsdev.citizens.repositories.CitizenRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/*
 * on demo data
 * https://assertj.github.io/doc/
 */
@SpringBootTest
public class ServiceMarriageUnit {

    @Autowired
    private MarriageService marriageService;

    @Test
    void wrong_create_where_partner_is_child_another_not_born() throws Exception {

        var marriageRequest = new MarriageRequest();
        marriageRequest.setFirstNameA("Oprah");
        marriageRequest.setFamilyNameA("Addams");
        marriageRequest.setBirthDateA(LocalDate.parse("2009-05-14"));
        marriageRequest.setGenderA(Gender.FEMALE);
        marriageRequest.setCitizenshipA(Citizenship.USA);

        marriageRequest.setFirstNameB("Justin");
        marriageRequest.setFamilyNameB("DiCaprio");
        marriageRequest.setBirthDateB(LocalDate.parse("1992-05-02"));
        marriageRequest.setGenderB(Gender.MALE);
        marriageRequest.setCitizenshipB(Citizenship.CANADA);

        marriageRequest.setDateOfEvent(LocalDate.parse("2015-06-24"));

        Optional<Marriage> marriageOp = marriageService.create(marriageRequest);
        System.out.println("-->> error: " + marriageService.getErrorMessages());

        assertThat(marriageOp.isPresent()).isFalse();
    }

    @Test
    void wrong_create_where_partner_has_active_marriage() throws Exception {

        var marriageRequest = new MarriageRequest();
        marriageRequest.setFirstNameA("Gomez");
        marriageRequest.setFamilyNameA("Addams");
        marriageRequest.setBirthDateA(LocalDate.parse("1970-03-30"));
        marriageRequest.setGenderA(Gender.MALE);
        marriageRequest.setCitizenshipA(Citizenship.USA);

        marriageRequest.setFirstNameB("Emma");
        marriageRequest.setFamilyNameB("Watson");
        marriageRequest.setBirthDateB(LocalDate.parse("1990-04-15"));
        marriageRequest.setGenderB(Gender.FEMALE);
        marriageRequest.setCitizenshipB(Citizenship.UK);
        marriageRequest.setDateOfEvent(LocalDate.parse("2024-06-24"));

        Optional<Marriage> marriageOp = marriageService.create(marriageRequest);
        System.out.println("-->> error: " + marriageService.getErrorMessages());

        assertThat(marriageOp.isPresent()).isFalse();
    }

    @Test
    void wrong_create_where_partners_have_nuclearfamily_relations() throws Exception {

        var marriageRequest = new MarriageRequest();
        marriageRequest.setFirstNameA("Chris");
        marriageRequest.setFamilyNameA("Watson");
        marriageRequest.setBirthDateA(LocalDate.parse("1960-10-03"));
        marriageRequest.setGenderA(Gender.MALE);
        marriageRequest.setCitizenshipA(Citizenship.UK);

        marriageRequest.setFirstNameB("Emma");
        marriageRequest.setFamilyNameB("Watson");
        marriageRequest.setBirthDateB(LocalDate.parse("1990-04-15"));
        marriageRequest.setGenderB(Gender.FEMALE);
        marriageRequest.setCitizenshipB(Citizenship.UK);

        Optional<Marriage> marriageOp = marriageService.create(marriageRequest);
        System.out.println("-->> error: " + marriageService.getErrorMessages());

        assertThat(marriageOp.isPresent()).isFalse();
    }

    @Test
    void wrong_create_where_partners_have_nuclearfamily_relations_sibling() throws Exception {

        var marriageRequest = new MarriageRequest();
        marriageRequest.setFirstNameA("Alex");
        marriageRequest.setFamilyNameA("Watson");
        marriageRequest.setBirthDateA(LocalDate.parse("1995-11-04"));
        marriageRequest.setGenderA(Gender.MALE);
        marriageRequest.setCitizenshipA(Citizenship.UK);

        marriageRequest.setFirstNameB("Emma");
        marriageRequest.setFamilyNameB("Watson");
        marriageRequest.setBirthDateB(LocalDate.parse("1990-04-15"));
        marriageRequest.setGenderB(Gender.FEMALE);
        marriageRequest.setCitizenshipB(Citizenship.UK);

        Optional<Marriage> marriageOp = marriageService.create(marriageRequest);
        System.out.println("-->> error: " + marriageService.getErrorMessages());

        assertThat(marriageOp.isPresent()).isFalse();
    }

    @Test
    void create_success_marriage() throws Exception {

        var marriageRequest = new MarriageRequest();
        marriageRequest.setFirstNameA("Wednesday");
        marriageRequest.setFamilyNameA("Addams");
        marriageRequest.setBirthDateA(LocalDate.parse("1995-02-16"));
        marriageRequest.setGenderA(Gender.FEMALE);
        marriageRequest.setCitizenshipA(Citizenship.USA);

        marriageRequest.setFirstNameB("Justin");
        marriageRequest.setFamilyNameB("DiCaprio");
        marriageRequest.setBirthDateB(LocalDate.parse("1992-05-02"));
        marriageRequest.setGenderB(Gender.MALE);
        marriageRequest.setCitizenshipB(Citizenship.CANADA);

        Optional<Marriage> marriageOp = marriageService.create(marriageRequest);

        assertThat(marriageOp.isPresent()).isTrue();
        assertThat(marriageService.getErrorMessages().replaceAll("[\\[\\]]", "")).isEmpty();
    }

    @Test
    void dissolution_active_marriage() throws Exception {

        var marriageRequest = new MarriageRequest();
        marriageRequest.setFirstNameA("Gomez");
        marriageRequest.setFamilyNameA("Addams");
        marriageRequest.setBirthDateA(LocalDate.parse("1970-03-30"));
        marriageRequest.setGenderA(Gender.MALE);
        marriageRequest.setCitizenshipA(Citizenship.USA);

        marriageRequest.setFirstNameB("Morticia");
        marriageRequest.setFamilyNameB("Addams");
        marriageRequest.setBirthDateB(LocalDate.parse("1973-04-28"));
        marriageRequest.setGenderB(Gender.FEMALE);
        marriageRequest.setCitizenshipB(Citizenship.USA);
        // marriageRequest.setDateOfEvent(LocalDate.parse("2024-06-24"));

        Optional<Marriage> marriageOp = marriageService.dissolution(marriageRequest);

        assertThat(marriageOp.isPresent()).isTrue();
        assertThat(marriageService.getErrorMessages().replaceAll("[\\[\\]]", "")).isEmpty();

        System.out.println("-->> marriage: " + marriageService.getErrorMessages());
        System.out.println("-->> marriage: " + marriageOp.get().toString());
    }

}
