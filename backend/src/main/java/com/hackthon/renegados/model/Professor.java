package com.hackthon.renegados.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Professor extends Usuario{

    private Long cpf;

}
