package com.protsdev.citizens.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.util.UriComponentsBuilder;

import com.protsdev.citizens.domain.CitizenFamilyService;
import com.protsdev.citizens.dto.FieldsDefiningCitizen;
import com.protsdev.citizens.dto.NuclearFamily;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/family")
public class CitizenController {
    @Autowired
    CitizenFamilyService citizenRelations;

    @GetMapping("/nuclear")
    public ResponseEntity<NuclearFamily> getNuclearFamily(
            FieldsDefiningCitizen inputFields,
            BindingResult result) {

        // check binding errors
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().eTag(result.getAllErrors().toString()).build();
        }

        Optional<NuclearFamily> family = citizenRelations.getNuclearFamily(inputFields);

        if (family.isPresent()) {
            return ResponseEntity.ok(family.get());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
