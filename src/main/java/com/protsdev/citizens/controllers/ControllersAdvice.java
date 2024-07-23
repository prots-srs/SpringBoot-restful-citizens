package com.protsdev.citizens.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.protsdev.citizens.services.FormTemplateModel;
import com.protsdev.citizens.storage.StorageFileNotFoundException;

@RestControllerAdvice
public class ControllersAdvice {

    @Autowired
    private FormTemplateModel formTemplateFactory;

    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {

        // Map<String, String> errors = new HashMap<>();

        // ex.getBindingResult().getAllErrors().forEach((error) -> {
        // String fieldName = ((FieldError) error).getField();
        // String errorMessage = error.getDefaultMessage();
        // errors.put(fieldName, errorMessage);

        // System.out.println("-->> fieldName: " + fieldName);
        // System.out.println("-->> errorMessage: " + errorMessage);
        // });

        // return errors;
        return ResponseEntity
                .badRequest()
                .body(formTemplateFactory.getCitizenStoreFormTemplateModel(
                        ex.getBindingResult().getAllErrors().toString(),
                        linkTo(methodOn(CitizenController.class).index(null, null)).withSelfRel()));
    }

    /*
     * 
     */
    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        // System.out.println("-->> StorageFileNotFoundException: " + exc.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MultipartException.class)
    public Map<String, String> handleMultipartException(MultipartException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("file", ex.getMessage() + " ...");
        // System.out.println("-->> MultipartException: " + ex.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoResourceFoundException.class)
    public Map<String, String> handleNoResourceFoundException(NoResourceFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("file", ex.getMessage() + " ...");

        // System.out.println("-->> NoResourceFoundException: " + ex.getMessage());
        return errors;
    }
}
