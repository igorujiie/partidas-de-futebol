package com.meli.projetoFinal.dto;

import com.meli.projetoFinal.model.Estado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class ClubeRequestDTO {
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, message = "Nome deve ter pelo menos duas letras")
    private String nome;


    @NotNull(message = "Estado é obrigatório")
    private Estado estado;

    @NotNull(message = "Data de criação é obrigatória")
    @PastOrPresent(message = "A data de criação não pode ser no futuro")
    private LocalDate dataCriacao;

}
