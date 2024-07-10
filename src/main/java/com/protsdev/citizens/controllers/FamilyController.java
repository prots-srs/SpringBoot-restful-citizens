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

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/family")
public class FamilyController {

  @Autowired
  private CitizenService citizenService;

  @Autowired
  private FamilyService citizenRelations;

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
      CitizenRequest inputFields,
      BindingResult bindingResult) {

    String errorMessage = "";

    if (bindingResult.hasErrors()) {
      errorMessage = bindingResult.getAllErrors().toString();
    } else {
      Optional<Citizen> citizen = citizenService.fetchCitizen(inputFields);

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
        .body(FormTemplateFactory.buildCitizenFormTemplateModel(errorMessage,
            linkTo(methodOn(FamilyController.class).nuclear(null, null)).withSelfRel(),
            linkTo(methodOn(FamilyController.class).index()).withRel("index")));
  }

  @GetMapping("/extended")
  public ResponseEntity<?> extended(
      CitizenRequest inputFields,
      BindingResult bindingResult) {

    String errorMessage = "";

    if (bindingResult.hasErrors()) {
      errorMessage = bindingResult.getAllErrors().toString();
    } else {
      Optional<Citizen> citizen = citizenService.fetchCitizen(inputFields);

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
        .body(FormTemplateFactory.buildCitizenFormTemplateModel(errorMessage,
            linkTo(methodOn(FamilyController.class).extended(null, null)).withSelfRel(),
            linkTo(methodOn(FamilyController.class).index()).withRel("index")));
  }

}
