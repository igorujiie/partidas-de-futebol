package com.meli.projetoFinal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RankingDTO {

    private Long clubeId;
    private String clubeNome;
    private int pontos;
    private int gols;
    private int vitorias;
    private int jogos;

}
