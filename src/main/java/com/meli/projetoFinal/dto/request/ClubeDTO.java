package com.meli.projetoFinal.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class ClubeDTO {
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, message = "Nome deve ter pelo menos duas letras")
    private String nome;


    @NotNull(message = "Estado é obrigatório")
    private String estado;

    @NotNull(message = "Data de criação é obrigatória")
    @PastOrPresent(message = "A data de criação não pode ser no futuro")
    private LocalDate dataCriacao;

    private Boolean ativo;

}
