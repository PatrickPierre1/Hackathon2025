package com.hackthon.renegados.repository;

import com.hackthon.renegados.model.Questao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestaoRepository extends JpaRepository<Questao, Long> {
}
