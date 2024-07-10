package com.protsdev.citizens.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

/**
 * Representation response for marriage.
 */
@Getter
public class MarriageRepresentation extends RepresentationModel<MarriageRepresentation> {

  private final List<CitizenView> partners;
  private final LocalDate startDate;
  private final LocalDate endDate;

  @JsonCreator
  public MarriageRepresentation(
      @JsonProperty("partners") List<CitizenView> partners,
      @JsonProperty("startDate") LocalDate startDate,
      @JsonProperty("endDate") LocalDate endDate) {
    this.partners = partners;
    this.startDate = startDate;
    this.endDate = endDate;
  }
}
