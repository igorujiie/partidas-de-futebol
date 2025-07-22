package com.meli.projetoFinal.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstadioDTO {

    @NotNull
    @Size(min =3, message = "O nome do estádio deve ter pelo menos 3 caracteres")
    private String nome;
}
