package com.hackthon.renegados.service;

import com.hackthon.renegados.model.Turma;
import com.hackthon.renegados.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurmaService {

    @Autowired
    private TurmaRepository turmaRepository;

    public List<Turma> listarTodos() {
        return turmaRepository.findAll();
    }

    public Turma buscarPorId(Long id) {
        return turmaRepository.findById(id).orElse(null);
    }

    public Turma salvar(Turma turma) {
        return turmaRepository.save(turma);
    }

    public void deletarPorId(Long id) {
        turmaRepository.deleteById(id);
    }
}
