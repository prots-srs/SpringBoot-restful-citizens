package com.protsdev.citizens.dto;

import java.util.Set;

public record NuclearFamilyAdult(
        CitizenView citizen,
        Set<CitizenView> partner,
        Set<CitizenView> children) implements NuclearFamily {

}
