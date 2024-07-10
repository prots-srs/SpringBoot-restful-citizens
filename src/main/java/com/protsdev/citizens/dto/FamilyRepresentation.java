package com.protsdev.citizens.dto;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FamilyRepresentation extends RepresentationModel<FamilyRepresentation> {

  private final FamilyNuclear family;

  public FamilyNuclear getFamily() {
    return family;
  }

  @JsonCreator
  public FamilyRepresentation(@JsonProperty("family") FamilyNuclear family) {
    this.family = family;
  }

}
