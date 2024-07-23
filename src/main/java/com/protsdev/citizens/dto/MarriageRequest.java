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
public class MarriageRequest {
    @NotBlank
    @Size(max = 64)
    protected String firstNameA;

    @NotBlank
    @Size(max = 64)
    protected String familyNameA;

    // @NotBlank
    @Size(max = 64)
    protected String secondNameA;

    @NotNull
    protected LocalDate birthDateA;

    @NotNull
    protected Gender genderA;

    @NotNull
    protected Citizenship citizenshipA;

    @NotBlank
    @Size(max = 64)
    protected String firstNameB;

    @NotBlank
    @Size(max = 64)
    protected String familyNameB;

    // @NotBlank
    @Size(max = 64)
    protected String secondNameB;

    @NotNull
    protected LocalDate birthDateB;

    @NotNull
    protected Gender genderB;

    @NotNull
    protected Citizenship citizenshipB;

    private LocalDate dateOfEvent = LocalDate.now();
    private Boolean giveBFamilyNameA = Boolean.valueOf(true);
}
