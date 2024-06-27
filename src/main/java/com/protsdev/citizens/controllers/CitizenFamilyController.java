package com.protsdev.citizens.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
// import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.protsdev.citizens.domain.CitizenFamilyService;
import com.protsdev.citizens.domain.CitizenService;
import com.protsdev.citizens.dto.CitizenFamilyRepresentation;
import com.protsdev.citizens.dto.CitizenRepresentation;
import com.protsdev.citizens.dto.FamilyExtended;
import com.protsdev.citizens.dto.FamilyNuclear;
import com.protsdev.citizens.models.Citizen;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/family")
public class CitizenFamilyController {

	@Autowired
	CitizenService citizenService;

	@Autowired
	CitizenFamilyService citizenRelations;

	@GetMapping
	public ResponseEntity<?> index() {
		var model = HalModelBuilder
				.emptyHalModel()
				.link(linkTo(methodOn(CitizenFamilyController.class).index())
						.withSelfRel())
				.link(linkTo(methodOn(CitizenFamilyController.class).nuclear(null, null))
						.withRel("nuclear"))
				.link(linkTo(methodOn(CitizenFamilyController.class).extended(null,
						null)).withRel("extended"))
				.build();

		return ResponseEntity.ok(model);
	}

	@GetMapping("/nuclear")
	public ResponseEntity<?> nuclear(
			CitizenRepresentation inputFields,
			BindingResult result) {

		Link selfLink = linkTo(methodOn(CitizenFamilyController.class).nuclear(null, null)).withSelfRel();
		Link rootLink = linkTo(methodOn(CitizenFamilyController.class).index()).withRel("index");

		// check binding errors
		if (result.hasErrors()) {
			return ResponseEntity.badRequest()
					.body(FormTemplateFactory.getCitizenFormTemplateModel(
							result.getAllErrors().toString(), selfLink, rootLink));
		}

		// check nullable fields
		if (inputFields.getNullFields().length() > 0) {
			return ResponseEntity
					.badRequest()
					.body(FormTemplateFactory.getCitizenFormTemplateModel(
							String.format("Fields '%s' have null value",
									inputFields.getNullFields()),
							selfLink, rootLink));
		}

		Optional<Citizen> citizen = citizenService.fetchCitizen(inputFields);
		if (citizen.isPresent()) {
			FamilyNuclear family = citizenRelations.getNuclearFamily(citizen.get());

			var citizenFamily = new CitizenFamilyRepresentation(family);
			citizenFamily.add(selfLink);
			citizenFamily.add(rootLink);

			return ResponseEntity.ok(citizenFamily);
		}

		return ResponseEntity
				.badRequest()
				.body(FormTemplateFactory.getCitizenFormTemplateModel(
						String.format("Citizen by %s %s, birthDay at %s, has gender %s and citizenship %s not found",
								inputFields.getFirstName(),
								inputFields.getFamilyName(),
								inputFields.getBirthDay(),
								inputFields.getGender(),
								inputFields.getCitizenship()),
						selfLink, rootLink));
	}

	@GetMapping("/extended")
	public ResponseEntity<?> extended(
			CitizenRepresentation inputFields,
			BindingResult result) {

		Link selfLink = linkTo(methodOn(CitizenFamilyController.class).extended(null, null)).withSelfRel();
		Link rootLink = linkTo(methodOn(CitizenFamilyController.class).index()).withRel("index");

		// check binding errors
		if (result.hasErrors()) {
			return ResponseEntity.badRequest()
					.body(FormTemplateFactory.getCitizenFormTemplateModel(
							result.getAllErrors().toString(), selfLink, rootLink));
		}

		// check nullable fields
		if (inputFields.getNullFields().length() > 0) {
			return ResponseEntity
					.badRequest()
					.body(FormTemplateFactory.getCitizenFormTemplateModel(
							String.format("Fields '%s' have null value",
									inputFields.getNullFields()),
							selfLink, rootLink));
		}

		Optional<Citizen> citizen = citizenService.fetchCitizen(inputFields);
		if (citizen.isPresent()) {

			FamilyExtended family = citizenRelations.getExtendedFamily(citizen.get());
			var citizenFamily = new CitizenFamilyRepresentation(family);
			citizenFamily.add(selfLink);
			citizenFamily.add(rootLink);

			return ResponseEntity.ok(citizenFamily);
		}

		return ResponseEntity
				.badRequest()
				.body(FormTemplateFactory.getCitizenFormTemplateModel(
						String.format(
								"Citizen by %s %s, birthDay at %s, has gender %s and citizenship %s not found",
								inputFields.getFirstName(),
								inputFields.getFamilyName(),
								inputFields.getBirthDay(),
								inputFields.getGender(),
								inputFields.getCitizenship()),
						selfLink, rootLink));
	}

}
