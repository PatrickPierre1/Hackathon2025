package com.hackthon.renegados.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Professor extends Usuario {

    private Long cpf;

    @OneToMany(mappedBy = "professor")
    @JsonManagedReference
    private List<Disciplina> disciplinas;
}
