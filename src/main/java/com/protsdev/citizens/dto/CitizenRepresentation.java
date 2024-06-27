package com.protsdev.citizens.dto;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.hateoas.RepresentationModel;

import com.protsdev.citizens.enums.Citizenship;
import com.protsdev.citizens.enums.Gender;

import lombok.Getter;

@Getter
public class CitizenRepresentation extends RepresentationModel<CitizenRepresentation> {
    private String firstName;
    private String familyName;
    private LocalDate birthDay;
    private Gender gender;
    private Citizenship citizenship;

    public String getNullFields() {
        var errors = new ArrayList<String>();

        if (firstName == null)
            errors.add("firstName");

        if (familyName == null)
            errors.add("familyName");
        if (birthDay == null)
            errors.add("birthDay");
        if (gender == null)
            errors.add("gender");
        if (citizenship == null)
            errors.add("citizenship");

        return String.join(",", errors);
    }

    @Override
    public String toString() {
        return "CitizenRepresentation [firstName=" + firstName + ", familyName=" + familyName + ", birthDay=" + birthDay
                + ", gender=" + gender + ", citizenship=" + citizenship + "]";
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName.trim();
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setCitizenship(Citizenship citizenship) {
        this.citizenship = citizenship;
    }
}