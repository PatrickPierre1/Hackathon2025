package com.hackthon.renegados.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter

public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;


    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = true)
    @JsonBackReference
    private Professor professor;


    @ManyToMany
    @JoinTable(
            name = "professor_turma",
            joinColumns = @JoinColumn(name = "professor_id"),
            inverseJoinColumns = @JoinColumn(name = "turma_id")
    )
    private List<Turma> turmas;
}
