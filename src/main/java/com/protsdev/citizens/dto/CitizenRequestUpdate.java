package com.protsdev.citizens.dto;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import com.protsdev.citizens.enums.Citizenship;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class CitizenRequestUpdate extends CitizenRequest {
    // store
    private LocalDate deathDate;
    private MultipartFile avatar;
    // update
    private String newFamilyName;
    private Citizenship newCitizenship;
}
