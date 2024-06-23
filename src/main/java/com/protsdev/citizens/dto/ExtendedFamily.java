package com.protsdev.citizens.dto;

import java.util.Set;

public record ExtendedFamily(
                CitizenView citizen,
                Set<CitizenView> partner,
                Set<CitizenView> children,
                Set<CitizenView> birthparents,
                Set<CitizenView> adopters,
                Set<CitizenView> siblings) implements NuclearFamily {

}
