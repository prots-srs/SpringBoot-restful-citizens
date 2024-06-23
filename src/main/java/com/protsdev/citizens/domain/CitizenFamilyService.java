package com.protsdev.citizens.domain;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.protsdev.citizens.dto.CitizenView;
import com.protsdev.citizens.dto.ExtendedFamily;
import com.protsdev.citizens.dto.FieldsDefiningCitizen;
import com.protsdev.citizens.dto.NuclearFamily;
import com.protsdev.citizens.dto.NuclearFamilyAdult;
import com.protsdev.citizens.dto.NuclearFamilyChild;
import com.protsdev.citizens.enums.Citizenship;
import com.protsdev.citizens.enums.Gender;
import com.protsdev.citizens.enums.TypeParenthood;
import com.protsdev.citizens.models.Citizen;
import com.protsdev.citizens.models.Parenthood;
import com.protsdev.citizens.repositories.CitizenRepository;
import com.protsdev.citizens.repositories.ParenthoodRepository;
import com.protsdev.citizens.services.VerifyFields;

@Service
public class CitizenFamilyService {
    private static final Integer ADULT_AGE_FROM = 18;

    private VerifyFields verifyService;
    private CitizenRepository citizenRepository;
    private ParenthoodRepository parenthoodRepository;

    public CitizenFamilyService(
            ParenthoodRepository parenthoodRepository,
            CitizenRepository citizenRepository,
            VerifyFields verifyService) {
        this.parenthoodRepository = parenthoodRepository;
        this.citizenRepository = citizenRepository;
        this.verifyService = verifyService;
    }

    private FieldsDefiningCitizen inputFields;
    private Date citizenBirthDay;
    private Gender citizenGender;
    private Citizenship citizenCitizenship;
    private Citizen targetCitizen;

    /*
     * fetch nuclear family by strings
     * names: first, family
     * days: birth
     * gender
     * citizenship
     */
    public Optional<NuclearFamily> getNuclearFamily(FieldsDefiningCitizen inputFields) {

        if (!defineInputFields(inputFields)) {
            return Optional.empty();
        }
        if (!defineCitizen()) {
            return Optional.empty();
        }

        // get output objects
        if (isAdult()) {
            return Optional.of(new NuclearFamilyAdult(
                    getCitizenView(targetCitizen),
                    getCurrentPartner(),
                    getChildrenByRights()));
        } else {
            List<Citizen> birthParents = getBirthParents();

            return Optional.of(new NuclearFamilyChild(
                    getCitizenView(targetCitizen),
                    getBirthParentsView(birthParents),
                    getAdopters(),
                    getSiblings(birthParents)));
        }
    }

    public Optional<NuclearFamily> getExtendedFamily(FieldsDefiningCitizen inputFields) {
        if (!defineInputFields(inputFields)) {
            return Optional.empty();
        }
        if (!defineCitizen()) {
            return Optional.empty();
        }

        List<Citizen> birthParents = getBirthParents();

        return Optional.of(new ExtendedFamily(
                getCitizenView(targetCitizen),
                getCurrentPartner(),
                getChildrenByRights(),
                getBirthParentsView(birthParents),
                getAdopters(),
                getSiblings(birthParents)));
    }

    /*
     * check INPUTs
     */
    private boolean defineInputFields(FieldsDefiningCitizen inputFields) {

        Optional<Date> birthDay = verifyService.getDateByString(inputFields.birthDay());
        Optional<Gender> gender = verifyService.getGenderByString(inputFields.gender());
        Optional<Citizenship> citizenship = verifyService.getCitizenshipByString(inputFields.citizenship());

        if (!birthDay.isPresent() || !gender.isPresent() || !citizenship.isPresent()) {
            return false;
        }

        this.inputFields = inputFields;

        citizenBirthDay = birthDay.get();
        citizenGender = gender.get();
        citizenCitizenship = citizenship.get();

        return true;
    }

    /*
     * obtain targen sitizen
     */
    private boolean defineCitizen() {
        List<Citizen> citizens = citizenRepository
                .findByNamesFamilyNameAndNamesFirstNameAndDays_BirthDayAndGenderAndCitizenship(
                        inputFields.familyName(),
                        inputFields.firstName(),
                        citizenBirthDay,
                        citizenGender,
                        citizenCitizenship);

        if (citizens.size() > 0) {
            targetCitizen = citizens.get(0);
            return true;
        }

        return false;
    }

    /*
     * adult checker
     */
    private boolean isAdult() {
        LocalDate ldate = targetCitizen.getDays().getBirthDay().toLocalDate();
        Integer years = Period.between(ldate, LocalDate.now()).getYears();

        return years >= ADULT_AGE_FROM;
    }

    /*
     * Output View
     */
    private CitizenView getCitizenView(Citizen citizen) {
        String name = citizen.getNames().getFirstName() + " " + citizen.getNames().getFamilyName();
        if (citizen.getNames().getMaidenName().length() > 0) {
            name = name + " (" + citizen.getNames().getMaidenName() + ")";
        }

        String lifeDays = citizen.getDays().getBirthDay().toString();
        if (citizen.getDays().getDeathDay() != null) {
            lifeDays = lifeDays + "-" + citizen.getDays().getDeathDay().toString();
        }

        return new CitizenView(
                name,
                lifeDays,
                citizen.getGender().toString(),
                citizen.getCitizenship().toString());
    }

    /*
     * start day NOT null
     * end day IS null
     * death of partner close marriage, defined end day by death day
     */
    private Set<CitizenView> getCurrentPartner() {
        Set<CitizenView> partnerView = new HashSet<>();

        List<Citizen> partnersAll = targetCitizen.getMarriages()
                .stream()
                .filter(ma -> ma.getDateRights().getStartDay() != null && ma.getDateRights().getEndDay() == null)
                .map(ma -> ma.getPartner()).toList();

        if (partnersAll.size() > 0) {
            partnerView.add(getCitizenView(partnersAll.get(0)));
        }

        return partnerView;
    }

    /*
     * start day NOT null
     * end day IS null
     */
    private Set<CitizenView> getChildrenByRights() {

        Set<CitizenView> childrenView = new HashSet<>();

        List<Citizen> children = targetCitizen.getParenthoods()
                .stream()
                .filter(pa -> pa.getDateRights().getStartDay() != null && pa.getDateRights().getEndDay() == null)
                .map(pa -> pa.getChild()).toList();

        children.forEach(ci -> childrenView.add(getCitizenView(ci)));

        return childrenView;
    }

    /*
     * get birth parents
     */
    private List<Citizen> getBirthParents() {
        List<Citizen> birthParents = new LinkedList<>();

        List<Parenthood> father = parenthoodRepository.findByChildAndType(targetCitizen, TypeParenthood.BIRTHFATHER);
        if (father.size() > 0) {
            birthParents.add(father.get(0).getCitizen());
        }
        List<Parenthood> mother = parenthoodRepository.findByChildAndType(targetCitizen, TypeParenthood.BIRTHMOTHER);
        if (mother.size() > 0) {
            birthParents.add(mother.get(0).getCitizen());
        }

        return birthParents;
    }

    private Set<CitizenView> getBirthParentsView(List<Citizen> birthParents) {
        Set<CitizenView> birthparentsView = new HashSet<>();
        birthParents.forEach(ci -> birthparentsView.add(getCitizenView(ci)));

        return birthparentsView;
    }

    private Set<CitizenView> getAdopters() {
        Set<CitizenView> adoptersView = new HashSet<>();

        List<Parenthood> adopterList = parenthoodRepository.findByChildAndType(targetCitizen, TypeParenthood.ADOPTER);
        adopterList.forEach(ci -> adoptersView.add(getCitizenView(ci.getCitizen())));

        return adoptersView;
    }

    private Set<CitizenView> getSiblings(List<Citizen> birthParents) {
        Set<CitizenView> siblingsView = new HashSet<>();

        birthParents.forEach(ci -> {
            Set<Parenthood> parenthoods = ci.getParenthoods();
            parenthoods.forEach(pa -> {
                if (!pa.getChild().equals(targetCitizen)) {
                    siblingsView.add(getCitizenView(pa.getChild()));
                }
            });
        });

        return siblingsView;
    }
}
