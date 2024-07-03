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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.protsdev.citizens.domain.CitizenService;
import com.protsdev.citizens.dto.CitizenRequestEx;
import com.protsdev.citizens.dto.CitizenRequest;
import com.protsdev.citizens.models.Citizen;

/*
 * get + form: receive citizen with id + hashcode
 * post + form: store citizen
 * post to /id + formEx: check id + hashcode and update certain fields
 */
@RestController
@RequestMapping("/api/citizen")
public class CitizenController {
  @Autowired
  CitizenService citizenService;

  @Autowired
  CitizenModelAssembler assembler;

  @GetMapping
  public ResponseEntity<?> index(CitizenRequest inputFields, BindingResult result) {

    Link selfLink = linkTo(methodOn(CitizenController.class).index(null, null)).withSelfRel();
    selfLink.andAffordance(afford(methodOn(CitizenController.class).create(null, null)));
    selfLink.andAffordance(afford(methodOn(CitizenController.class).update(null, null, null)));

    Optional<Citizen> citizen = citizenService.fetchCitizen(inputFields, result);
    if (citizen.isPresent()) {
      return ResponseEntity.ok(assembler.toModel(citizen.get()));
    }

    return ResponseEntity
        .badRequest()
        .body(FormTemplateFactory.buildCitizenFormTemplateModel(citizenService.getLastError(), selfLink, null));
  }

  /*
   * create new citizen
   */
  @PostMapping
  public ResponseEntity<?> create(CitizenRequest inputFields, BindingResult result) {

    String errorMessage;
    Optional<Citizen> citizen = citizenService.fetchCitizen(inputFields, result);

    if (citizen.isPresent()) {
      errorMessage = String.format(
          "Citizen by %s %s, birthDay at %s, has gender %s and citizenship %s is current present",
          inputFields.getFirstName(),
          inputFields.getFamilyName(),
          inputFields.getBirthDay(),
          inputFields.getGender(),
          inputFields.getCitizenship());
    } else {

      var requareError = inputFields.defineRequaredFields();
      if (requareError.length() > 0) {
        errorMessage = String.format("Fields '%s' have null value", requareError);
      } else {

        Optional<Citizen> citizenNew = citizenService.create(inputFields);

        if (citizenNew.isPresent()) {
          return ResponseEntity
              // .created(linkTo(methodOn(CitizenController.class).newCitizen(null,
              // null)).toUri())
              .status(HttpStatus.CREATED)
              .body(assembler.toModel(citizenNew.get()));
        } else {
          errorMessage = "Error persisting citizen";
        }
      }
    }

    return ResponseEntity.badRequest()
        .body(FormTemplateFactory.buildCitizenFormTemplateModel(
            errorMessage,
            linkTo(methodOn(CitizenController.class).create(null, null)).withSelfRel(),
            null));
  }

  /*
   * updates
   */
  @PostMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable Long id, CitizenRequestEx inputFields,
      BindingResult result) {

    String errorMessage = "";

    if (result.hasErrors()) {
      errorMessage = result.getAllErrors().toString();
    }

    if (errorMessage.length() == 0) {
      // check null
      if (inputFields.getHashCode() == null) {
        errorMessage = "HashCode is null";
      }

      if (errorMessage.length() == 0) {
        // get citizen
        Optional<Citizen> citizen = citizenService.findById(id, inputFields.getHashCode());
        if (citizen.isPresent()) {

          // update block only certain fields
          var needUpdate = false;
          Citizen ci = citizen.get();

          if (inputFields.getDeathDay() != null) {
            ci.getDays().setDeathDay(inputFields.getDeathDay());
            needUpdate = true;
          }

          if (inputFields.getCitizenship() != null) {
            ci.setCitizenship(inputFields.getCitizenship());
            needUpdate = true;
          }

          if (inputFields.getFamilyName() != null) {
            ci.getNames().setFamilyName(inputFields.getFamilyName());
            needUpdate = true;
          }

          if (needUpdate) {
            Citizen updatedCitizen = citizenService.update(ci);
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(updatedCitizen));
          } else {
            errorMessage = "Nothing update";
          }

        } else {
          errorMessage = String.format("Citizen by id=%s not found", id);
        }
      }
    }

    return ResponseEntity
        .badRequest()
        .body(FormTemplateFactory.buildCitizenFormTemplateModel(
            errorMessage,
            linkTo(methodOn(CitizenController.class).update(id, null, null)).withSelfRel(),
            null));
  }

}