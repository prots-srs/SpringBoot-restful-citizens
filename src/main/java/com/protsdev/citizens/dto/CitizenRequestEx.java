package com.protsdev.citizens.dto;

import java.time.LocalDate;

import com.protsdev.citizens.enums.Citizenship;

import lombok.Setter;
import lombok.ToString;
import lombok.Getter;

@Getter
@Setter
@ToString
public class CitizenRequestEx {
  private LocalDate deathDay;
  private Citizenship citizenship;
  private String familyName;
  private Integer hashCode;
}
