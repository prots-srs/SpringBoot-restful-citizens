package com.protsdev.citizens.domain.family_models;

public record CitizenView(
        String name,
        String birthDay,
        String deathDay,
        String gender,
        String citizenship) {
}
