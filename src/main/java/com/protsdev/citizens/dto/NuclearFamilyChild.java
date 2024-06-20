package com.protsdev.citizens.dto;

import java.util.Set;

public record NuclearFamilyChild(
        CitizenView citizen,
        Set<CitizenView> birthparents,
        Set<CitizenView> adopters,
        Set<CitizenView> brothers) implements NuclearFamily {
}
