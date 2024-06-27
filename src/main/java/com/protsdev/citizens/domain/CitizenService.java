package com.protsdev.citizens.domain;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.protsdev.citizens.dto.CitizenRepresentation;
import com.protsdev.citizens.models.Citizen;
import com.protsdev.citizens.repositories.CitizenRepository;

@Service
public class CitizenService {

    private CitizenRepository citizenRepository;

    CitizenService(CitizenRepository citizenRepository) {
        this.citizenRepository = citizenRepository;
    }

    /*
     * obtain citizen
     * 
     * names: first, family
     * days: birth
     * gender
     * citizenship
     */
    public Optional<Citizen> fetchCitizen(CitizenRepresentation citizenResponse) {
        Optional<Citizen> citizen = Optional.empty();

        List<Citizen> citizens = citizenRepository
                .findByNamesFamilyNameAndNamesFirstNameAndDays_BirthDayAndGenderAndCitizenship(
                        citizenResponse.getFamilyName(),
                        citizenResponse.getFirstName(),
                        // Date.valueOf(citizenResponse.getBirthDay()),
                        citizenResponse.getBirthDay(),
                        citizenResponse.getGender(),
                        citizenResponse.getCitizenship());

        if (citizens.size() > 0) {
            citizen = Optional.of(citizens.get(0));
        }

        return citizen;
    }

}
