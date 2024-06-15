package com.protsdev.citizens.domain;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

public class CitizenRelations {
    private static final Integer ADULT_AGE_FROM = 18;

    // private Citizen citizen;

    public <T> Optional<T> getNuclearFamily(String name, String family, String birth, String gender) {
        Optional<T> result = Optional.empty();

        // Optional<Citizen> citizen = this.citizens.stream()
        // .filter(ci -> ci.getNames().getFamily().equals(family)
        // && ci.getNames().getFirst().equals(name)
        // && ci.getStageDays().getBirth().equals(LocalDate.parse(birth))
        // && Sex.valueOf(sex).equals(ci.getSex()))
        // .findFirst();

        // if (citizen.isPresent()) {
        // Citizen ci = citizen.get();

        // UICitizen uiCitizen = new UICitizen(
        // ci.getNames().getFirst() + " " + ci.getNames().getFamily(),
        // ci.getStageDays().getBirth().toString(),
        // ci.getStageDays().getDeath() != null ?
        // ci.getStageDays().getDeath().toString() : "",
        // ci.getSex().toString());

        // if (checkAdultAge(ci.getStageDays().getBirth())) {
        // result = Optional.of(
        // new NuclearFamilyAdult(
        // uiCitizen,
        // new UICitizen("", "", "", ""),
        // new LinkedList<UICitizen>()));
        // } else {
        // result = Optional.of(
        // new NuclearFamilyChild(
        // uiCitizen,
        // new LinkedList<UICitizen>(),
        // new LinkedList<UICitizen>()));
        // }
        // }
        return result;
    }

    private boolean checkAdultAge(LocalDate date) {
        Integer years = Period.between(date, LocalDate.now()).getYears();

        return years >= ADULT_AGE_FROM;
    }
}
