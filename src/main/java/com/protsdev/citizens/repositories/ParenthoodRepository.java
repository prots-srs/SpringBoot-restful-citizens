package com.protsdev.citizens.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.protsdev.citizens.enums.TypeParenthood;
import com.protsdev.citizens.models.Citizen;
import com.protsdev.citizens.models.Parenthood;

public interface ParenthoodRepository extends JpaRepository<Parenthood, Long> {
    List<Parenthood> findByChildAndType(Citizen child, TypeParenthood type);
}
