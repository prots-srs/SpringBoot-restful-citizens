package com.protsdev.citizens.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.protsdev.citizens.models.Citizen;
import com.protsdev.citizens.models.Marriage;
import java.util.List;

public interface MarriageRepository extends JpaRepository<Marriage, Long> {
  List<Marriage> findByCitizenAndPartner(Citizen citizen, Citizen partner);
}
