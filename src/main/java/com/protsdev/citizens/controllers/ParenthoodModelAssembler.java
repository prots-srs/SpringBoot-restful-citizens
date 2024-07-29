package com.protsdev.citizens.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.protsdev.citizens.dto.CitizenView;
import com.protsdev.citizens.dto.ParenthoodRepresentation;
import com.protsdev.citizens.models.Parenthood;

@Component
public class ParenthoodModelAssembler implements RepresentationModelAssembler<Parenthood, ParenthoodRepresentation> {

    @SuppressWarnings("null")
    @Override
    public ParenthoodRepresentation toModel(Parenthood entity) {

        var pR = new ParenthoodRepresentation(
                CitizenView.convertCitizenToView(entity.getCitizen()),
                CitizenView.convertCitizenToView(entity.getChild()),
                entity.getDateRights().getStartDate(),
                entity.getDateRights().getEndDate(),
                entity.getType());

        pR.add(linkTo(methodOn(ParenthoodControllers.class).index()).withRel("index"));

        return pR;
    }

}
