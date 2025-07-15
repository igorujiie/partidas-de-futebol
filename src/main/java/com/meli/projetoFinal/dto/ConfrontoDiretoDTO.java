package com.meli.projetoFinal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfrontoDiretoDTO {
    private List<ConfrontoDiretoResponseDTO> partidas;
    private RetrospectoClubeDTO Time1;
    private RetrospectoClubeDTO Time2;
}
