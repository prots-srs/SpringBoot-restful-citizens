package com.protsdev.citizens.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.protsdev.citizens.domain.CitizenService;
import com.protsdev.citizens.dto.CitizenRequestUpdate;
import com.protsdev.citizens.dto.CitizenRequest;
import com.protsdev.citizens.models.Citizen;
import com.protsdev.citizens.services.FormTemplateModel;
import jakarta.validation.Valid;

/*
 * get + form: receive citizen with id + hashcode
 * post + form: store citizen
 * post to /id + formEx: check id + hashcode and update certain fields
 */
@RestController
@RequestMapping("/api/citizen")
public class CitizenController {
    @Autowired
    private CitizenService citizenService;
    @Autowired
    private CitizenModelAssembler assembler;
    @Autowired
    private FormTemplateModel formTemplateFactory;

    // public CitizenController(
    // CitizenService citizenService,
    // CitizenModelAssembler assembler,
    // StorageService storageService) {
    // this.citizenService = citizenService;
    // this.assembler = assembler;
    // }

    @GetMapping
    public ResponseEntity<?> index(@Valid @ModelAttribute CitizenRequest iFs,
            BindingResult bR) {

        Link selfLink = linkTo(methodOn(CitizenController.class).index(null, null)).withSelfRel();
        selfLink.andAffordance(afford(methodOn(CitizenController.class).store(null, null)));

        String errorMessage;
        if (bR.hasErrors()) {
            errorMessage = bR.getAllErrors().toString();
        } else {

            Optional<Citizen> citizen = citizenService.fetch(iFs);
            if (citizen.isPresent()) {
                return ResponseEntity.ok(assembler.toModel(citizen.get()));
            } else {
                errorMessage = citizenService.getLastError();
            }
        }

        return ResponseEntity
                .badRequest()
                .body(formTemplateFactory.getCitizenFormTemplateModel(errorMessage, selfLink));
    }

    /*
     * create new citizen|update current
     */
    @PostMapping
    public ResponseEntity<?> store(@Valid @ModelAttribute CitizenRequestUpdate iFs,
            BindingResult bR) {

        String errorMessage = "";
        if (bR.hasErrors()) {
            errorMessage = bR.getAllErrors().toString();
        } else {
            Optional<Citizen> citizen = citizenService.store(iFs);
            if (citizen.isPresent()) {
                return ResponseEntity
                        // // .created(linkTo(methodOn(CitizenController.class).newCitizen(null,
                        // // null)).toUri())
                        .status(HttpStatus.CREATED)
                        .body(assembler.toModel(citizen.get()));
            } else {
                errorMessage = citizenService.getLastError();
            }
        }

        return ResponseEntity.badRequest()
                .body(formTemplateFactory.getCitizenFormTemplateModel(
                        errorMessage,
                        linkTo(methodOn(CitizenController.class).store(null, null)).withSelfRel()));
    }
}