package com.protsdev.citizens.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api")
public class RootController {

    @GetMapping
    public ResponseEntity<?> index() {
        var model = HalModelBuilder
                .emptyHalModel()
                .link(linkTo(methodOn(CitizenFamilyController.class).index()).withRel("family"))
                .build();

        return ResponseEntity.ok(model);
    }
}
