package com.hackthon.renegados.api.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TurmaFormDto {
    private Long id;
    private String nome;
    private List<Long> disciplinasIds;

}