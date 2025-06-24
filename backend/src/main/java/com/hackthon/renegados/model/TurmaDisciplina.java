package com.hackthon.renegados.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "turma_disciplina")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TurmaDisciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    @ManyToOne
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @ManyToMany
    @JsonIgnore
    @JoinTable(
            name = "turma_disciplina_provas",
            joinColumns = @JoinColumn(name = "turma_disciplina_id"),
            inverseJoinColumns = @JoinColumn(name = "prova_id")
    )
    private List<Provas> provas = new ArrayList<>();

    @OneToMany(mappedBy = "turmaDisciplina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Aluno> alunos = new ArrayList<>();

}