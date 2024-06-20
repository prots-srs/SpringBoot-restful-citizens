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
        this.inputFields = inputFields;

        Optional<NuclearFamily> result = Optional.empty();

        // input fields !
        if (!checkAndGetInputFields()) {
            this.inputFields = null;
            return result;
        }

        // target citizen
        Optional<Citizen> targetCitizen = getCitizenByInputFields();
        if (!targetCitizen.isPresent()) {
            return result;
        }

        this.targetCitizen = targetCitizen.get();

        // get output objects
        if (isAdult(this.targetCitizen.getDays().getBirthDay())) {

            result = getAdultNuclearFamily();
        } else {
            result = getChildNuclearFamily();
        }

        return result;
    }

    private boolean checkAndGetInputFields() {
        Optional<Date> birthDay = verifyService.getDateByString(inputFields.birthDay());
        Optional<Gender> gender = verifyService.getGenderByString(inputFields.gender());
        Optional<Citizenship> citizenship = verifyService.getCitizenshipByString(inputFields.citizenship());

        if (!birthDay.isPresent() || !gender.isPresent() || !citizenship.isPresent()) {
            return false;
        }

        citizenBirthDay = birthDay.get();
        citizenGender = gender.get();
        citizenCitizenship = citizenship.get();

        return true;
    }

    private boolean isAdult(Date date) {
        LocalDate ldate = date.toLocalDate();
        Integer years = Period.between(ldate, LocalDate.now()).getYears();

        return years >= ADULT_AGE_FROM;
    }

    private Optional<Citizen> getCitizenByInputFields() {
        List<Citizen> citizens = citizenRepository
                .findByNamesFamilyNameAndNamesFirstNameAndDays_BirthDayAndGenderAndCitizenship(
                        inputFields.familyName(),
                        inputFields.firstName(),
                        citizenBirthDay,
                        citizenGender,
                        citizenCitizenship);

        if (citizens.size() > 0) {
            return Optional.of(citizens.get(0));
        }

        return Optional.empty();
    }

    /*
     * adult family
     */
    private Optional<NuclearFamily> getAdultNuclearFamily() {

        Set<CitizenView> partnerView = new HashSet<>();
        Set<CitizenView> childrenView = new HashSet<>();

        Optional<Citizen> partner = getCurrentPartner();
        if (partner.isPresent()) {
            partnerView.add(getCitizenView(partner.get()));
        }

        List<Citizen> children = getChildrenByRights();
        children.forEach(ci -> childrenView.add(getCitizenView(ci)));

        Optional<NuclearFamily> result = Optional.of(new NuclearFamilyAdult(
                getCitizenView(targetCitizen),
                partnerView,
                childrenView));

        return result;
    }

    private Optional<NuclearFamily> getChildNuclearFamily() {
        Set<CitizenView> birthparentsView = new HashSet<>();
        Set<CitizenView> adoptersView = new HashSet<>();
        Set<CitizenView> brothersView = new HashSet<>();

        List<Citizen> birthParents = getBirthParents();
        birthParents.forEach(ci -> birthparentsView.add(getCitizenView(ci)));

        List<Citizen> adopters = getAdopters();
        adopters.forEach(ci -> adoptersView.add(getCitizenView(ci)));

        List<Citizen> brsisList = getBrothersSisters(birthParents);
        brsisList.forEach(ci -> brothersView.add(getCitizenView(ci)));

        Optional<NuclearFamily> result = Optional.of(new NuclearFamilyChild(
                getCitizenView(targetCitizen),
                birthparentsView,
                adoptersView,
                brothersView));

        return result;
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
    private Optional<Citizen> getCurrentPartner() {
        List<Citizen> partnersAll = targetCitizen.getMarriages()
                .stream()
                .filter(ma -> ma.getDateRights().getStartDay() != null && ma.getDateRights().getEndDay() == null)
                .map(ma -> ma.getPartner()).toList();

        if (partnersAll.size() > 0) {
            return Optional.of(partnersAll.get(0));
        }

        return Optional.empty();
    }

    /*
     * start day NOT null
     * end day IS null
     */
    private List<Citizen> getChildrenByRights() {
        return targetCitizen.getParenthoods()
                .stream()
                .filter(pa -> pa.getDateRights().getStartDay() != null && pa.getDateRights().getEndDay() == null)
                .map(pa -> pa.getChild()).toList();
    }

    private List<Citizen> getBirthParents() {
        List<Citizen> parents = new LinkedList<>();

        List<Parenthood> father = parenthoodRepository.findByChildAndType(targetCitizen, TypeParenthood.BIRTHFATHER);
        if (father.size() > 0) {
            parents.add(father.get(0).getCitizen());
        }
        List<Parenthood> mather = parenthoodRepository.findByChildAndType(targetCitizen, TypeParenthood.BIRTHMOTHER);
        if (mather.size() > 0) {
            parents.add(mather.get(0).getCitizen());
        }

        return parents;
    }

    private List<Citizen> getAdopters() {
        List<Citizen> adopters = new LinkedList<>();

        List<Parenthood> adopterList = parenthoodRepository.findByChildAndType(targetCitizen, TypeParenthood.ADOPTER);
        adopterList.forEach(ci -> adopters.add(ci.getCitizen()));

        return adopters;
    }

    private List<Citizen> getBrothersSisters(List<Citizen> birthParents) {
        List<Citizen> children = new LinkedList<>();

        birthParents.forEach(ci -> {
            Set<Parenthood> parenthoods = ci.getParenthoods();
            parenthoods.forEach(pa -> {
                if (!pa.getChild().equals(targetCitizen)) {
                    children.add(pa.getChild());
                }
            });
        });

        return children;
    }
}
