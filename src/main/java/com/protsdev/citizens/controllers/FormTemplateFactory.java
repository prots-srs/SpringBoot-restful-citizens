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

  private static FormTemplate getCitizenFormTemplate(String errorMessage) {

    var paramName = new HashMap<String, String>();
    paramName.put("name", "firstName");
    paramName.put("required", "true");

    var paramNameF = new HashMap<String, String>();
    paramNameF.put("name", "familyName");
    paramNameF.put("required", "true");

    var paramBirthDay = new HashMap<String, String>();
    paramBirthDay.put("name", "birthDay");
    paramBirthDay.put("required", "true");

    var paramDeathDay = new HashMap<String, String>();
    paramDeathDay.put("name", "deathDay");
    paramDeathDay.put("required", "false");

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

    var template = new FormTemplate.Builder("Input citizen personal data for search")
        .setMethod(RequestMethod.GET)
        .setError(errorMessage)
        .setContentType("application/json|multipart/form-data")
        .addProperty(paramName)
        .addProperty(paramNameF)
        .addProperty(paramBirthDay)
        .addProperty(paramDeathDay)
        .addProperty(paramGender)
        .addProperty(paramCitizenship)
        .build();

    return template;
  }
}
