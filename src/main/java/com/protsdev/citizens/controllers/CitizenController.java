package com.protsdev.citizens.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
// import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.NonComposite;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.protsdev.citizens.domain.CitizenFamilyService;
import com.protsdev.citizens.dto.CitizenFamilyResponse;
import com.protsdev.citizens.dto.FieldsDefiningCitizen;
import com.protsdev.citizens.dto.NuclearFamily;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/family")
public class CitizenController {
    @Autowired
    CitizenFamilyService citizenRelations;

    @GetMapping("/nuclear")
    public ResponseEntity<CitizenFamilyResponse> getNuclearFamily(
            @NonComposite @RequestParam Collection<String> citizen) {

        if (citizen.size() != 5) {
            return ResponseEntity.badRequest().build();
        }

        String[] arCitizen = new String[citizen.size()];
        arCitizen = citizen.toArray(arCitizen);

        var inputFields = new FieldsDefiningCitizen(
                arCitizen[0], arCitizen[1], arCitizen[2], arCitizen[3], arCitizen[4]);

        Optional<NuclearFamily> family = citizenRelations.getNuclearFamily(inputFields);

        if (family.isPresent()) {
            // EntityModel<NuclearFamily> model = EntityModel.of(family.get());
            var citizenFamily = new CitizenFamilyResponse(family.get());
            citizenFamily
                    .add(linkTo(methodOn(CitizenController.class).getNuclearFamily(citizen)).withSelfRel());
            citizenFamily
                    .add(linkTo(methodOn(CitizenController.class).getExtendedFamily(citizen))
                            .withRel("extended"));

            return ResponseEntity.ok(citizenFamily);
        } else {
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/extended")
    public ResponseEntity<CitizenFamilyResponse> getExtendedFamily(
            @NonComposite @RequestParam Collection<String> citizen) {
        // FieldsDefiningCitizen inputFields,
        // BindingResult result) {

        // check binding errors
        // if (result.hasErrors()) {
        // return
        // ResponseEntity.badRequest().eTag(result.getAllErrors().toString()).build();
        // }

        if (citizen.size() != 5) {
            return ResponseEntity.badRequest().build();
        }

        String[] arCitizen = new String[citizen.size()];
        arCitizen = citizen.toArray(arCitizen);

        FieldsDefiningCitizen inputFields = new FieldsDefiningCitizen(
                arCitizen[0], arCitizen[1], arCitizen[2], arCitizen[3], arCitizen[4]);

        Optional<NuclearFamily> family = citizenRelations.getExtendedFamily(inputFields);

        if (family.isPresent()) {
            CitizenFamilyResponse citizenFamily = new CitizenFamilyResponse(family.get());
            citizenFamily
                    .add(linkTo(methodOn(CitizenController.class).getExtendedFamily(citizen))
                            .withSelfRel());
            citizenFamily
                    .add(linkTo(methodOn(CitizenController.class).getNuclearFamily(citizen))
                            .withRel("nuclear"));

            return ResponseEntity.ok(citizenFamily);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
