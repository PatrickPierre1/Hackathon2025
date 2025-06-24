package com.hackthon.renegados.api.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespostaInput {
    private String numeroQuestao;
    private String respostaAluno;

}