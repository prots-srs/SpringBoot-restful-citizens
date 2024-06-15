package com.protsdev.citizens.domain.family_models;

import java.util.List;

public record NuclearFamilyChild(
                CitizenView citizen,
                List<CitizenView> birthparents,
                List<CitizenView> adopters,
                List<CitizenView> brothers) {
}
