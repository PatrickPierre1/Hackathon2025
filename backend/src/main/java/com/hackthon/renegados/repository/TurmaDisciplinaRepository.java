package com.hackthon.renegados.repository;

import com.hackthon.renegados.model.TurmaDisciplina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TurmaDisciplinaRepository extends JpaRepository<TurmaDisciplina, Long> {
    Optional<TurmaDisciplina> findByTurmaIdAndDisciplinaId(Long turmaId, Long disciplinaId);

    List<TurmaDisciplina> findByTurmaId(Long turmaId);
}
