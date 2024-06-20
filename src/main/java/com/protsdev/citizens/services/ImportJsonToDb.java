package com.protsdev.citizens.services;

import java.util.Optional;
import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.protsdev.citizens.enums.Gender;
import com.protsdev.citizens.jsons.CitizenJson;
import com.protsdev.citizens.jsons.FullJson;
import com.protsdev.citizens.jsons.MarriageJson;
import com.protsdev.citizens.jsons.ParenthoodJson;
import com.protsdev.citizens.models.Citizen;
import com.protsdev.citizens.models.CitizenName;
import com.protsdev.citizens.models.DateRights;
import com.protsdev.citizens.models.LifeStageDays;
import com.protsdev.citizens.models.Marriage;
import com.protsdev.citizens.models.Parenthood;
import com.protsdev.citizens.repositories.CitizenRepository;
import com.protsdev.citizens.repositories.MarriageRepository;
import com.protsdev.citizens.repositories.ParenthoodRepository;

@Service
public class ImportJsonToDb {
    private JsonLoaderService jsonLoader;
    private CitizenRepository citizenRepository;
    private MarriageRepository marriageRepository;
    private ParenthoodRepository parenthoodRepository;

    public ImportJsonToDb(JsonLoaderService jsonLoader,
            CitizenRepository citizenRepository,
            MarriageRepository marriageRepository,
            ParenthoodRepository parenthoodRepository) {
        this.jsonLoader = jsonLoader;
        this.citizenRepository = citizenRepository;
        this.marriageRepository = marriageRepository;
        this.parenthoodRepository = parenthoodRepository;
    }

    public void run(String fileName) {
        Optional<FullJson> json = jsonLoader.readJsonFromFile(fileName);

        json.ifPresent(js -> {
            persistSimpleCitizens(js.citizens());
            persistCitizensMarriages(js.marriages());
            persistCitizensParenthood(js.parenthoods());
        });
    }

    /*
     * persist simple citizen
     */
    private void persistSimpleCitizens(List<CitizenJson> citizenJsons) {
        citizenJsons.forEach(ci -> {

            Citizen citizenEntity = new Citizen();
            citizenEntity.setNames(convertNames(ci));
            citizenEntity.setDays(convertDays(ci));
            citizenEntity.setGender(ci.gender());
            citizenEntity.setCitizenship(ci.citizenship());

            citizenRepository.save(citizenEntity);
        });
    }

    /*
     * convert Names from domain to entitties
     */
    private CitizenName convertNames(CitizenJson citizen) {
        CitizenName names = new CitizenName();
        names.setFirstName(citizen.firstName());
        names.setFamilyName(citizen.familyName());
        names.setMaidenName("");
        names.setSecondName("");

        return names;
    }

    /*
     * convert LifeStageDays from domain to entitties
     */
    private LifeStageDays convertDays(CitizenJson citizen) {
        LifeStageDays days = new LifeStageDays();

        days.setBirthDay(Date.valueOf(citizen.birthDay()));

        return days;
    }

    /*
     * add marriages to citizens
     */
    private void persistCitizensMarriages(List<MarriageJson> marriages) {

        marriages.forEach(ma -> {

            // persisted citizens
            List<Citizen> citizens = citizenRepository.findAll();
            // find target citizen entity
            Optional<Citizen> citizenOptionalHusband = citizens.stream()
                    .filter(ci -> filterCitizen(ma.husband(), ci))
                    .findFirst();
            Optional<Citizen> citizenOptionalWife = citizens.stream().filter(ci -> filterCitizen(ma.wife(), ci))
                    .findFirst();

            if (citizenOptionalHusband.isPresent() && citizenOptionalWife.isPresent()) {

                Citizen citizenA = citizenOptionalHusband.get();
                Citizen citizenB = citizenOptionalWife.get();

                Marriage marriageA = new Marriage();
                marriageA.setCitizen(citizenA);
                marriageA.setPartner(citizenB);
                marriageA.setDateRights(convertDateRights(ma.dateOnRights()));

                marriageRepository.save(marriageA);

                Marriage marriageB = new Marriage();
                marriageB.setCitizen(citizenB);
                marriageB.setPartner(citizenA);
                marriageB.setDateRights(convertDateRights(ma.dateOnRights()));

                marriageRepository.save(marriageB);
                takePartnersFamilyName(citizenB, citizenA.getNames().getFamilyName());
                citizenRepository.save(citizenB);
            }

        });
    }

    private boolean filterCitizen(String citizenString, Citizen citizen) {
        String[] argsFind = citizenString.split(";");

        if (argsFind.length != 4) {
            return false;
        }

        return citizen.getNames().getFamilyName().equals(argsFind[0])
                && citizen.getNames().getFirstName().equals(argsFind[1])
                && citizen.getDays().getBirthDay().equals(Date.valueOf(argsFind[2]))
                && Gender.valueOf(argsFind[3]).equals(citizen.getGender());
    }

    /*
     * convert DateRights from domain to entitties
     */
    private DateRights convertDateRights(String date) {
        DateRights dre = new DateRights();
        dre.setStartDay(Date.valueOf(date));
        return dre;
    }

    /*
     * change family name with persisting maiden name
     */
    public static void takePartnersFamilyName(Citizen citizen, String newFamilyName) {
        if (citizen.getNames().getMaidenName().isEmpty()) {
            citizen.getNames().setMaidenName(citizen.getNames().getFamilyName());
        }
        citizen.getNames().setFamilyName(newFamilyName);
    }

    private void persistCitizensParenthood(List<ParenthoodJson> parenthoodJsons) {
        // persisted citizens
        List<Citizen> citizens = citizenRepository.findAll();

        parenthoodJsons.forEach(pa -> {
            // find target citizen entity
            Optional<Citizen> citizenOptional = citizens.stream()
                    .filter(ci -> filterCitizen(pa.citizen(), ci))
                    .findFirst();
            Optional<Citizen> citizenChildOptional = citizens.stream().filter(ci -> filterCitizen(pa.child(), ci))
                    .findFirst();

            if (citizenOptional.isPresent() && citizenChildOptional.isPresent()) {

                Citizen citizen = citizenOptional.get();
                Citizen child = citizenChildOptional.get();

                Parenthood parenthood = new Parenthood();
                parenthood.setCitizen(citizen);
                parenthood.setChild(child);
                parenthood.setDateRights(convertDateRights(pa.dateOnRights()));
                parenthood.setType(pa.parenthood());

                parenthoodRepository.save(parenthood);
            }
        });
    }
}