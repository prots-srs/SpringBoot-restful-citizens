package com.protsdev.citizens.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.protsdev.citizens.enums.Citizenship;
import com.protsdev.citizens.enums.Gender;
import com.protsdev.citizens.models.Citizen;

import java.util.List;
import java.sql.Date;
import java.time.LocalDate;

public interface CitizenRepository extends JpaRepository<Citizen, Long> {
    // List<CitizenEntity> findAllByBirthDayBetween(Date from, Date to);

    List<Citizen> findByNamesFamilyNameAndNamesFirstNameAndDays_BirthDayAndGenderAndCitizenship(
            String familyName,
            String firstName,
            LocalDate birthDay,
            Gender gender,
            Citizenship citizenship);
}
