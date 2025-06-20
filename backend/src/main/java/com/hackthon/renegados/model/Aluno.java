package com.hackthon.renegados.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Entity
@Getter // Gerar o Get
@Setter // Gerar o Set

public class Aluno extends Usuario {


    @Column(name = "ra", nullable = true) // nome da coluna
    private String ra;

}
