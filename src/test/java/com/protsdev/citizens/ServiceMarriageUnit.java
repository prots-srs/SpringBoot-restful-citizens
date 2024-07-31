package com.protsdev.citizens;

import static org.assertj.core.api.Assertions.assertThat;
// import static org.assertj.guava.api.Assertions.assertThat;
// import static org.assertj.guava.api.Assertions.entry;

import java.time.LocalDate;
import java.util.Optional;

import com.protsdev.citizens.domain.MarriageService;
import com.protsdev.citizens.dto.MarriageRequest;
import com.protsdev.citizens.enums.Citizenship;
import com.protsdev.citizens.enums.Gender;
import com.protsdev.citizens.models.Marriage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        marriageRequest.setFirstName("Oprah");
        marriageRequest.setFamilyName("Addams");
        marriageRequest.setBirthDate(LocalDate.parse("2009-05-14"));
        marriageRequest.setGender(Gender.FEMALE);
        marriageRequest.setCitizenship(Citizenship.USA);

        marriageRequest.setFirstNamePartner("Justin");
        marriageRequest.setFamilyNamePartner("DiCaprio");
        marriageRequest.setBirthDatePartner(LocalDate.parse("1992-05-02"));
        marriageRequest.setGenderPartner(Gender.MALE);
        marriageRequest.setCitizenshipPartner(Citizenship.CANADA);

        marriageRequest.setDateOfEvent(LocalDate.parse("2015-06-24"));

        Optional<Marriage> marriageOp = marriageService.create(marriageRequest);
        System.out.println("-->> error wrong_create_where_partner_is_child_another_not_born: "
                + marriageService.getErrorMessages());

        assertThat(marriageOp.isPresent()).isFalse();
    }

    @Test
    void wrong_create_where_partner_has_active_marriage() throws Exception {

        var marriageRequest = new MarriageRequest();
        marriageRequest.setFirstName("Gomez");
        marriageRequest.setFamilyName("Addams");
        marriageRequest.setBirthDate(LocalDate.parse("1970-03-30"));
        marriageRequest.setGender(Gender.MALE);
        marriageRequest.setCitizenship(Citizenship.USA);

        marriageRequest.setFirstNamePartner("Pamela");
        marriageRequest.setFamilyNamePartner("DiCaprio");
        marriageRequest.setBirthDatePartner(LocalDate.parse("1967-07-01"));
        marriageRequest.setGenderPartner(Gender.FEMALE);
        marriageRequest.setCitizenshipPartner(Citizenship.CANADA);
        marriageRequest.setDateOfEvent(LocalDate.parse("2024-06-24"));

        Optional<Marriage> marriageOp = marriageService.create(marriageRequest);
        System.out.println(
                "-->> error wrong_create_where_partner_has_active_marriage: " + marriageService.getErrorMessages());

        assertThat(marriageOp.isPresent()).isFalse();
    }

    @Test
    void wrong_create_where_partners_have_nuclearfamily_relations() throws Exception {

        var marriageRequest = new MarriageRequest();
        marriageRequest.setFirstName("Chris");
        marriageRequest.setFamilyName("Watson");
        marriageRequest.setBirthDate(LocalDate.parse("1960-10-03"));
        marriageRequest.setGender(Gender.MALE);
        marriageRequest.setCitizenship(Citizenship.UK);

        marriageRequest.setFirstNamePartner("Emma");
        marriageRequest.setFamilyNamePartner("Watson");
        marriageRequest.setBirthDatePartner(LocalDate.parse("1990-04-15"));
        marriageRequest.setGenderPartner(Gender.FEMALE);
        marriageRequest.setCitizenshipPartner(Citizenship.UK);

        Optional<Marriage> marriageOp = marriageService.create(marriageRequest);
        System.out.println("-->> error wrong_create_where_partners_have_nuclearfamily_relations: "
                + marriageService.getErrorMessages());

        assertThat(marriageOp.isPresent()).isFalse();
    }

    @Test
    void wrong_create_where_partners_have_nuclearfamily_relations_sibling() throws Exception {

        var marriageRequest = new MarriageRequest();
        marriageRequest.setFirstName("Alex");
        marriageRequest.setFamilyName("Watson");
        marriageRequest.setBirthDate(LocalDate.parse("1995-11-04"));
        marriageRequest.setGender(Gender.MALE);
        marriageRequest.setCitizenship(Citizenship.UK);

        marriageRequest.setFirstNamePartner("Emma");
        marriageRequest.setFamilyNamePartner("Watson");
        marriageRequest.setBirthDatePartner(LocalDate.parse("1990-04-15"));
        marriageRequest.setGenderPartner(Gender.FEMALE);
        marriageRequest.setCitizenshipPartner(Citizenship.UK);

        Optional<Marriage> marriageOp = marriageService.create(marriageRequest);
        System.out.println("-->> error wrong_create_where_partners_have_nuclearfamily_relations_sibling: "
                + marriageService.getErrorMessages());

        assertThat(marriageOp.isPresent()).isFalse();
    }

    @Test
    void create_success_marriage() throws Exception {

        var marriageRequest = new MarriageRequest();
        marriageRequest.setFirstName("Wednesday");
        marriageRequest.setFamilyName("Addams");
        marriageRequest.setBirthDate(LocalDate.parse("1995-02-16"));
        marriageRequest.setGender(Gender.FEMALE);
        marriageRequest.setCitizenship(Citizenship.USA);

        marriageRequest.setFirstNamePartner("Justin");
        marriageRequest.setFamilyNamePartner("DiCaprio");
        marriageRequest.setBirthDatePartner(LocalDate.parse("1992-05-02"));
        marriageRequest.setGenderPartner(Gender.MALE);
        marriageRequest.setCitizenshipPartner(Citizenship.CANADA);

        Optional<Marriage> marriageOp = marriageService.create(marriageRequest);

        assertThat(marriageOp.isPresent()).isTrue();
        assertThat(marriageService.getErrorMessages().replaceAll("[\\[\\]]", "")).isEmpty();
    }

    @Test
    void dissolution_active_marriage() throws Exception {

        var marriageRequest = new MarriageRequest();
        marriageRequest.setFirstName("Gomez");
        marriageRequest.setFamilyName("Addams");
        marriageRequest.setBirthDate(LocalDate.parse("1970-03-30"));
        marriageRequest.setGender(Gender.MALE);
        marriageRequest.setCitizenship(Citizenship.USA);

        marriageRequest.setFirstNamePartner("Morticia");
        marriageRequest.setFamilyNamePartner("Addams");
        marriageRequest.setBirthDatePartner(LocalDate.parse("1973-04-28"));
        marriageRequest.setGenderPartner(Gender.FEMALE);
        marriageRequest.setCitizenshipPartner(Citizenship.USA);
        // marriageRequest.setDateOfEvent(LocalDate.parse("2024-06-24"));

        Optional<Marriage> marriageOp = marriageService.dissolution(marriageRequest);

        assertThat(marriageOp.isPresent()).isTrue();
        assertThat(marriageService.getErrorMessages().replaceAll("[\\[\\]]", "")).isEmpty();

        System.out.println("-->> error dissolution_active_marriage: " + marriageService.getErrorMessages());
        System.out.println("-->> marriage: " + marriageOp.get().toString());
    }

}
