package com.protsdev.citizens.dto;

import java.util.Set;

public record FamilyNuclearAdult(
        CitizenView citizen,
        Set<CitizenView> partner,
        Set<CitizenView> children) implements FamilyNuclear {

}
