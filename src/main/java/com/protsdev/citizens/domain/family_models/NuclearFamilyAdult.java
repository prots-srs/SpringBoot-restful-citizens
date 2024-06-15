package com.protsdev.citizens.domain.family_models;

import java.util.List;

public record NuclearFamilyAdult(
                CitizenView citizen,
                CitizenView partner,
                List<CitizenView> children) {

}
