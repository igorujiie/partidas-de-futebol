package com.meli.projetoFinal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfrontoDiretoResponseDTO {
    private Long id;
    private String clubeCasaNome;
    private String clubeVisitanteNome;
    private String estadio;
    private int golCasa;
    private int golVisitante;
    private String dataPartida;

}