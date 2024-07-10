package com.protsdev.citizens.dto;

import java.time.LocalDate;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MarriageRequest {
  private Long idCitizenA;
  private Integer hashCodeCitizenA;
  private Long idCitizenB;
  private Integer hashCodeCitizenB;
  private LocalDate dateOfEvent = LocalDate.now();
  private Boolean giveHusbandFamilyName = Boolean.valueOf(true);

  public String defineRequaredFields() {
    var errors = new ArrayList<String>();

    if (idCitizenA == null || idCitizenA.equals(Long.valueOf(0)))
      errors.add("idCitizenA");
    if (hashCodeCitizenA == null || hashCodeCitizenA.equals(Integer.valueOf(0)))
      errors.add("hashCodeCitizenA");

    if (idCitizenB == null || idCitizenB.equals(Long.valueOf(0)))
      errors.add("idCitizenB");
    if (hashCodeCitizenA == null || hashCodeCitizenA.equals(Integer.valueOf(0)))
      errors.add("hashCodeCitizenB");

    return String.join(",", errors);
  }
}
