package com.hackthon.renegados.api.dto;

import com.hackthon.renegados.api.DisciplinaApi;
import java.util.List;

public class TurmaComDisciplinaDto {

    public record TurmaComDisciplinasDto(Long turmaId, String turmaNome, List<DisciplinaApi.DisciplinaDto> disciplinas) {}

}
