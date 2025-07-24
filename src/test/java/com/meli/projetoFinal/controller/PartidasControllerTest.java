package com.meli.projetoFinal.controller;

import com.meli.projetoFinal.dto.request.PartidasDTO;
import com.meli.projetoFinal.dto.response.ConfrontoDiretoDTO;
import com.meli.projetoFinal.dto.response.RankingDTO;
import com.meli.projetoFinal.dto.response.RetrospectoAdversarioDTO;
import com.meli.projetoFinal.model.Partidas;
import com.meli.projetoFinal.service.PartidasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PartidasControllerTest {

    @Mock
    private PartidasService partidasService;

    @InjectMocks
    private PartidasController partidasController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void cadastrarPartidas() {
        Partidas partidas = new Partidas();
        PartidasDTO partidasDTO = new PartidasDTO();

        when(partidasService.cadastrarPartida(partidasDTO)).thenReturn(partidas);

        ResponseEntity<Partidas> resultado =partidasController.criarPartida(partidasDTO);
        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertEquals(partidas, resultado.getBody());
        verify(partidasService, times(1)).cadastrarPartida(partidasDTO);

    }

    @Test
    void atualizarPartidas() {
        Partidas partidas = new Partidas();
        PartidasDTO partidasDTO = new PartidasDTO();
        Long id = 1L;

        when(partidasService.atualizarPartida(id, partidasDTO)).thenReturn(partidas);
        ResponseEntity<Partidas> resultado = partidasController.atualizarPartida(id, partidasDTO);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(partidas, resultado.getBody());
        verify(partidasService, times(1)).atualizarPartida(id, partidasDTO);
    }

    @Test
    void getPartida() {
        Partidas partidas = new Partidas();
        Long id = 1L;

        when(partidasService.getPartida(id)).thenReturn(partidas);
        ResponseEntity<Partidas> resultado = partidasController.buscarPartida(id);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(partidas, resultado.getBody());
        verify(partidasService, times(1)).getPartida(id);
    }

    @Test
    void getPartidas() {
        Page<Partidas> partidas = mock(Page.class);
        Pageable pageable = mock(Pageable.class);
        boolean goleada = true;

        when(partidasService.getPartidas(pageable, goleada)).thenReturn(partidas);
        ResponseEntity<Page<Partidas>> result = partidasController.listarPartidas(pageable, goleada);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(partidas, result.getBody());
        verify(partidasService, times(1)).getPartidas(pageable, goleada);

    }

    @Test
    void deletePartida() {
        Long id = 1L;
        ResponseEntity<Partidas> result = partidasController.deletarPartida(id);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(partidasService, times(1)).deletePartida(id);
    }

//    @Test
//    void getRetrospectoClube_ReturnsRetrospectoForValidClubeId() {
//        Long clubeId = 1L;
//        RetrospectoClubeDTO dto = new RetrospectoClubeDTO();
//
//        when(partidasService.getRetrospectoClube(clubeId)).thenReturn(dto);
//
//        ResponseEntity<RetrospectoClubeDTO> response = partidasController.getRetrospectoClube(clubeId);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(dto, response.getBody());
//        verify(partidasService, times(1)).getRetrospectoClube(clubeId);
//    }

    @Test
    void getRetrospectoContraAdversarios_ReturnsListForValidClubeId() {
        Long clubeId = 1L;
        List<RetrospectoAdversarioDTO> lista = List.of(new RetrospectoAdversarioDTO());

        when(partidasService.getRetrospectoContraAdversarios(clubeId)).thenReturn(lista);

        ResponseEntity<List<RetrospectoAdversarioDTO>> response = partidasController.getRetrospectoContraAdversarios(clubeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lista, response.getBody());
        verify(partidasService, times(1)).getRetrospectoContraAdversarios(clubeId);
    }

    @Test
    void getConfrontosDiretos_ReturnsConfrontoForValidIds() {
        Long clubeId = 1L;
        Long adversarioId = 2L;
        ConfrontoDiretoDTO dto = new ConfrontoDiretoDTO();

        when(partidasService.getRetrospectoConfronto(clubeId, adversarioId)).thenReturn(dto);

        ResponseEntity<ConfrontoDiretoDTO> response = partidasController.getConfrontosDiretos(clubeId, adversarioId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(partidasService, times(1)).getRetrospectoConfronto(clubeId, adversarioId);
    }

    @Test
    void getRanking_ReturnsRankingForValidCriteria() {
        String criterio = "pontos";
        List<RankingDTO> ranking = List.of(new RankingDTO());

        when(partidasService.getRanking(criterio)).thenReturn(ranking);

        ResponseEntity<List<RankingDTO>> response = partidasController.getRanking(criterio);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ranking, response.getBody());
        verify(partidasService, times(1)).getRanking(criterio);
    }

}