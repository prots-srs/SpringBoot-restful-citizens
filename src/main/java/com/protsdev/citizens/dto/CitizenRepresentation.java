package com.protsdev.citizens.dto;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CitizenRepresentation extends RepresentationModel<CitizenRepresentation> {
    private final CitizenView citizen;
    // private final Integer hashCode;
    // private final Long id;

    // public Long getId() {
    // return id;
    // }

    // public Integer getHashCode() {
    // return hashCode;
    // }

    public CitizenView getCitizen() {
        return citizen;
    }

    @JsonCreator
    public CitizenRepresentation(
            @JsonProperty("citizen") CitizenView citizen) {
        // @JsonProperty("id") Long id,
        // @JsonProperty("hashCode") Integer hashCode) {
        this.citizen = citizen;
        // this.hashCode = hashCode;
        // this.id = id;
    }
}
