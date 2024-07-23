package com.protsdev.citizens.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.LinkedList;
import java.util.List;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.protsdev.citizens.dto.CitizenView;
import com.protsdev.citizens.dto.MarriageRepresentation;
import com.protsdev.citizens.models.Marriage;

@Component
public class MarriageModelAssembler implements RepresentationModelAssembler<Marriage, MarriageRepresentation> {

    @SuppressWarnings("null")
    @Override
    public MarriageRepresentation toModel(Marriage ma) {

        List<CitizenView> listCitizenView = new LinkedList<>();
        listCitizenView.add(CitizenView.convertCitizenToView(ma.getCitizen()));
        listCitizenView.add(CitizenView.convertCitizenToView(ma.getPartner()));

        var mR = new MarriageRepresentation(listCitizenView,
                ma.getDateRights().getStartDate(),
                ma.getDateRights().getEndDate());

        mR.add(linkTo(methodOn(MarriageController.class).index()).withRel("index"));

        return mR;
    }

}
