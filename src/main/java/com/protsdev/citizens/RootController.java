package com.protsdev.citizens;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.protsdev.citizens.controllers.CitizenController;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api")
public class RootController {

    // @GetMapping
    // RepresentationModel<?> index() {

    // RepresentationModel<?> rootModel = new RepresentationModel<>();
    // rootModel.add(linkTo(methodOn(CitizenController.class).getNuclearFamily(null,
    // null)).withRel("family"));
    // return rootModel;
    // }

}
