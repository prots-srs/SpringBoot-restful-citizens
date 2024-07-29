package com.protsdev.citizens.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.protsdev.citizens.domain.ParenthoodService;
import com.protsdev.citizens.dto.ParenthoodRequest;
import com.protsdev.citizens.models.Parenthood;
import com.protsdev.citizens.services.FormTemplateModel;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/parenthood")
public class ParenthoodControllers {

    @Autowired
    private ParenthoodService parenthoodService;

    @Autowired
    private ParenthoodModelAssembler assembler;

    @Autowired
    private FormTemplateModel formTemplateFactory;

    @GetMapping
    public ResponseEntity<?> index() {

        var model = HalModelBuilder
                .emptyHalModel()
                .link(linkTo(methodOn(ParenthoodControllers.class).index()).withSelfRel())
                .link(linkTo(methodOn(ParenthoodControllers.class).giveRights(null,
                        null)).withRel("give"))
                .link(linkTo(methodOn(ParenthoodControllers.class).lostRights(null,
                        null)).withRel("lost"))
                .build();

        return ResponseEntity.ok(model);
    }

    @PostMapping("/give")
    public ResponseEntity<?> giveRights(@Valid @ModelAttribute ParenthoodRequest request, BindingResult bR) {
        String errorMessage = "";
        if (bR.hasErrors()) {
            errorMessage = bR.getAllErrors().toString();
        } else {
            Optional<Parenthood> parenthoodOp = parenthoodService.giveRight(request);
            if (parenthoodOp.isPresent()) {
                return ResponseEntity.ok(
                        assembler.toModel(parenthoodOp.get())
                                .add(linkTo(methodOn(ParenthoodControllers.class).giveRights(null,
                                        null)).withSelfRel()));
            } else {
                errorMessage = parenthoodService.getErrorMessages();
            }
        }

        return ResponseEntity
                .badRequest()
                .body(formTemplateFactory.getParenthoodFormTemplateModel(
                        errorMessage,
                        linkTo(methodOn(ParenthoodControllers.class).giveRights(null, null)).withSelfRel(),
                        linkTo(methodOn(ParenthoodControllers.class).index()).withRel("index")));
    }

    @PostMapping("/lost")
    public ResponseEntity<?> lostRights(@Valid @ModelAttribute ParenthoodRequest request, BindingResult bR) {
        String errorMessage = "";
        if (bR.hasErrors()) {
            errorMessage = bR.getAllErrors().toString();
        } else {
            Optional<Parenthood> parenthoodOp = parenthoodService.lostRight(request);
            if (parenthoodOp.isPresent()) {
                return ResponseEntity.ok(
                        assembler.toModel(parenthoodOp.get())
                                .add(linkTo(methodOn(ParenthoodControllers.class).lostRights(null,
                                        null)).withSelfRel()));
            } else {
                errorMessage = parenthoodService.getErrorMessages();
            }
        }

        return ResponseEntity
                .badRequest()
                .body(formTemplateFactory.getParenthoodFormTemplateModel(
                        errorMessage,
                        linkTo(methodOn(ParenthoodControllers.class).lostRights(null, null)).withSelfRel(),
                        linkTo(methodOn(ParenthoodControllers.class).index()).withRel("index")));
    }

}
