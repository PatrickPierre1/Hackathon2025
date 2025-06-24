package com.hackthon.renegados.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aluno_prova_turma_disciplina")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlunoProva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "prova_id", nullable = false)
    private Provas prova;

    @ManyToOne
    @JoinColumn(name = "turma_disciplina_id", nullable = false)
    private TurmaDisciplina turmaDisciplina;

    @Column(name = "nota")
    private Double nota;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "alunoProva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RespostaAlunoQuestao> respostas = new ArrayList<>();

}
