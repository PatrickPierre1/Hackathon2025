package com.hackthon.renegados.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Entity
@Getter // Gerar o Get
@Setter // Gerar o Set

public class Aluno extends Usuario {


    @Column(name = "ra", nullable = true)
    private String ra;

    @Column(name = "cpf", nullable = true)
    private String cpf;

    @Column(name = "telefone", nullable = true)
    private String telefone;

    @Column(name = "email", nullable = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "turma_disciplina_id", nullable = true)
    private TurmaDisciplina turmaDisciplina;

}
