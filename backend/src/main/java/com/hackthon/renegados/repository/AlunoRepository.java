package com.hackthon.renegados.repository;

import com.hackthon.renegados.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository  extends JpaRepository<Aluno, Long> {

    // no repository sempre vai extender no JPA buscando a tebela de ferencia pegando o atributo da chave identificação
}
