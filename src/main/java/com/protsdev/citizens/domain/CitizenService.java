package com.protsdev.citizens.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.protsdev.citizens.dto.CitizenRequest;
import com.protsdev.citizens.models.Citizen;
import com.protsdev.citizens.models.CitizenName;
import com.protsdev.citizens.models.LifeStageDays;
import com.protsdev.citizens.repositories.CitizenRepository;

@Service
public class CitizenService {

  private CitizenRepository citizenRepository;

  private String lastError = "";

  public String getLastError() {
    return lastError;
  }

  CitizenService(CitizenRepository citizenRepository) {
    this.citizenRepository = citizenRepository;
  }

  /*
   * obtain citizen
   * error in lastError
   */
  public Optional<Citizen> fetchCitizen(CitizenRequest citizenResponse, BindingResult result) {

    lastError = "";
    Optional<Citizen> citizen = Optional.empty();

    // check binding errors
    if (result.hasErrors()) {
      lastError = result.getAllErrors().toString();
    }

    if (lastError.length() == 0) {
      // check nullable fields
      var requareError = citizenResponse.defineRequaredFields();
      if (requareError.length() > 0) {
        lastError = String.format("Fields '%s' have null value", requareError);
      }
    }

    if (lastError.length() == 0) {
      List<Citizen> citizens = citizenRepository
          .findByNamesFamilyNameAndNamesFirstNameAndDays_BirthDayAndGenderAndCitizenship(
              citizenResponse.getFamilyName(),
              citizenResponse.getFirstName(),
              citizenResponse.getBirthDay(),
              citizenResponse.getGender(),
              citizenResponse.getCitizenship());

      if (citizens.size() > 0) {
        citizen = Optional.of(citizens.get(0));
      } else {
        lastError = String.format(
            "Citizen by %s %s, birthDay at %s, has gender %s and citizenship %s is missing",
            citizenResponse.getFirstName(),
            citizenResponse.getFamilyName(),
            citizenResponse.getBirthDay(),
            citizenResponse.getGender(),
            citizenResponse.getCitizenship());
      }
    }

    return citizen;
  }

  public Optional<Citizen> create(CitizenRequest cR) {

    Citizen citizenEntity = new Citizen();
    citizenEntity.setNames(getNames(cR.getFirstName(), cR.getFamilyName()));
    citizenEntity.setDays(getLifeDays(cR.getBirthDay()));
    citizenEntity.setGender(cR.getGender());
    citizenEntity.setCitizenship(cR.getCitizenship());

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

  /*
   * find by id and hashcode
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

  /*
   * update
   */
  public Citizen update(Citizen citizen) {
    return citizenRepository.save(citizen);
  }
}
