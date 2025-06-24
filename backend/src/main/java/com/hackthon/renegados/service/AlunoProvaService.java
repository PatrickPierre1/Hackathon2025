package com.hackthon.renegados.service;

import com.hackthon.renegados.api.dto.AlunoProvaInput;
import com.hackthon.renegados.api.dto.RespostaInput;
import com.hackthon.renegados.model.*;
import com.hackthon.renegados.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlunoProvaService {

    @Autowired
    private AlunoProvaRepository alunoProvaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProvaRepository provaRepository;

    @Autowired
    private TurmaDisciplinaRepository turmaDisciplinaRepository;

    @Autowired
    private QuestaoRepository questaoRepository;

    public AlunoProva salvarAlunoProvaComRespostas(AlunoProvaInput input) {
        AlunoProva alunoProva = new AlunoProva();

        // Buscar entidades principais
        Aluno aluno = alunoRepository.findById(input.getAlunoId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        Provas prova = provaRepository.findById(input.getProvaId())
                .orElseThrow(() -> new RuntimeException("Prova não encontrada"));
        TurmaDisciplina turmaDisciplina = turmaDisciplinaRepository.findById(input.getTurmaDisciplinaId())
                .orElseThrow(() -> new RuntimeException("TurmaDisciplina não encontrada"));

        alunoProva.setAluno(aluno);
        alunoProva.setProva(prova);
        alunoProva.setTurmaDisciplina(turmaDisciplina);
        alunoProva.setNota(input.getNota());
        alunoProva.setStatus(input.getStatus());

        List<RespostaAlunoQuestao> respostasEntidade = new ArrayList<>();

        for (RespostaInput ri : input.getRespostas()) {
            Questao questao = questaoRepository.findByNumeroQuestaoAndProvaId(
                    ri.getNumeroQuestao(), prova.getId());

            if (questao == null) {
                throw new RuntimeException("Questao não encontrada: numero " + ri.getNumeroQuestao());
            }

            RespostaAlunoQuestao resposta = new RespostaAlunoQuestao();
            resposta.setAlunoProva(alunoProva);
            resposta.setQuestao(questao);
            resposta.setRespostaAluno(ri.getRespostaAluno());

            respostasEntidade.add(resposta);
        }

        alunoProva.setRespostas(respostasEntidade);

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

    public AlunoProva buscarPorAlunoEProva(Long alunoId, Long provaId) {
        return alunoProvaRepository.findTopByAlunoIdAndProvaIdOrderByIdDesc(alunoId, provaId);
    }

    public List<AlunoProva> listarPorAlunoETurma(Long alunoId, Long turmaId) {
        return alunoProvaRepository.findAllByAlunoIdAndTurmaDisciplina_Turma_Id(alunoId, turmaId);
    }


}
