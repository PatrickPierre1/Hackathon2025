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


    @Column(name = "ra", nullable = true) // nome da coluna
    private String ra;

    @Column(name = "cpf", nullable = true) // nome da coluna
    private String cpf;

    @Column(name = "telefone", nullable = true) // nome da coluna
    private String telefone;

    @Column(name = "email", nullable = true) // nome da coluna
    private String email;

}
