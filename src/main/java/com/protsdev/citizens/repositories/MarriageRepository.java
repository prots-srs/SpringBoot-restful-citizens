package com.protsdev.citizens.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.protsdev.citizens.models.Marriage;

public interface MarriageRepository extends JpaRepository<Marriage, Long> {
}
