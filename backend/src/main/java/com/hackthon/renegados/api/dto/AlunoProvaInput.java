package com.hackthon.renegados.api.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlunoProvaInput {

    private Long alunoId;
    private Long provaId;
    private Long turmaDisciplinaId;
    private Double nota;
    private String status;
    private List<RespostaInput> respostas;

}
