package com.hackthon.renegados.service;

import com.hackthon.renegados.model.Questao;
import com.hackthon.renegados.repository.QuestaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestaoService {

    @Autowired
    private QuestaoRepository questaoRepository;

    // Salvar ou atualizar questão
    public Questao salvar(Questao questao) {
        return questaoRepository.save(questao);
    }

    // Buscar por ID
    public Questao buscarPorId(Long id) {
        return questaoRepository.findById(id).orElse(null);
    }

    // Listar todas as questões
    public List<Questao> listarTodos() {
        return questaoRepository.findAll();
    }

    // Deletar por ID
    public void deletarPorId(Long id) {
        questaoRepository.deleteById(id);
    }
}
