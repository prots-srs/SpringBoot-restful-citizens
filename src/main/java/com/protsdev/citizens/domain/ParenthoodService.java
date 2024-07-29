package com.protsdev.citizens.domain;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.protsdev.citizens.dto.CitizenRequest;
import com.protsdev.citizens.dto.CitizenRequestUpdate;
import com.protsdev.citizens.dto.ParenthoodRequest;
import com.protsdev.citizens.enums.ParenthoodType;
import com.protsdev.citizens.models.Citizen;
import com.protsdev.citizens.models.DateRights;
import com.protsdev.citizens.models.Parenthood;
import com.protsdev.citizens.repositories.ParenthoodRepository;

@Service
public class ParenthoodService {

    private Map<String, String> messages = new HashMap<>();
    private List<String> errorMessages;

    private final ParenthoodRepository parenthoodRepository;
    private final CitizenService citizenService;

    public ParenthoodService(
            ParenthoodRepository parenthoodRepository,
            CitizenService citizenService) {
        this.citizenService = citizenService;
        this.parenthoodRepository = parenthoodRepository;

        messages.put("error_found_child", "Child is found");
        messages.put("error_date_right_wrong", "Date rights is wrong");
        messages.put("error_create", "Error create patenhood");
        messages.put("error_found_parenthood", "Not found patenhood");
    }

    /**
     * Errors checking of partners.
     */
    public String getErrorMessages() {
        if (errorMessages == null) {
            return "";
        }
        return errorMessages.toString();
    }

    /*
     * facade
     */
    public Optional<Parenthood> giveRight(ParenthoodRequest parenthoodRequest) {
        // define error messages
        errorMessages = new LinkedList<>();

        // define empty answer
        Optional<Parenthood> parenthood = Optional.empty();

        // find citizen
        var citizen = findCitizen(parenthoodRequest);
        if (citizen.isPresent() == false) {
            return parenthood;
        }

        // find child
        var child = findChild(parenthoodRequest);
        // if birthmother, create child
        if (parenthoodRequest.getParenthoodType().equals(ParenthoodType.BIRTHMOTHER)) {
            if (child.isPresent()) {
                errorMessages.add(messages.get("error_found_child"));
                return parenthood;
            } else {
                if (parenthoodRequest.getChildBirthDate().compareTo(parenthoodRequest.getDateRights()) != 0) {
                    errorMessages.add(messages.get("error_date_right_wrong"));
                } else {
                    child = createChild(parenthoodRequest);
                }
            }
        }

        if (child.isPresent()) {
            parenthood = createParenthood(citizen.get(), child.get(), parenthoodRequest.getDateRights(),
                    parenthoodRequest.getParenthoodType());

            if (parenthood.isPresent() == false) {
                errorMessages.add(messages.get("error_create"));
            }
        } else {
            errorMessages.add(messages.get("error_found_child"));
        }

        return parenthood;
    }

    /*
     * facade
     */
    public Optional<Parenthood> lostRight(ParenthoodRequest parenthoodRequest) {
        // define error messages
        errorMessages = new LinkedList<>();
        // define empty answer
        Optional<Parenthood> parenthood = Optional.empty();

        // find citizen
        var citizen = findCitizen(parenthoodRequest);
        if (citizen.isPresent() == false) {
            return parenthood;
        }

        // find child
        var child = findChild(parenthoodRequest);
        if (child.isPresent()) {
            // find parenthood
            List<Parenthood> parenthoods = parenthoodRepository.findByCitizenAndChildAndType(citizen.get(), child.get(),
                    parenthoodRequest.getParenthoodType());
            for (Parenthood ph : parenthoods) {
                if (ph.getDateRights().getEndDate() == null) {
                    ph.getDateRights().setEndDate(parenthoodRequest.getDateRights());
                    parenthood = Optional.of(parenthoodRepository.save(ph));
                    break;
                }
            }

            if (parenthood.isPresent() == false) {
                errorMessages.add(messages.get("error_found_parenthood"));
            }
        } else {
            errorMessages.add(messages.get("error_found_child"));
        }

        return parenthood;
    }

    private Optional<Citizen> findCitizen(ParenthoodRequest parenthoodRequest) {
        var cR = new CitizenRequest();
        cR.setFirstName(parenthoodRequest.getFirstName());
        cR.setFamilyName(parenthoodRequest.getFamilyName());
        cR.setBirthDate(parenthoodRequest.getBirthDate());
        cR.setGender(parenthoodRequest.getGender());
        cR.setCitizenship(parenthoodRequest.getCitizenship());

        Optional<Citizen> citizen = citizenService.fetch(cR);
        if (citizen.isPresent() == false) {
            errorMessages.add(citizenService.getLastError());
        }

        return citizen;
    }

    private Optional<Citizen> findChild(ParenthoodRequest parenthoodRequest) {
        var cR = new CitizenRequest();
        cR.setFirstName(parenthoodRequest.getChildFirstName());
        cR.setFamilyName(parenthoodRequest.getChildFamilyName());
        cR.setBirthDate(parenthoodRequest.getChildBirthDate());
        cR.setGender(parenthoodRequest.getChildGender());
        cR.setCitizenship(parenthoodRequest.getChildCitizenship());

        return citizenService.fetch(cR);
    }

    private Optional<Citizen> createChild(ParenthoodRequest parenthoodRequest) {
        var cR = new CitizenRequestUpdate();
        cR.setFirstName(parenthoodRequest.getChildFirstName());
        cR.setFamilyName(parenthoodRequest.getChildFamilyName());
        cR.setBirthDate(parenthoodRequest.getChildBirthDate());
        cR.setGender(parenthoodRequest.getChildGender());
        cR.setCitizenship(parenthoodRequest.getChildCitizenship());

        return citizenService.store(cR);
    }

    private Optional<Parenthood> createParenthood(Citizen citizen, Citizen child, LocalDate dateRights,
            ParenthoodType type) {
        Parenthood parenthood = new Parenthood();
        parenthood.setCitizen(citizen);
        parenthood.setChild(child);

        DateRights dre = new DateRights();
        dre.setStartDate(dateRights);

        parenthood.setDateRights(dre);
        parenthood.setType(type);

        return Optional.of(parenthoodRepository.save(parenthood));
    }

}
