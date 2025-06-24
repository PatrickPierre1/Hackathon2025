package com.hackthon.renegados.repository;

import com.hackthon.renegados.model.AlunoProva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlunoProvaRepository extends JpaRepository<AlunoProva, Long> {

    List<AlunoProva> findByProvaId(Long provaId);

    List<AlunoProva> findByTurmaDisciplinaId(Long turmaDisciplinaId);

    List<AlunoProva> findByAlunoId(Long alunoId);

    List<AlunoProva> findByAlunoIdAndTurmaDisciplinaId(Long alunoId, Long turmaDisciplinaId);

    AlunoProva findTopByAlunoIdAndProvaIdOrderByIdDesc(Long alunoId, Long provaId);

    List<AlunoProva> findAllByAlunoIdAndTurmaDisciplina_Turma_Id(Long alunoId, Long turmaId);

}
