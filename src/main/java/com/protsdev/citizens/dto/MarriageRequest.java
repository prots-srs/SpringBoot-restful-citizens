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
public class MarriageRequest extends CitizenRequest {
    @NotBlank
    @Size(max = 64)
    protected String firstNamePartner;

    @NotBlank
    @Size(max = 64)
    protected String familyNamePartner;

    // @NotBlank
    @Size(max = 64)
    protected String secondNamePartner;

    @NotNull
    protected LocalDate birthDatePartner;

    @NotNull
    protected Gender genderPartner;

    @NotNull
    protected Citizenship citizenshipPartner;

    private LocalDate dateOfEvent = LocalDate.now();
    private Boolean givePartnerFamilyNameCitizen = Boolean.valueOf(true);
}
