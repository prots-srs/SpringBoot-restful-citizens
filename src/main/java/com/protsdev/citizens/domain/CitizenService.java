package com.protsdev.citizens.domain;

import com.protsdev.citizens.dto.CitizenRequest;
import com.protsdev.citizens.dto.CitizenRequestUpdate;
import com.protsdev.citizens.enums.Citizenship;
import com.protsdev.citizens.enums.Gender;
import com.protsdev.citizens.models.Citizen;
import com.protsdev.citizens.models.CitizenName;
import com.protsdev.citizens.models.LifeStageDays;
import com.protsdev.citizens.repositories.CitizenRepository;
import com.protsdev.citizens.storage.StorageService;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

/**
 * Citizen service.
 * obtain citizen:
 * -by required fields,
 * -by id+hashCode
 * create
 * update (proxy to repository)
 * checkers:
 * -adult
 */
@Service
public class CitizenService {
    private Map<String, String> messages = new HashMap<>();

    public static final Integer ADULT_AGE_FROM = 18;

    private CitizenRepository citizenRepository;
    private StorageService storageService;

    private String lastError = "";

    public String getLastError() {
        return lastError;
    }

    CitizenService(CitizenRepository citizenRepository,
            StorageService storageService) {
        this.citizenRepository = citizenRepository;
        this.storageService = storageService;

        messages.put("error_persisting", "Error persisting citizen");
        messages.put("error_missing", "Citizen by %s %s, birthDay at %s, has gender %s and citizenship %s is missing");
    }

    /**
     * obtain citizen.
     * error in lastError
     * wrong find
     */
    public Optional<Citizen> fetchByExample(CitizenRequest iFs) {

        lastError = "";
        Optional<Citizen> citizen = Optional.empty();

        Citizen citizenEntity = new Citizen();
        citizenEntity.setNames(getNames(iFs.getFirstName(), iFs.getFamilyName(), null));
        citizenEntity.setDays(getLifeDays(iFs.getBirthDate()));
        citizenEntity.setGender(iFs.getGender());
        citizenEntity.setCitizenship(iFs.getCitizenship());

        Example<Citizen> exCi = Example.of(citizenEntity);

        citizen = citizenRepository.findOne(exCi);

        if (!citizen.isPresent()) {
            lastError = String.format(
                    messages.get("error_missing"),
                    iFs.getFirstName(),
                    iFs.getFamilyName(),
                    iFs.getBirthDate(),
                    iFs.getGender(),
                    iFs.getCitizenship());
        }

        return citizen;
    }

    public Optional<Citizen> fetch(CitizenRequest iFs) {
        lastError = "";
        Optional<Citizen> citizen = Optional.empty();

        List<Citizen> citizensPersisted = citizenRepository
                .findByNamesFamilyNameAndNamesFirstNameAndDays_BirthDayAndGenderAndCitizenship(
                        iFs.getFamilyName(),
                        iFs.getFirstName(),
                        iFs.getBirthDate(),
                        iFs.getGender(),
                        iFs.getCitizenship());

        if (citizensPersisted.size() > 0) {
            citizen = Optional.of(citizensPersisted.get(0));
        } else {
            lastError = String.format(
                    messages.get("error_missing"),
                    iFs.getFirstName(),
                    iFs.getFamilyName(),
                    iFs.getBirthDate(),
                    iFs.getGender(),
                    iFs.getCitizenship());
        }

        return citizen;
    }

    /**
     * store citizen
     */
    public Optional<Citizen> store(CitizenRequestUpdate iFs) {

        lastError = "";
        Optional<Citizen> citizen = fetch(iFs);

        if (citizen.isPresent()) {
            // update block only certain fields
            var needUpdate = false;
            Citizen ci = citizen.get();

            if (iFs.getDeathDate() != null) {
                ci.getDays().setDeathDay(iFs.getDeathDate());
                needUpdate = true;
            }

            if (iFs.getNewCitizenship() != null) {
                ci.setCitizenship(iFs.getNewCitizenship());
                needUpdate = true;
            }

            if (iFs.getNewFamilyName() != null) {
                ci.getNames().setFamilyName(iFs.getNewFamilyName());
                needUpdate = true;
            }

            if (iFs.getAvatar() != null) {
                var fileSaved = storageService.store(iFs.getAvatar());
                if (fileSaved != null) {
                    ci.setAvatar(fileSaved);
                    needUpdate = true;
                }
            }

            if (needUpdate) {
                citizen = Optional.of(citizenRepository.save(ci));
            }

        } else {
            // create
            Citizen citizenEntity = new Citizen();
            citizenEntity.setNames(getNames(iFs.getFirstName(), iFs.getFamilyName(), iFs.getSecondName()));
            citizenEntity.setDays(getLifeDays(iFs.getBirthDate()));
            citizenEntity.setGender(iFs.getGender());
            citizenEntity.setCitizenship(iFs.getCitizenship());

            citizen = Optional.of(citizenRepository.save(citizenEntity));

            if (!citizen.isPresent()) {
                lastError = messages.get("error_persisting");
            }
        }

        return citizen;
    }

    /**
     * Check for adult citizen
     */
    public boolean isAdult(Citizen citizen) {
        LocalDate ldate = citizen.getDays().getBirthDay();
        Integer years = Period.between(ldate, LocalDate.now()).getYears();

        return years >= ADULT_AGE_FROM;
    }

    /**
     * change family name with persisting maiden name
     */
    public void setMaidenName(Citizen citizen, String newFamilyName) {
        if (citizen.getNames().getMaidenName().isEmpty()) {
            citizen.getNames().setMaidenName(citizen.getNames().getFamilyName());
        }
        citizen.getNames().setFamilyName(newFamilyName);
    }

    /**
     * update current citizen for simple use
     */
    public Citizen update(Citizen citizen) {
        return citizenRepository.save(citizen);
    }

    private CitizenName getNames(String fN, String faN, String sN) {
        CitizenName names = new CitizenName();
        names.setFirstName(fN);
        names.setFamilyName(faN);
        names.setMaidenName("");
        if (sN != null) {
            names.setSecondName(sN);
        } else {
            names.setSecondName("");
        }

        return names;
    }

    private LifeStageDays getLifeDays(LocalDate birthDay) {
        LifeStageDays days = new LifeStageDays();
        days.setBirthDay(birthDay);

        return days;
    }

}
