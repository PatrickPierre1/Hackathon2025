package com.hackthon.renegados.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Questao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_questao", nullable = false)
    private String numeroQuestao;

    @Column(name = "resposta", nullable = false)
    private String resposta;

    @ManyToOne
    @JoinColumn(name = "prova_id")
    private Provas prova;
}
