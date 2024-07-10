package com.protsdev.citizens.dto;

import java.time.LocalDate;

import com.protsdev.citizens.enums.Citizenship;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class CitizenRequestUpdate {
  private Integer hashCode;
  private String familyName;
  private LocalDate deathDay;
  private Citizenship citizenship;
}
