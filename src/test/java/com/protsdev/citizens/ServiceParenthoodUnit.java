package com.protsdev.citizens;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.protsdev.citizens.domain.ParenthoodService;
import com.protsdev.citizens.dto.ParenthoodRequest;
import com.protsdev.citizens.enums.Citizenship;
import com.protsdev.citizens.enums.Gender;
import com.protsdev.citizens.enums.ParenthoodType;
import com.protsdev.citizens.models.Citizen;
import com.protsdev.citizens.models.Parenthood;
import com.protsdev.citizens.repositories.CitizenRepository;
import com.protsdev.citizens.repositories.ParenthoodRepository;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class ServiceParenthoodUnit {
    @Autowired
    private ParenthoodService parenthoodService;

    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired
    private ParenthoodRepository parenthoodRepository;

    @AfterAll
    void view_all_citizens() {
        List<Citizen> ci = citizenRepository.findAll();
        ci.forEach(c -> System.out.println(String.format("-->> citizen: id=%s, name=%s", c.getId(), c.getNames())));

        List<Parenthood> prh = parenthoodRepository.findAll();
        prh.forEach(p -> System.out.println(
                String.format(
                        "-->> parenthood id=%s, citizen name=%s, child name=%s, start right=%s, end rights=%s, type=%s",
                        p.getId(),
                        p.getCitizen().getNames(),
                        p.getChild().getNames(),
                        p.getDateRights().getStartDate(),
                        p.getDateRights().getEndDate(),
                        p.getType())));
    }

    @Test
    void should_contain_error() throws Exception {
        var request = new ParenthoodRequest();
        // current citizen
        request.setFirstName("Morticiam");
        request.setFamilyName("Addams");
        request.setBirthDate(LocalDate.parse("1973-04-28"));
        request.setGender(Gender.FEMALE);
        request.setCitizenship(Citizenship.USA);
        // current child
        request.setChildFirstName("Oprah");
        request.setChildFamilyName("Addams");
        request.setChildBirthDate(LocalDate.parse("2009-05-14"));
        request.setChildGender(Gender.FEMALE);
        request.setChildCitizenship(Citizenship.USA);
        // service
        request.setDateRights(LocalDate.parse("2023-04-02"));
        request.setParenthoodType(ParenthoodType.BIRTHMOTHER);

        // assert citizen missing
        parenthoodService.giveRight(request);
        assertThat(parenthoodService.getErrorMessages().length() > 2).isTrue();
        System.out.println("-->> error citizen not found: " + parenthoodService.getErrorMessages());

        // assert child not missing
        request.setFirstName("Morticia");
        parenthoodService.giveRight(request);
        assertThat(parenthoodService.getErrorMessages().length() > 2).isTrue();
        System.out.println("-->> error child exists: " + parenthoodService.getErrorMessages());

        // assert birth date and date start rights
        request.setChildBirthDate(LocalDate.parse("2000-05-14"));
        parenthoodService.giveRight(request);
        assertThat(parenthoodService.getErrorMessages().length() > 2).isTrue();
        System.out.println("-->> error date right is fail: " + parenthoodService.getErrorMessages());
    }

    @Test
    void should_create_child_by_mother_father() throws Exception {
        var request = new ParenthoodRequest();
        // citizen
        request.setFirstName("Morticia");
        request.setFamilyName("Addams");
        request.setBirthDate(LocalDate.parse("1973-04-28"));
        request.setGender(Gender.FEMALE);
        request.setCitizenship(Citizenship.USA);
        // child
        request.setChildFirstName("Monica");
        request.setChildFamilyName("Addams");
        request.setChildBirthDate(LocalDate.parse("2023-04-02"));
        request.setChildGender(Gender.FEMALE);
        request.setChildCitizenship(Citizenship.USA);
        // service
        request.setDateRights(LocalDate.parse("2023-04-02"));
        request.setParenthoodType(ParenthoodType.BIRTHMOTHER);

        parenthoodService.giveRight(request);
        // System.out.println("-->> error should_create_child_by_mother: " +
        // parenthoodService.getErrorMessages());
        assertThat(parenthoodService.getErrorMessages().length()).isEqualTo(2);

        request.setFirstName("Gomez");
        request.setFamilyName("Addams");
        request.setBirthDate(LocalDate.parse("1970-03-30"));
        request.setGender(Gender.MALE);
        request.setCitizenship(Citizenship.USA);
        request.setParenthoodType(ParenthoodType.BIRTHFATHER);

        parenthoodService.giveRight(request);
        // System.out.println("-->> error should_create_parenthood_by_father: " +
        // parenthoodService.getErrorMessages());
        assertThat(parenthoodService.getErrorMessages().length()).isEqualTo(2);

        request.setFirstName("Leonardo");
        request.setFamilyName("DiCaprio");
        request.setBirthDate(LocalDate.parse("1965-02-22"));
        request.setGender(Gender.MALE);
        request.setCitizenship(Citizenship.CANADA);
        request.setDateRights(LocalDate.parse("2024-01-03"));
        request.setParenthoodType(ParenthoodType.GODFATHER);

        parenthoodService.giveRight(request);
        // System.out.println("-->> error should_create_parenthood_by_father: " +
        // parenthoodService.getErrorMessages());
        assertThat(parenthoodService.getErrorMessages().length()).isEqualTo(2);
    }

    @Test
    void should_lost_right() {
        var request = new ParenthoodRequest();
        // citizen
        request.setFirstName("Morticia");
        request.setFamilyName("Addams");
        request.setBirthDate(LocalDate.parse("1973-04-28"));
        request.setGender(Gender.FEMALE);
        request.setCitizenship(Citizenship.USA);
        // child
        request.setChildFirstName("Pugsley");
        request.setChildFamilyName("Addams");
        request.setChildBirthDate(LocalDate.parse("1998-09-29"));
        request.setChildGender(Gender.MALE);
        request.setChildCitizenship(Citizenship.USA);

        // service
        request.setDateRights(LocalDate.parse("2024-04-02"));
        request.setParenthoodType(ParenthoodType.BIRTHMOTHER);

        parenthoodService.lostRight(request);
        // System.out.println("-->> error should_lost_right: " +
        // parenthoodService.getErrorMessages());
        assertThat(parenthoodService.getErrorMessages().length()).isEqualTo(2);
    }
}
