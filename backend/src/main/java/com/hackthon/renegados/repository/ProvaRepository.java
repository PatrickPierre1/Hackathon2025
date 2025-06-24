package com.hackthon.renegados.repository;

import com.hackthon.renegados.model.Provas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProvaRepository extends JpaRepository<Provas, Long> {

    @Query("SELECT p FROM Provas p LEFT JOIN FETCH p.questoes WHERE p.id = :id")
    Optional<Provas> findByIdWithQuestoes(@Param("id") Long id);

}
