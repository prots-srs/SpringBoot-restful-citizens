package com.protsdev.citizens.domain;

import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.protsdev.citizens.dto.CitizenView;
import com.protsdev.citizens.dto.FamilyExtended;
import com.protsdev.citizens.dto.FamilyNuclear;
import com.protsdev.citizens.dto.FamilyNuclearAdult;
import com.protsdev.citizens.dto.FamilyNuclearChild;
import com.protsdev.citizens.enums.ParenthoodType;
import com.protsdev.citizens.models.Citizen;
import com.protsdev.citizens.models.Parenthood;
import com.protsdev.citizens.repositories.ParenthoodRepository;

@Service
public class FamilyService {
    private final ParenthoodRepository parenthoodRepository;
    private final CitizenService citizenService;

    public FamilyService(ParenthoodRepository parenthoodRepository,
            CitizenService citizenService) {
        this.parenthoodRepository = parenthoodRepository;
        this.citizenService = citizenService;
    }

    private Citizen targetCitizen;

    public FamilyNuclear getNuclearFamily(Citizen citizen) {
        targetCitizen = citizen;

        // get output objects
        if (citizenService.isAdult(targetCitizen)) {
            return new FamilyNuclearAdult(
                    CitizenView.convertCitizenToView(targetCitizen),
                    getCurrentPartner(),
                    getChildrenByRights());
        } else {
            List<Citizen> birthParents = getBirthParents();

            return new FamilyNuclearChild(
                    CitizenView.convertCitizenToView(targetCitizen),
                    getBirthParentsView(birthParents),
                    getAdopters(),
                    getSiblings(birthParents));
        }
    }

    public FamilyExtended getExtendedFamily(Citizen citizen) {
        targetCitizen = citizen;

        List<Citizen> birthParents = getBirthParents();

        return new FamilyExtended(
                CitizenView.convertCitizenToView(targetCitizen),
                getCurrentPartner(),
                getChildrenByRights(),
                getBirthParentsView(birthParents),
                getAdopters(),
                getSiblings(birthParents));
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
                .filter(ma -> ma.getDateRights().getStartDate() != null && ma.getDateRights().getEndDate() == null)
                .map(ma -> ma.getPartner()).toList();

        if (partnersAll.size() > 0) {
            partnerView.add(CitizenView.convertCitizenToView(partnersAll.get(0)));
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
                .filter(pa -> pa.getDateRights().getStartDate() != null && pa.getDateRights().getEndDate() == null)
                .map(pa -> pa.getChild()).toList();

        children.forEach(ci -> childrenView.add(CitizenView.convertCitizenToView(ci)));

        return childrenView;
    }

    /*
     * get birth parents
     */
    private List<Citizen> getBirthParents() {
        List<Citizen> birthParents = new LinkedList<>();

        List<Parenthood> father = parenthoodRepository.findByChildAndType(targetCitizen, ParenthoodType.BIRTHFATHER);
        if (father.size() > 0) {
            birthParents.add(father.get(0).getCitizen());
        }
        List<Parenthood> mother = parenthoodRepository.findByChildAndType(targetCitizen, ParenthoodType.BIRTHMOTHER);
        if (mother.size() > 0) {
            birthParents.add(mother.get(0).getCitizen());
        }

        return birthParents;
    }

    private Set<CitizenView> getBirthParentsView(List<Citizen> birthParents) {
        Set<CitizenView> birthparentsView = new HashSet<>();
        birthParents.forEach(ci -> birthparentsView.add(CitizenView.convertCitizenToView(ci)));

        return birthparentsView;
    }

    private Set<CitizenView> getAdopters() {
        Set<CitizenView> adoptersView = new HashSet<>();

        List<Parenthood> adopterList = parenthoodRepository.findByChildAndType(targetCitizen, ParenthoodType.ADOPTER);
        adopterList.forEach(ci -> adoptersView.add(CitizenView.convertCitizenToView(ci.getCitizen())));

        return adoptersView;
    }

    private Set<CitizenView> getSiblings(List<Citizen> birthParents) {
        Set<CitizenView> siblingsView = new HashSet<>();

        birthParents.forEach(ci -> {
            Set<Parenthood> parenthoods = ci.getParenthoods();
            parenthoods.forEach(pa -> {
                if (!pa.getChild().equals(targetCitizen)) {
                    siblingsView.add(CitizenView.convertCitizenToView(pa.getChild()));
                }
            });
        });

        return siblingsView;
    }
}
