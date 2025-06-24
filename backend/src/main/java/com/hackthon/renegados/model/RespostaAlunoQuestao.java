package com.hackthon.renegados.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "resposta_aluno_questao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RespostaAlunoQuestao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relação com AlunoProva (um AlunoProva tem várias respostas)
    @ManyToOne
    @JoinColumn(name = "aluno_prova_id", nullable = false)
    private AlunoProva alunoProva;

    // Qual questão da prova
    @ManyToOne
    @JoinColumn(name = "questao_id", nullable = false)
    private Questao questao;

    // Resposta que o aluno marcou
    @Column(name = "resposta_aluno", nullable = false)
    private String respostaAluno;
}
