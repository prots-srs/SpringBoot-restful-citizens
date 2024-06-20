package com.protsdev.citizens;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.protsdev.citizens.enums.Citizenship;
import com.protsdev.citizens.enums.Gender;
import com.protsdev.citizens.services.VerifyFields;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;
import java.util.Optional;

@SpringBootTest
public class VerifyTests {

    @Autowired
    private VerifyFields verify;

    @Test
    void testValidEmail() {
        String rightEmail = "john.wick@example.com";
        String wrongEmail1 = "john.wick#example.com";
        String wrongEmail2 = "john.wick@";
        String wrongEmail3 = "@example.com";

        assertThat(verify.checkEmail(rightEmail)).isTrue();
        assertThat(verify.checkEmail(wrongEmail1)).isFalse();
        assertThat(verify.checkEmail(wrongEmail2)).isFalse();
        assertThat(verify.checkEmail(wrongEmail3)).isFalse();
    }

    @Test
    void check_verify_income_date() {
        String birthDayFake = "1995/02.16";
        String birthDayRight = "1995-02-16";

        Optional<Date> dateA = verify.getDateByString(birthDayFake);
        assertThat(dateA.isPresent()).isFalse();

        Optional<Date> dateB = verify.getDateByString(birthDayRight);
        assertThat(dateB.isPresent()).isTrue();
    }

    @Test
    void check_verify_income_gender() {
        String genderFake = "feMale";
        String genderRight = genderFake.toUpperCase();

        Optional<Gender> genderA = verify.getGenderByString(genderFake);
        assertThat(genderA.isPresent()).isFalse();

        Optional<Gender> genderB = verify.getGenderByString(genderRight);
        assertThat(genderB.isPresent()).isTrue();
    }

    @Test
    void check_verify_income_citizenship() {
        String citizenshipFake = "sha";
        String citizenshipRight = "CANADA";

        Optional<Citizenship> citizenshipA = verify.getCitizenshipByString(citizenshipFake);
        assertThat(citizenshipA.isPresent()).isFalse();

        Optional<Citizenship> citizenshipB = verify.getCitizenshipByString(citizenshipRight);
        assertThat(citizenshipB.isPresent()).isTrue();
    }

}
