package com.hackthon.renegados.service;

import com.hackthon.renegados.model.Provas;
import com.hackthon.renegados.repository.ProvaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProvaService {

    @Autowired
    private ProvaRepository provaRepository;

    // Salvar ou atualizar
    public Provas salvar(Provas prova) {
        return provaRepository.save(prova);
    }

    // Buscar por ID
    @Transactional
    public Provas buscarPorId(Long id) {
        return provaRepository.findById(id).orElse(null);
    }

    @Transactional
    public Provas buscarPorIdComQuestoes(Long id) {
        return provaRepository.findByIdWithQuestoes(id).orElse(null);
    }

    // Listar todos
    public List<Provas> listarTodos() {
        return provaRepository.findAll();
    }

    // Deletar por ID
    public void deletarPorId(Long id) {
        provaRepository.deleteById(id);
    }
}
