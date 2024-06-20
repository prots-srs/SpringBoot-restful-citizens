package com.protsdev.citizens.services;

import java.sql.Date;
import java.util.Optional;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.protsdev.citizens.enums.Citizenship;
import com.protsdev.citizens.enums.Gender;

@Service
public class VerifyFields {
    private static final Logger log = LoggerFactory.getLogger(VerifyFields.class);

    private final String EMAIL_PATTERN = "^(.+)@(\\S+)$";

    public boolean checkEmail(String email) {
        return Pattern.compile(this.EMAIL_PATTERN)
                .matcher(email)
                .matches();
    }

    public Optional<Date> getDateByString(String maybeDate) {
        Optional<Date> dateOpt = Optional.empty();

        try {
            Date date = Date.valueOf(maybeDate);
            dateOpt = Optional.of(date);
        } catch (Exception e) {
            log.error("Parse date: '" + maybeDate + "' " + e.getClass());
        }
        return dateOpt;
    }

    public Optional<Gender> getGenderByString(String maybeGender) {
        Optional<Gender> genderOpt = Optional.empty();

        try {
            Gender gender = Gender.valueOf(maybeGender);
            genderOpt = Optional.of(gender);
        } catch (Exception e) {
            log.error("Parse gender: '" + maybeGender + "' " + e.getClass());
        }
        return genderOpt;
    }

    public Optional<Citizenship> getCitizenshipByString(String maybeCitizenship) {
        Optional<Citizenship> citizenshipOpt = Optional.empty();

        try {
            Citizenship citizenship = Citizenship.valueOf(maybeCitizenship);
            citizenshipOpt = Optional.of(citizenship);
        } catch (Exception e) {
            log.error("Parse citizenship: '" + maybeCitizenship + "' " + e.getClass());
        }
        return citizenshipOpt;
    }
}
