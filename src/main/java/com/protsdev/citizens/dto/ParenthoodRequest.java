package com.protsdev.citizens.dto;

import java.time.LocalDate;

import com.protsdev.citizens.enums.Citizenship;
import com.protsdev.citizens.enums.Gender;
import com.protsdev.citizens.enums.ParenthoodType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ParenthoodRequest extends CitizenRequest {
    // child
    @NotBlank
    @Size(max = 64)
    private String childFirstName;

    @NotBlank
    @Size(max = 64)
    private String childFamilyName;

    // @NotBlank
    @Size(max = 64)
    private String childSecondName;

    @NotNull
    private LocalDate childBirthDate;

    @NotNull
    private Gender childGender;

    @NotNull
    private Citizenship childCitizenship;

    @NotNull
    private ParenthoodType parenthoodType;

    @NotNull
    private LocalDate dateRights;
}
