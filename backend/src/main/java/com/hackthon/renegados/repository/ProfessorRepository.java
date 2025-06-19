package com.hackthon.renegados.repository;

import com.hackthon.renegados.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}
