package com.protsdev.citizens.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;

import com.protsdev.citizens.domain.MarriageService;
import com.protsdev.citizens.dto.MarriageRequest;
import com.protsdev.citizens.models.Marriage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/marriage")
public class MarriageController {

  @Autowired
  private MarriageService marriageService;

  @Autowired
  private MarriageModelAssembler assembler;

  @GetMapping
  public ResponseEntity<?> index() {

    var model = HalModelBuilder
        .emptyHalModel()
        .link(linkTo(methodOn(MarriageController.class).index()).withSelfRel())
        .link(linkTo(methodOn(MarriageController.class).create(null, null)).withRel("create"))
        .link(linkTo(methodOn(MarriageController.class).dissolution(null, null)).withRel("dissolution"))
        .build();

    return ResponseEntity.ok(model);
  }

  @PostMapping("/create")
  public ResponseEntity<?> create(MarriageRequest inputFields, BindingResult bindingResult) {

    String errorMessage = "";
    if (bindingResult.hasErrors()) {
      errorMessage = bindingResult.getAllErrors().toString();
    } else {
      Optional<Marriage> marriageOp = marriageService.create(inputFields);
      if (marriageOp.isPresent()) {
        return ResponseEntity.ok(
            assembler.toModel(marriageOp.get())
                .add(linkTo(methodOn(MarriageController.class).create(null, null)).withSelfRel()));
      } else {
        errorMessage = marriageService.getErrorMessages();
      }
    }

    return ResponseEntity
        .badRequest()
        .body(FormTemplateFactory.buildMarriageFormTemplateModel(
            errorMessage,
            linkTo(methodOn(MarriageController.class).create(null, null)).withSelfRel(),
            linkTo(methodOn(MarriageController.class).index()).withRel("index")));

  }

  @PostMapping("/dissolution")
  public ResponseEntity<?> dissolution(MarriageRequest inputFields, BindingResult bindingResult) {

    String errorMessage = "";
    if (bindingResult.hasErrors()) {
      errorMessage = bindingResult.getAllErrors().toString();
    } else {
      Optional<Marriage> marriageOp = marriageService.dissolution(inputFields);
      if (marriageOp.isPresent()) {
        return ResponseEntity.ok(
            assembler.toModel(marriageOp.get())
                .add(linkTo(methodOn(MarriageController.class).dissolution(null, null)).withSelfRel()));
      } else {
        errorMessage = marriageService.getErrorMessages();
      }
    }

    return ResponseEntity
        .badRequest()
        .body(FormTemplateFactory.buildMarriageFormTemplateModel(
            errorMessage,
            linkTo(methodOn(MarriageController.class).dissolution(null, null)).withSelfRel(),
            linkTo(methodOn(MarriageController.class).index()).withRel("index")));
  }

}
