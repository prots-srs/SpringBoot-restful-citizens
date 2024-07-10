package com.protsdev.citizens.dto;

import com.protsdev.citizens.models.Citizen;

public record CitizenView(
    String name,
    String lifeDays,
    String gender,
    String citizenship) {

  /*
   * Output View
   */
  public static CitizenView convertCitizenToView(Citizen citizen) {
    String name = citizen.getNames().getFirstName() + " " + citizen.getNames().getFamilyName();
    if (citizen.getNames().getMaidenName().length() > 0) {
      name = name + " (" + citizen.getNames().getMaidenName() + ")";
    }

    String lifeDays = citizen.getDays().getBirthDay().toString();
    if (citizen.getDays().getDeathDay() != null) {
      lifeDays = lifeDays + " - " + citizen.getDays().getDeathDay().toString();
    }

    return new CitizenView(
        name,
        lifeDays,
        citizen.getGender().toString(),
        citizen.getCitizenship().toString());
  }
}
