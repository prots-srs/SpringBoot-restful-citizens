package com.protsdev.citizens.dto;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CitizenFamilyResponse extends RepresentationModel<CitizenFamilyResponse> {

    private final NuclearFamily family;

    public NuclearFamily getFamily() {
        return family;
    }

    @JsonCreator
    public CitizenFamilyResponse(@JsonProperty("family") NuclearFamily family) {
        this.family = family;
    }

}
