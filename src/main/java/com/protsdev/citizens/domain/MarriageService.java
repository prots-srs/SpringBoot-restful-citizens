package com.protsdev.citizens.domain;

import com.protsdev.citizens.dto.MarriageRequest;
import com.protsdev.citizens.enums.Gender;
import com.protsdev.citizens.enums.TypeParenthood;
import com.protsdev.citizens.models.Citizen;
import com.protsdev.citizens.models.DateRights;
import com.protsdev.citizens.models.Marriage;
import com.protsdev.citizens.models.Parenthood;
import com.protsdev.citizens.repositories.MarriageRepository;
import com.protsdev.citizens.repositories.ParenthoodRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

/**
 * create and dissolution marriages.
 */
@Service
public class MarriageService {
  private Map<String, String> messages = new HashMap<>();

  private final MarriageRepository marriageRepository;
  private final ParenthoodRepository parenthoodRepository;

  private final CitizenService citizenService;

  private List<String> errorMessages;

  public MarriageService(
      ParenthoodRepository parenthoodRepository,
      MarriageRepository marriageRepository,
      CitizenService citizenService) {
    this.marriageRepository = marriageRepository;
    this.citizenService = citizenService;
    this.parenthoodRepository = parenthoodRepository;

    messages.put("error_null", "Fields '%s' have null value.");
    messages.put("error_notfound", "Citizens not found");
    messages.put("error_wrong_date", "Wrong date of event of citizen %s");
    messages.put("error_child", "Citizen %s is child");
    messages.put("error_active", "Citizen %s has current active marriage");
    messages.put("error_parenthood_relations", "Citizen %s has parenthood relations with %s");
    messages.put("error_sibling_relations", "Citizen %s has sibling relations with %s");
    messages.put("error_notpersist", "Data not persisting");
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

  /**
   * Front method.
   * set marriage
   */
  public Optional<Marriage> create(MarriageRequest inputFields) {

    // define error messages
    errorMessages = new LinkedList<>();

    // define empty answer
    Optional<Marriage> marriage = Optional.empty();

    // check not null
    var requareError = inputFields.defineRequaredFields();
    if (requareError.length() > 0) {
      errorMessages.add(String.format(messages.get("error_null"), requareError));
    } else {

      // find citizens
      Optional<Citizen> partnerAOp = citizenService.findById(inputFields.getIdCitizenA(),
          inputFields.getHashCodeCitizenA());
      Optional<Citizen> partnerBOp = citizenService.findById(inputFields.getIdCitizenB(),
          inputFields.getHashCodeCitizenB());

      // citizens is present
      if (partnerAOp.isPresent() && partnerBOp.isPresent()) {

        Citizen partnerA = partnerAOp.get();
        Citizen partnerB = partnerBOp.get();

        if (checkCitizensForCreate(partnerA, partnerB, inputFields.getDateOfEvent())) {

          // persisting
          createEntities(partnerA, partnerB, inputFields.getDateOfEvent());

          // processing maiden name
          if (inputFields.getGiveHusbandFamilyName().booleanValue()) {
            if (partnerA.getGender().equals(Gender.FEMALE)) {
              citizenService.setMaidenName(partnerA, partnerB.getNames().getFamilyName());
              partnerA = citizenService.update(partnerA);
            } else if (partnerB.getGender().equals(Gender.FEMALE)) {
              citizenService.setMaidenName(partnerB, partnerA.getNames().getFamilyName());
              partnerB = citizenService.update(partnerB);
            }
          }

          List<Marriage> marriagesList = marriageRepository.findByCitizenAndPartner(partnerA, partnerB);
          if (marriagesList.size() > 0) {
            marriage = Optional.of(marriagesList.get(0));
          }
        }
      } else {
        // citizens not found
        errorMessages.add(messages.get("error_notfound"));
      }
    }

    return marriage;
  }

  /**
   * need check few stages
   * - partners haven`t current active marriage from another citizen
   * - partners is adult or dateOfEvent will be smaller then parnters birtdates
   * from 18 years
   */
  private boolean checkCitizensForCreate(Citizen cA, Citizen cB, LocalDate eD) {
    return checkDateOfEvent(cA, eD)
        && checkDateOfEvent(cB, eD)
        && checkMissingActiveMarriage(cA)
        && checkMissingActiveMarriage(cB)
        && checkMissingParenthoods(cA, cB)
        && checkMissingSibling(cA, cB);
  }

  /**
   * partners is adult or dateOfEvent will be smaller then parnters birtdates
   * from 18 years.
   */
  private boolean checkDateOfEvent(Citizen ci, LocalDate eD) {

    if (ci.getDays().getBirthDay().compareTo(eD) >= 0) {
      errorMessages.add(String.format(messages.get("error_wrong_date"), ci.getNames().toString()));
      return false;
    } else {

      Integer years = Period.between(ci.getDays().getBirthDay(), eD).getYears();
      if (years < CitizenService.ADULT_AGE_FROM) {
        errorMessages.add(String.format(messages.get("error_child"), ci.getNames().toString()));
        return false;
      }
    }

    return true;
  }

  private boolean checkDateOfEventByFuture(LocalDate eD) {
    if (eD != null) {
      if (eD.compareTo(LocalDate.now()) > 0) {
        return false;
      }
    }

    return true;
  }

  /**
   * partners haven`t current marriage from another citizen
   */
  private boolean checkMissingActiveMarriage(Citizen ci) {
    List<Boolean> activeMarriage = new LinkedList<>();
    activeMarriage.add(true);
    ci.getMarriages()
        .forEach(ma -> {
          if (ma.getDateRights().getEndDate() == null) {
            errorMessages.add(String.format(messages.get("error_active"),
                ci.getNames().toString()));
            activeMarriage.set(0, false);
          }
        });

    return activeMarriage.get(0);
  }

  /*
   * - haven`t direct family relations
   * deprecated father<->daughter, mother<->son, sibling
   */
  private boolean checkMissingParenthoods(Citizen partnerA, Citizen partnerB) {
    List<Boolean> parenthoodStage = new LinkedList<>();
    parenthoodStage.add(true);

    partnerA.getParenthoods().forEach(pa -> {
      if (pa.getChild().equals(partnerB)
          && (pa.getType().equals(TypeParenthood.BIRTHFATHER) || pa.getType().equals(TypeParenthood.BIRTHMOTHER))) {
        errorMessages.add(String.format(messages.get("error_parenthood_relations"),
            partnerA.getNames().toString(), partnerB.getNames().toString()));
        parenthoodStage.set(0, false);
      }
    });
    partnerB.getParenthoods().forEach(pa -> {
      if (pa.getChild().equals(partnerA)
          && (pa.getType().equals(TypeParenthood.BIRTHFATHER) || pa.getType().equals(TypeParenthood.BIRTHMOTHER))) {
        errorMessages.add(String.format(messages.get("error_parenthood_relations"),
            partnerA.getNames().toString(), partnerB.getNames().toString()));
        parenthoodStage.set(0, false);
      }
    });

    return parenthoodStage.get(0);
  }

  private boolean checkMissingSibling(Citizen partnerA, Citizen partnerB) {
    Set<Citizen> parentsA = new HashSet<>();
    Set<Citizen> parentsB = new HashSet<>();

    List<Boolean> siblingStage = new LinkedList<>();
    siblingStage.add(true);

    List<Parenthood> parentsAF = parenthoodRepository.findByChildAndType(partnerA, TypeParenthood.BIRTHFATHER);
    List<Parenthood> parentsAM = parenthoodRepository.findByChildAndType(partnerA, TypeParenthood.BIRTHMOTHER);
    parentsAF.forEach(pa -> parentsA.add(pa.getCitizen()));
    parentsAM.forEach(pa -> parentsA.add(pa.getCitizen()));

    List<Parenthood> parentsBF = parenthoodRepository.findByChildAndType(partnerB, TypeParenthood.BIRTHFATHER);
    List<Parenthood> parentsBM = parenthoodRepository.findByChildAndType(partnerB, TypeParenthood.BIRTHMOTHER);
    parentsBF.forEach(pa -> parentsB.add(pa.getCitizen()));
    parentsBM.forEach(pa -> parentsB.add(pa.getCitizen()));

    parentsA.forEach(parentA -> {
      parentsB.forEach(parentB -> {
        if (parentB.equals(parentA)) {
          errorMessages.add(String.format(messages.get("error_sibling_relations"),
              partnerA.getNames().toString(), partnerB.getNames().toString()));
          siblingStage.set(0, false);
        }
      });
    });

    return siblingStage.get(0);
  }

  /**
   * create marriage records
   */
  private void createEntities(Citizen partnerA, Citizen partnerB, LocalDate dateOfEvent) {

    DateRights dre = new DateRights();
    dre.setStartDate(dateOfEvent);

    Marriage marriageA = new Marriage();
    marriageA.setCitizen(partnerA);
    marriageA.setPartner(partnerB);
    marriageA.setDateRights(dre);
    marriageRepository.save(marriageA);

    Marriage marriageB = new Marriage();
    marriageB.setCitizen(partnerB);
    marriageB.setPartner(partnerA);
    marriageB.setDateRights(dre);
    marriageRepository.save(marriageB);
  }

  /**
   * Front method.
   * dissolutions marriage
   */
  public Optional<Marriage> dissolution(MarriageRequest inputFields) {
    // define error messages
    errorMessages = new LinkedList<>();
    Optional<Marriage> marriage = Optional.empty();

    // check not null
    var requareError = inputFields.defineRequaredFields();
    if (requareError.length() > 0) {
      errorMessages.add(String.format(messages.get("error_null"), requareError));
    } else {

      // find citizens
      Optional<Citizen> partnerAOp = citizenService.findById(inputFields.getIdCitizenA(),
          inputFields.getHashCodeCitizenA());
      Optional<Citizen> partnerBOp = citizenService.findById(inputFields.getIdCitizenB(),
          inputFields.getHashCodeCitizenB());

      // citizens is present
      if (partnerAOp.isPresent() && partnerBOp.isPresent() && checkDateOfEventByFuture(inputFields.getDateOfEvent())) {

        Optional<Marriage> upMarriage = updateEntities(partnerAOp.get(), partnerBOp.get(),
            inputFields.getDateOfEvent());
        if (upMarriage.isPresent()) {
          marriage = upMarriage;
        } else {
          errorMessages.add(messages.get("error_notpersist"));
        }
      } else {
        // citizens not found
        errorMessages.add(messages.get("error_notfound"));
      }
    }

    return marriage;
  }

  /**
   * create marriage records
   */
  private Optional<Marriage> updateEntities(Citizen partnerA, Citizen partnerB, LocalDate dateOfEvent) {
    LocalDate date = dateOfEvent == null ? LocalDate.now() : dateOfEvent;

    Optional<Marriage> updatedMarriage = Optional.empty();

    List<Marriage> marriagesListA = marriageRepository.findByCitizenAndPartner(partnerA, partnerB);
    if (marriagesListA.size() > 0) {
      Marriage marriageEntityA = marriagesListA.get(0);
      marriageEntityA.getDateRights().setEndDate(date);
      updatedMarriage = Optional.of(marriageRepository.save(marriageEntityA));
    }
    List<Marriage> marriagesListB = marriageRepository.findByCitizenAndPartner(partnerB, partnerA);
    if (marriagesListB.size() > 0) {
      Marriage marriageEntityB = marriagesListB.get(0);
      marriageEntityB.getDateRights().setEndDate(date);
      marriageRepository.save(marriageEntityB);
    }

    return updatedMarriage;
  }
}
