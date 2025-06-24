package com.hackthon.renegados.service;

import com.hackthon.renegados.model.AlunoProva;
import com.hackthon.renegados.model.Questao;
import com.hackthon.renegados.model.RespostaAlunoQuestao;
import com.hackthon.renegados.repository.AlunoProvaRepository;
import com.hackthon.renegados.repository.QuestaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlunoProvaService {

    @Autowired
    private AlunoProvaRepository alunoProvaRepository;

    @Autowired
    private QuestaoRepository questaoRepository;

    public AlunoProva salvar(AlunoProva alunoProva) {
        if (alunoProva.getRespostas() != null) {
            alunoProva.getRespostas().forEach(r -> r.setAlunoProva(alunoProva));
        }
        return alunoProvaRepository.save(alunoProva);
    }

    public List<AlunoProva> listarTodos() {
        return alunoProvaRepository.findAll();
    }

    public List<AlunoProva> listarPorProva(Long provaId) {
        return alunoProvaRepository.findByProvaId(provaId);
    }

    public List<AlunoProva> listarPorTurmaDisciplina(Long turmaDisciplinaId) {
        return alunoProvaRepository.findByTurmaDisciplinaId(turmaDisciplinaId);
    }

    public List<AlunoProva> listarPorAluno(Long alunoId) {
        return alunoProvaRepository.findByAlunoId(alunoId);
    }
}
