package com.protsdev.citizens.controllers;

import java.util.Arrays;
import java.util.HashMap;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.web.bind.annotation.RequestMethod;

import com.protsdev.citizens.dto.FormTemplate;
import com.protsdev.citizens.enums.Citizenship;
import com.protsdev.citizens.enums.Gender;

public class FormTemplateFactory {

  public static RepresentationModel<?> buildCitizenFormTemplateModel(String errorMessage, Link selfLink,
      Link rootLink) {
    if (rootLink == null) {
      return HalModelBuilder
          .emptyHalModel()
          .embed(getCitizenFormTemplate(errorMessage))
          .link(selfLink)
          .build();
    } else {
      return HalModelBuilder
          .emptyHalModel()
          .embed(getCitizenFormTemplate(errorMessage))
          .link(selfLink)
          .link(rootLink)
          .build();
    }
  }

  public static RepresentationModel<?> buildCitizenUpdateFormTemplateModel(String errorMessage, Link self) {
    return HalModelBuilder
        .emptyHalModel()
        .embed(getCitizenUpdateFormTemplate(errorMessage))
        .link(self)
        .build();
  }

  private static FormTemplate getCitizenFormTemplate(String errorMessage) {
    if (errorMessage == null) {
      errorMessage = "";
    }

    var paramName = new HashMap<String, String>();
    paramName.put("name", "firstName");
    paramName.put("required", "true");

    var paramNameF = new HashMap<String, String>();
    paramNameF.put("name", "familyName");
    paramNameF.put("required", "true");

    var paramBirthDay = new HashMap<String, String>();
    paramBirthDay.put("name", "birthDay");
    paramBirthDay.put("required", "true");

    var paramGender = new HashMap<String, String>();
    paramGender.put("name", "gender");
    paramGender.put("required", "true");
    var genders = Arrays.stream(Gender.values()).map(g -> g.toString()).toList();
    paramGender.put("options", String.join(",", genders));

    var paramCitizenship = new HashMap<String, String>();
    paramCitizenship.put("name", "citizenship");
    paramCitizenship.put("required", "true");
    var chitizenships = Arrays.stream(Citizenship.values()).map(g -> g.toString()).toList();
    paramCitizenship.put("options", String.join(",", chitizenships));

    return new FormTemplate.Builder("Input citizen personal data for search")
        .setMethod(RequestMethod.GET)
        .setError(errorMessage)
        .setContentType("application/json|multipart/form-data")
        .addProperty(paramName)
        .addProperty(paramNameF)
        .addProperty(paramBirthDay)
        .addProperty(paramGender)
        .addProperty(paramCitizenship)
        .build();
  }

  private static FormTemplate getCitizenUpdateFormTemplate(String errorMessage) {
    if (errorMessage == null) {
      errorMessage = "";
    }

    var paramHashCode = new HashMap<String, String>();
    paramHashCode.put("name", "hashCode");
    paramHashCode.put("required", "true");

    var paramNameF = new HashMap<String, String>();
    paramNameF.put("name", "familyName");
    paramNameF.put("required", "false");

    var paramDeathDay = new HashMap<String, String>();
    paramDeathDay.put("name", "deathDay");
    paramDeathDay.put("required", "false");

    var paramCitizenship = new HashMap<String, String>();
    paramCitizenship.put("name", "citizenship");
    paramCitizenship.put("required", "false");
    var chitizenships = Arrays.stream(Citizenship.values()).map(g -> g.toString()).toList();
    paramCitizenship.put("options", String.join(",", chitizenships));

    return new FormTemplate.Builder("Input citizen personal data for search")
        .setMethod(RequestMethod.POST)
        .setError(errorMessage)
        .setContentType("application/json|multipart/form-data")
        .addProperty(paramHashCode)
        .addProperty(paramNameF)
        .addProperty(paramDeathDay)
        .addProperty(paramCitizenship)
        .build();
  }

  public static RepresentationModel<?> buildMarriageFormTemplateModel(String errorMessage, Link self, Link root) {
    return HalModelBuilder
        .emptyHalModel()
        .embed(getMarriageFormTemplate(errorMessage))
        .link(self)
        .link(root)
        .build();
  }

  private static FormTemplate getMarriageFormTemplate(String errorMessage) {
    if (errorMessage == null) {
      errorMessage = "";
    }

    var idA = new HashMap<String, String>();
    idA.put("name", "idCitizenA");
    idA.put("required", "true");

    var hashCodeA = new HashMap<String, String>();
    hashCodeA.put("name", "hashCodeCitizenA");
    hashCodeA.put("required", "true");

    var idB = new HashMap<String, String>();
    idB.put("name", "idCitizenB");
    idB.put("required", "true");

    var hashCodeB = new HashMap<String, String>();
    hashCodeB.put("name", "hashCodeCitizenB");
    hashCodeB.put("required", "true");

    var date = new HashMap<String, String>();
    date.put("name", "dateOfEvent");
    date.put("required", "true");

    return new FormTemplate.Builder("Input citizens for marriage")
        .setMethod(RequestMethod.POST)
        .setError(errorMessage)
        .setContentType("application/json|multipart/form-data")
        .addProperty(idA)
        .addProperty(hashCodeA)
        .addProperty(idB)
        .addProperty(hashCodeB)
        .addProperty(date)
        .build();
  }
}
