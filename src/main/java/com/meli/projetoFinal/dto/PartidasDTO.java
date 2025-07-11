package com.meli.projetoFinal.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PartidasDTO {
    @NotNull(message = "Clube casa é obrigatório")
    private Long clubeCasa;

    @NotNull(message = "Clube visitante é obrigatório")
    private Long clubeVisitante;

    @NotNull(message = "Estádio é obrigatório")
    private Long estadioId;

    @NotNull(message = "Data da partida é obrigatória")
    @PastOrPresent(message = "A data da partida não pode ser no futuro")
    private LocalDate dataPartida;

    @NotNull(message = "Gols casa são obrigatórios")
    @Min(value = 0, message = "Gols não podem ser negativos")
    private Integer golsCasa;

    @NotNull(message = "Gols visitante são obrigatórios")
    @Min(value = 0, message = "Gols não podem ser negativos")
    private Integer golsVisitante;

}