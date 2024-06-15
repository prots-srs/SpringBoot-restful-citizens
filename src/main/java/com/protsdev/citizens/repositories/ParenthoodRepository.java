package com.protsdev.citizens.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.protsdev.citizens.models.Citizen;
import com.protsdev.citizens.models.Parenthood;

import java.util.List;

public interface ParenthoodRepository extends JpaRepository<Parenthood, Long> {
    // List<ParenthoodEntity> findByCitizen(CitizenEntity citizen);

    // List<ParenthoodEntity> findByChild(CitizenEntity child);
}
