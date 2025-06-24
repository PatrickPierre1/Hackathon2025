package com.hackthon.renegados.service;

import com.hackthon.renegados.model.Aluno;
import com.hackthon.renegados.model.Disciplina;
import com.hackthon.renegados.model.Turma;
import com.hackthon.renegados.model.TurmaDisciplina;
import com.hackthon.renegados.repository.AlunoRepository;
import com.hackthon.renegados.repository.TurmaDisciplinaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TurmaDisciplinaService {

    @Autowired
    private TurmaDisciplinaRepository turmaDisciplinaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private TurmaService turmaService;

    @Autowired
    private DisciplinaService disciplinaService;

    public List<TurmaDisciplina> listarTodos() {
        return turmaDisciplinaRepository.findAll();
    }

    public Optional<TurmaDisciplina> buscarPorId(Long id) {
        return turmaDisciplinaRepository.findById(id);
    }

    public TurmaDisciplina salvar(TurmaDisciplina turmaDisciplina) {
        return turmaDisciplinaRepository.save(turmaDisciplina);
    }

    public void deletarPorId(Long id) {
        turmaDisciplinaRepository.deleteById(id);
    }

    public TurmaDisciplina buscar(Long turmaId, Long disciplinaId) {
        return turmaDisciplinaRepository.findByTurmaIdAndDisciplinaId(turmaId, disciplinaId)
                .orElse(null);
    }

    public TurmaDisciplina buscarOuCriar(Long turmaId, Long disciplinaId) {
        return turmaDisciplinaRepository.findByTurmaIdAndDisciplinaId(turmaId, disciplinaId)
                .orElseGet(() -> {
                    Turma turma = turmaService.buscarPorId(turmaId);
                    Disciplina disciplina = disciplinaService.buscarPorId(disciplinaId);

                    if (turma == null || disciplina == null) {
                        throw new IllegalArgumentException("Turma ou Disciplina não encontrados");
                    }

                    TurmaDisciplina td = new TurmaDisciplina();
                    td.setTurma(turma);
                    td.setDisciplina(disciplina);
                    return turmaDisciplinaRepository.save(td);
                });
    }

    public TurmaDisciplina criarVinculo(Turma turma, Disciplina disciplina) {
        TurmaDisciplina existente = turmaDisciplinaRepository.findByTurmaIdAndDisciplinaId(turma.getId(), disciplina.getId())
                .orElse(null);

        if (existente != null) {
            return existente; // Já existe o vínculo
        }

        TurmaDisciplina td = new TurmaDisciplina();
        td.setTurma(turma);
        td.setDisciplina(disciplina);
        return turmaDisciplinaRepository.save(td);
    }

    public List<Long> buscarDisciplinasIdsPorTurma(Long turmaId) {
        return turmaDisciplinaRepository.findByTurmaId(turmaId)
                .stream()
                .map(td -> td.getDisciplina().getId())
                .toList();
    }

    @Transactional
    public void vincularAluno(Long vinculoId, Long alunoId) {
        TurmaDisciplina vinculo = turmaDisciplinaRepository.findById(vinculoId)
                .orElseThrow(() -> new RuntimeException("Vínculo não encontrado"));

        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        // Setar a turmaDisciplina no aluno (lado dono)
        aluno.setTurmaDisciplina(vinculo);

        alunoRepository.save(aluno); 
    }



}
