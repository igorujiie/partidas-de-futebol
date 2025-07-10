package com.meli.projetoFinal.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PartidasDTO {

    @NotNull
    private Long clubeCasaId;

    @NotNull
    private Long clubeVisitanteId;

    @NotNull
    private Long estadioId;

    @PastOrPresent(message = "A data de criação não pode ser no futuro")
    private LocalDate dataPartida;

    private Integer golsCasa;
    private Integer golsVisitante;
}