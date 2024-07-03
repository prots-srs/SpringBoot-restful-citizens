package com.protsdev.citizens.domain;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
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
import com.protsdev.citizens.enums.TypeParenthood;
import com.protsdev.citizens.models.Citizen;
import com.protsdev.citizens.models.Parenthood;
import com.protsdev.citizens.repositories.ParenthoodRepository;

@Service
public class CitizenFamilyService {
  private static final Integer ADULT_AGE_FROM = 18;

  private ParenthoodRepository parenthoodRepository;

  public CitizenFamilyService(ParenthoodRepository parenthoodRepository) {
    this.parenthoodRepository = parenthoodRepository;
  }

  private Citizen targetCitizen;

  public FamilyNuclear getNuclearFamily(Citizen citizen) {
    targetCitizen = citizen;

    // get output objects
    if (isAdult()) {
      return new FamilyNuclearAdult(
          CitizenView.converCitizenToView(targetCitizen),
          getCurrentPartner(),
          getChildrenByRights());
    } else {
      List<Citizen> birthParents = getBirthParents();

      return new FamilyNuclearChild(
          CitizenView.converCitizenToView(targetCitizen),
          getBirthParentsView(birthParents),
          getAdopters(),
          getSiblings(birthParents));
    }
  }

  public FamilyExtended getExtendedFamily(Citizen citizen) {
    targetCitizen = citizen;

    List<Citizen> birthParents = getBirthParents();

    return new FamilyExtended(
        CitizenView.converCitizenToView(targetCitizen),
        getCurrentPartner(),
        getChildrenByRights(),
        getBirthParentsView(birthParents),
        getAdopters(),
        getSiblings(birthParents));
  }

  /*
   * adult checker
   */
  private boolean isAdult() {
    LocalDate ldate = targetCitizen.getDays().getBirthDay();// .toLocalDate();
    Integer years = Period.between(ldate, LocalDate.now()).getYears();

    return years >= ADULT_AGE_FROM;
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
      partnerView.add(CitizenView.converCitizenToView(partnersAll.get(0)));
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

    children.forEach(ci -> childrenView.add(CitizenView.converCitizenToView(ci)));

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
    birthParents.forEach(ci -> birthparentsView.add(CitizenView.converCitizenToView(ci)));

    return birthparentsView;
  }

  private Set<CitizenView> getAdopters() {
    Set<CitizenView> adoptersView = new HashSet<>();

    List<Parenthood> adopterList = parenthoodRepository.findByChildAndType(targetCitizen, TypeParenthood.ADOPTER);
    adopterList.forEach(ci -> adoptersView.add(CitizenView.converCitizenToView(ci.getCitizen())));

    return adoptersView;
  }

  private Set<CitizenView> getSiblings(List<Citizen> birthParents) {
    Set<CitizenView> siblingsView = new HashSet<>();

    birthParents.forEach(ci -> {
      Set<Parenthood> parenthoods = ci.getParenthoods();
      parenthoods.forEach(pa -> {
        if (!pa.getChild().equals(targetCitizen)) {
          siblingsView.add(CitizenView.converCitizenToView(pa.getChild()));
        }
      });
    });

    return siblingsView;
  }
}
