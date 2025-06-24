package com.hackthon.renegados.repository;

import com.hackthon.renegados.model.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TurmaRepository extends JpaRepository<Turma, Long> {

    @Query("SELECT t FROM Turma t LEFT JOIN FETCH t.turmaDisciplinas td LEFT JOIN FETCH td.disciplina d LEFT JOIN FETCH d.professor WHERE t.id = :id")
    Optional<Turma> findByIdWithDisciplinas(@Param("id") Long id);

}
