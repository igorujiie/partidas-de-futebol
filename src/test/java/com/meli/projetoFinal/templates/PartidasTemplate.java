package com.meli.projetoFinal.templates;

import com.meli.projetoFinal.dto.request.PartidasDTO;
import com.meli.projetoFinal.model.Clube;
import com.meli.projetoFinal.model.Estadio;

import java.time.LocalDate;

public class PartidasTemplate {

    public static PartidasDTO payloadDeSucesso() {
        PartidasDTO dto = new PartidasDTO();
        dto.setClubeCasa(1L);
        dto.setClubeVisitante(2L);
        dto.setGolsCasa(2);
        dto.setGolsVisitante(1);
        dto.setEstadioId(1L);
        dto.setDataPartida(LocalDate.of(2023, 12, 31));
        return dto;
    }

    public static Clube clubeCasa() {
        Clube clube = new Clube();
        clube.setId(1L);
        clube.setNome("Clube Casa");
        clube.setAtivo(true);
        clube.setDataCriacao(LocalDate.now().minusYears(5));
        return clube;
    }


    public static Clube clubeCasaInativo() {
        Clube clube = new Clube();
        clube.setId(1L);
        clube.setNome("Clube Casa");
        clube.setAtivo(false);
        clube.setDataCriacao(LocalDate.now().minusYears(5));
        return clube;
    }

    public static Clube clubeVisitante() {
        Clube clube = new Clube();
        clube.setId(2L);
        clube.setNome("Clube Visitante");
        clube.setAtivo(true);
        clube.setDataCriacao(LocalDate.now().minusYears(3));
        return clube;
    }


    public static Clube clubeVisitanteInativo() {
        Clube clube = new Clube();
        clube.setId(2L);
        clube.setNome("Clube Visitante");
        clube.setAtivo(false);
        clube.setDataCriacao(LocalDate.now().minusYears(3));
        return clube;
    }

    public static Estadio estadio() {
        Estadio estadio = new Estadio();
        estadio.setId(1L);
        estadio.setNome("Estádio Teste");
        return estadio;
    }

    public static PartidasDTO partidaComDataAnteriorACriacaoDoClube() {
        PartidasDTO dto = new PartidasDTO();
        dto.setClubeCasa(1L);
        dto.setClubeVisitante(2L);
        dto.setGolsCasa(2);
        dto.setGolsVisitante(1);
        dto.setEstadioId(3L);
        dto.setDataPartida(LocalDate.now().minusYears(6));
        return dto;
    }

    public static PartidasDTO partidaComDataAnteriorACriacaoDoClubeVisitante() {
        Clube clubeCasa = clubeCasa();
        Clube clubeVisitante = clubeVisitante();
        Estadio estadio = estadio();

        LocalDate dataPartida = clubeVisitante.getDataCriacao().minusDays(1);

        PartidasDTO dto = new PartidasDTO();
        dto.setClubeCasa(clubeCasa.getId());
        dto.setClubeVisitante(clubeVisitante.getId());
        dto.setEstadioId(estadio.getId());
        dto.setDataPartida(dataPartida);
        dto.setGolsCasa(0);
        dto.setGolsVisitante(0);
        return dto;
    }
}

