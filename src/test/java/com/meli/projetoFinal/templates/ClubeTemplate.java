package com.meli.projetoFinal.templates;

import com.meli.projetoFinal.dto.ClubeDTO;

import java.time.LocalDate;

public class ClubeTemplate {

    public static ClubeDTO payloadDeSucesso() {
        ClubeDTO dto = new ClubeDTO();
        dto.setNome("Santos");
        dto.setEstado("SP");
        dto.setDataCriacao(LocalDate.of(1912,04,14));
        dto.setAtivo(true);
        return dto;
    }

    public static ClubeDTO payloadDeDadosInvalidos() {
        ClubeDTO dto = new ClubeDTO();
        dto.setNome("Santos");
        dto.setEstado("SP");
        dto.setDataCriacao(LocalDate.now().minusDays(1));
        dto.setAtivo(true);
        return dto;
    }

    public static ClubeDTO payloadDDataNula() {
        ClubeDTO dto = new ClubeDTO();
        dto.setNome("Santos");
        dto.setEstado("SP");
        dto.setDataCriacao(null);
        dto.setAtivo(true);
        return dto;
    }

    public static ClubeDTO payloadDataFutura() {
        ClubeDTO dto = new ClubeDTO();
        dto.setNome("Santos");
        dto.setEstado("SP");
        dto.setDataCriacao(LocalDate.now().plusDays(1));
        dto.setAtivo(true);
        return dto;
    }

}
