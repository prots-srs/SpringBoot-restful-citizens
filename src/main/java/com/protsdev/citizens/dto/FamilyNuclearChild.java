package com.protsdev.citizens.dto;

import java.util.Set;

public record FamilyNuclearChild(
                CitizenView citizen,
                Set<CitizenView> birthparents,
                Set<CitizenView> adopters,
                Set<CitizenView> sibling) implements FamilyNuclear {
}
