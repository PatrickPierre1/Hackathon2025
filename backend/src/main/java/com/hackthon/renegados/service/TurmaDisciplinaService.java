package com.hackthon.renegados.service;

import com.hackthon.renegados.model.Disciplina;
import com.hackthon.renegados.model.Turma;
import com.hackthon.renegados.model.TurmaDisciplina;
import com.hackthon.renegados.repository.TurmaDisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TurmaDisciplinaService {

    @Autowired
    private TurmaDisciplinaRepository turmaDisciplinaRepository;

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
}
