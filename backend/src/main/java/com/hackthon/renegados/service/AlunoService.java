package com.hackthon.renegados.service;

import com.hackthon.renegados.model.Aluno;
import com.hackthon.renegados.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService{

    @Autowired
    private AlunoRepository alunoRepository;

    // Listar alunos é uma lista de todos os alunos
    public List<Aluno>  listarTodos(){ return alunoRepository.findAll(); }


    public Aluno salvar(Aluno aluno){
        System.out.println("to aqui mano :"+aluno);
        try{
            if (aluno == null){
                throw  new RuntimeException("errou");
            }

            String cpfNumerico = aluno.getCpf().replaceAll("\\D", "");
            aluno.setPassword(new BCryptPasswordEncoder().encode(cpfNumerico));

            aluno.setLogin(aluno.getRa());
            aluno.setRole("ALUNO");

            alunoRepository.save(aluno);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        return aluno;
    }

    public Aluno buscarPorId(Long id){ return alunoRepository.findById(id).orElse(null); }

    //Remover aluno por id
    public void deletarPorId(Long id){ alunoRepository.deleteById(id); }
}
