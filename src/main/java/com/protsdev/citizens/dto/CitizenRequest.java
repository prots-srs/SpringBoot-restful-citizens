package com.protsdev.citizens.dto;

import java.time.LocalDate;

import com.protsdev.citizens.enums.Citizenship;
import com.protsdev.citizens.enums.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CitizenRequest {

    @NotBlank
    @Size(max = 64)
    protected String firstName;

    @NotBlank
    @Size(max = 64)
    protected String familyName;

    // @NotBlank
    @Size(max = 64)
    protected String secondName;

    @NotNull
    protected LocalDate birthDate;

    @NotNull
    protected Gender gender;

    @NotNull
    protected Citizenship citizenship;

    public void setFirstName(String name) {
        firstName = name.trim();
    }

    public void setFamilyName(String name) {
        familyName = name.trim();
    }

    public void setSecondName(String name) {
        if (name == null) {
            name = "";
        }
        secondName = name.trim();
    }

    public void setBirthDate(LocalDate date) {
        birthDate = date;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setCitizenship(Citizenship citizenship) {
        this.citizenship = citizenship;
    }
}