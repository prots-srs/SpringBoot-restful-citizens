package com.protsdev.citizens.domain;

import com.protsdev.citizens.dto.CitizenRequest;
import com.protsdev.citizens.models.Citizen;
import com.protsdev.citizens.models.CitizenName;
import com.protsdev.citizens.models.LifeStageDays;
import com.protsdev.citizens.repositories.CitizenRepository;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
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

  private String lastError = "";

  public String getLastError() {
    return lastError;
  }

  CitizenService(CitizenRepository citizenRepository) {
    this.citizenRepository = citizenRepository;

    messages.put("error_null", "Fields '%s' have null value.");
    messages.put("error_missing", "Citizen by %s %s, birthDay at %s, has gender %s and citizenship %s is missing");
  }

  /**
   * obtain citizen.
   * error in lastError
   */
  public Optional<Citizen> fetchCitizen(CitizenRequest inputFields) {

    lastError = "";
    Optional<Citizen> citizen = Optional.empty();

    // check nullable fields
    var requareError = inputFields.defineRequaredFields();
    if (requareError.length() > 0) {
      lastError = String.format(messages.get("error_null"), requareError);
    }

    if (lastError.length() == 0) {
      List<Citizen> citizens = citizenRepository
          .findByNamesFamilyNameAndNamesFirstNameAndDays_BirthDayAndGenderAndCitizenship(
              inputFields.getFamilyName(),
              inputFields.getFirstName(),
              inputFields.getBirthDay(),
              inputFields.getGender(),
              inputFields.getCitizenship());

      if (citizens.size() > 0) {
        citizen = Optional.of(citizens.get(0));
      } else {
        lastError = String.format(
            messages.get("error_missing"),
            inputFields.getFirstName(),
            inputFields.getFamilyName(),
            inputFields.getBirthDay(),
            inputFields.getGender(),
            inputFields.getCitizenship());
      }
    }

    return citizen;
  }

  /**
   * create citizen from success request.
   */
  public Optional<Citizen> create(CitizenRequest citizenRequest) {

    Citizen citizenEntity = new Citizen();
    citizenEntity.setNames(getNames(citizenRequest.getFirstName(), citizenRequest.getFamilyName()));
    citizenEntity.setDays(getLifeDays(citizenRequest.getBirthDay()));
    citizenEntity.setGender(citizenRequest.getGender());
    citizenEntity.setCitizenship(citizenRequest.getCitizenship());

    Optional<Citizen> ci = Optional.of(citizenRepository.save(citizenEntity));

    return ci;
  }

  private CitizenName getNames(String firstName, String familyName) {
    CitizenName names = new CitizenName();
    names.setFirstName(firstName);
    names.setFamilyName(familyName);
    names.setMaidenName("");
    names.setSecondName("");

    return names;
  }

  private LifeStageDays getLifeDays(LocalDate birthDay) {
    LifeStageDays days = new LifeStageDays();
    days.setBirthDay(birthDay);

    return days;
  }

  /**
   * find by id and hashcode.
   */
  public Optional<Citizen> findById(Long id, Integer hashCode) {

    // check null
    if (id == null || hashCode == null) {
      return Optional.empty();
    }

    // check hashCode
    Optional<Citizen> citizen = citizenRepository.findById(id);
    if (citizen.isPresent()) {
      if (citizen.hashCode() != hashCode) {
        return Optional.empty();
      }
    }

    return citizen;
  }

  /**
   * update
   */
  public Citizen update(Citizen citizen) {
    return citizenRepository.save(citizen);
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
}
