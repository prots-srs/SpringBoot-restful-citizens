package com.protsdev.citizens.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.protsdev.citizens.dto.CitizenRepresentation;
import com.protsdev.citizens.dto.CitizenView;
import com.protsdev.citizens.models.Citizen;

@Component
public class CitizenModelAssembler implements
        RepresentationModelAssembler<Citizen, CitizenRepresentation> {

    @SuppressWarnings("null")
    @Override
    public CitizenRepresentation toModel(Citizen ci) {
        var cR = new CitizenRepresentation(
                CitizenView.convertCitizenToView(ci));
        cR.add(
                linkTo(methodOn(CitizenController.class).index(null, null)).withSelfRel());

        return cR;
    }

}
