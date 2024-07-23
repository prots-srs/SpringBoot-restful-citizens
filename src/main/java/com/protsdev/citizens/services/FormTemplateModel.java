package com.protsdev.citizens.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import com.protsdev.citizens.dto.FormTemplate;
import com.protsdev.citizens.enums.Citizenship;
import com.protsdev.citizens.enums.Gender;

@Service
public class FormTemplateModel {

    /*
     * citizen form model
     */
    public RepresentationModel<?> getCitizenFormTemplateModel(String errorMessage, Link selfLink) {
        return HalModelBuilder
                .emptyHalModel()
                .embed(getCitizenFormTemplate(errorMessage))
                .link(selfLink)
                .build();
    }

    /*
     * store sitizen model
     */
    public RepresentationModel<?> getCitizenStoreFormTemplateModel(String errorMessage, Link selfLink) {
        return HalModelBuilder
                .emptyHalModel()
                .embed(getCitizenUpdateFormTemplate(errorMessage))
                .link(selfLink)
                .build();
    }

    /*
     * 
     */
    public RepresentationModel<?> getMarriageFormTemplateModel(String errorMessage, Link self, Link root) {
        return HalModelBuilder
                .emptyHalModel()
                .embed(getMarriageFormTemplate(errorMessage))
                .link(self)
                .link(root)
                .build();
    }

    /*
     * citizen form request
     */
    private FormTemplate getCitizenFormTemplate(String errorMessage) {
        if (errorMessage == null) {
            errorMessage = "";
        }

        return new FormTemplate.Builder("Input citizen personal data for request")
                .setMethod(RequestMethod.GET)
                .setError(errorMessage)
                .setContentType("application/json|multipart/form-data")
                // to search
                .addProperty(getTextField("firstName", "text", "true"))
                .addProperty(getTextField("familyName", "text", "true"))
                .addProperty(getTextField("secondName", "text", "false"))
                .addProperty(getTextField("birthDate", "text", "true"))
                .addProperty(getGenderField(null))
                .addProperty(getCitizenshipField("citizenship"))
                .build();
    }

    /*
     * citizen extended form for store or update
     */
    private FormTemplate getCitizenUpdateFormTemplate(String errorMessage) {
        if (errorMessage == null) {
            errorMessage = "";
        }

        return new FormTemplate.Builder("Input citizen personal data for request")
                .setMethod(RequestMethod.POST)
                .setError(errorMessage)
                .setContentType("application/json|multipart/form-data")
                // to search citizen
                .addProperty(getTextField("firstName", "text", "true"))
                .addProperty(getTextField("familyName", "text", "true"))
                .addProperty(getTextField("secondName", "text", "false"))
                .addProperty(getTextField("birthDate", "text", "true"))
                .addProperty(getGenderField(null))
                .addProperty(getCitizenshipField("citizenship"))
                // to update
                .addProperty(getTextField("deathDate", "text", "false"))
                .addProperty(getTextField("avatar", "file", "false"))
                .addProperty(getTextField("newFamilyName", "text", "false"))
                .addProperty(getCitizenshipField("newCitizenship"))
                .build();
    }

    private FormTemplate getMarriageFormTemplate(String errorMessage) {
        if (errorMessage == null) {
            errorMessage = "";
        }

        return new FormTemplate.Builder("Input citizens for marriage")
                .setMethod(RequestMethod.POST)
                .setError(errorMessage)
                .setContentType("application/json|multipart/form-data")
                // to search citizen A (Husband)
                .addProperty(getTextField("firstNameA", "text", "true"))
                .addProperty(getTextField("familyNameA", "text", "true"))
                .addProperty(getTextField("secondNameA", "text", "false"))
                .addProperty(getTextField("birthDateA", "text", "true"))
                .addProperty(getGenderField("genderA"))
                .addProperty(getCitizenshipField("citizenshipA"))
                // citizen B (Wife)
                .addProperty(getTextField("firstNameB", "text", "true"))
                .addProperty(getTextField("familyNameB", "text", "true"))
                .addProperty(getTextField("secondNameB", "text", "false"))
                .addProperty(getTextField("birthDateB", "text", "true"))
                .addProperty(getGenderField("genderB"))
                .addProperty(getCitizenshipField("citizenshipB"))
                // date
                .addProperty(getTextField("dateOfEvent", "text", "false"))
                .addProperty(getTextField("giveBFamilyNameA", "checkbox", "false"))
                .build();
    }

    /*
     * fields on all case of life
     */
    private Map<String, String> getTextField(String fieldName, String type, String required) {
        var param = new HashMap<String, String>();
        param.put("name", fieldName != null ? fieldName : "");
        param.put("type", type != null ? type : "text");
        param.put("required", required != null ? required : "false");

        return param;
    }

    private Map<String, String> getGenderField(String fieldName) {
        var param = getTextField(fieldName != null ? fieldName : "gender", "select", "true");
        var genders = Arrays.stream(Gender.values()).map(g -> g.toString()).toList();
        param.put("options", String.join(",", genders));
        return param;
    }

    private Map<String, String> getCitizenshipField(String fieldName) {
        var param = getTextField(fieldName != null ? fieldName : "citizenship", "select", "true");
        var chitizenships = Arrays.stream(Citizenship.values()).map(g -> g.toString()).toList();
        param.put("options", String.join(",", chitizenships));
        return param;
    }

}
