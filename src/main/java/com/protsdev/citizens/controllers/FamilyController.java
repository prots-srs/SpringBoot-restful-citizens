package com.protsdev.citizens.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
// import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.protsdev.citizens.domain.FamilyService;
import com.protsdev.citizens.domain.CitizenService;
import com.protsdev.citizens.dto.FamilyRepresentation;
import com.protsdev.citizens.dto.CitizenRequest;
import com.protsdev.citizens.dto.FamilyExtended;
import com.protsdev.citizens.dto.FamilyNuclear;
import com.protsdev.citizens.models.Citizen;
import com.protsdev.citizens.services.FormTemplateModel;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@RestController
@RequestMapping("/api/family")
public class FamilyController {

    @Autowired
    private CitizenService citizenService;

    @Autowired
    private FamilyService citizenRelations;

    @Autowired
    private FormTemplateModel formTemplateFactory;

    @GetMapping
    public ResponseEntity<?> index() {
        var model = HalModelBuilder
                .emptyHalModel()
                .link(linkTo(methodOn(FamilyController.class).index()).withSelfRel())
                .link(linkTo(methodOn(FamilyController.class).nuclear(null, null)).withRel("nuclear"))
                .link(linkTo(methodOn(FamilyController.class).extended(null, null)).withRel("extended"))
                .build();

        return ResponseEntity.ok(model);
    }

    @GetMapping("/nuclear")
    public ResponseEntity<?> nuclear(
            @Valid @ModelAttribute CitizenRequest iFs,
            BindingResult bR) {

        String errorMessage = "";

        if (bR.hasErrors()) {
            errorMessage = bR.getAllErrors().toString();
        } else {
            Optional<Citizen> citizen = citizenService.fetch(iFs);

            if (citizen.isPresent()) {
                FamilyNuclear family = citizenRelations.getNuclearFamily(citizen.get());

                var citizenFamily = new FamilyRepresentation(family);
                citizenFamily.add(linkTo(methodOn(FamilyController.class).nuclear(null, null)).withSelfRel());
                citizenFamily.add(linkTo(methodOn(FamilyController.class).index()).withRel("index"));

                return ResponseEntity.ok(citizenFamily);
            } else {
                errorMessage = citizenService.getLastError();
            }
        }

        return ResponseEntity
                .badRequest()
                .body(formTemplateFactory.getCitizenFormTemplateModel(errorMessage,
                        linkTo(methodOn(FamilyController.class).nuclear(null, null)).withSelfRel()));
    }

    @GetMapping("/extended")
    public ResponseEntity<?> extended(
            @Valid @ModelAttribute CitizenRequest iFs,
            BindingResult bR) {

        String errorMessage = "";

        if (bR.hasErrors()) {
            errorMessage = bR.getAllErrors().toString();
        } else {
            Optional<Citizen> citizen = citizenService.fetch(iFs);

            if (citizen.isPresent()) {

                FamilyExtended family = citizenRelations.getExtendedFamily(citizen.get());
                var citizenFamily = new FamilyRepresentation(family);
                citizenFamily.add(linkTo(methodOn(FamilyController.class).extended(null, null)).withSelfRel());
                citizenFamily.add(linkTo(methodOn(FamilyController.class).index()).withRel("index"));

                return ResponseEntity.ok(citizenFamily);
            } else {
                errorMessage = citizenService.getLastError();
            }
        }

        return ResponseEntity
                .badRequest()
                .body(formTemplateFactory.getCitizenFormTemplateModel(errorMessage,
                        linkTo(methodOn(FamilyController.class).extended(null, null)).withSelfRel()));
    }

}
