package com.protsdev.citizens.dto;

import java.time.LocalDate;

import com.protsdev.citizens.enums.Citizenship;
import com.protsdev.citizens.enums.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
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
}