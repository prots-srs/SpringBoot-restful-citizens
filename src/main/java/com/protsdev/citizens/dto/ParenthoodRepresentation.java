package com.protsdev.citizens.dto;

import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.protsdev.citizens.enums.ParenthoodType;

import lombok.Getter;

@Getter
public class ParenthoodRepresentation extends RepresentationModel<ParenthoodRepresentation> {
    private final CitizenView parent;
    private final CitizenView child;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final ParenthoodType type;

    @JsonCreator
    public ParenthoodRepresentation(
            @JsonProperty("parent") CitizenView parent,
            @JsonProperty("child") CitizenView child,
            @JsonProperty("startDate") LocalDate startDate,
            @JsonProperty("endDate") LocalDate endDate,
            @JsonProperty("type") ParenthoodType type) {
        this.parent = parent;
        this.child = child;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
    }

}
