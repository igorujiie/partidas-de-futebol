package com.meli.projetoFinal.controller;

import com.meli.projetoFinal.dto.PartidasDTO;
import com.meli.projetoFinal.model.Estadio;
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

import static org.junit.jupiter.api.Assertions.*;
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

        when(partidasService.getPartidas(pageable)).thenReturn(partidas);
        ResponseEntity<Page<Partidas>> result = partidasController.listarPartidas(pageable);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(partidas, result.getBody());
        verify(partidasService, times(1)).getPartidas(pageable);

    }

    @Test
    void deletePartida() {
        Long id = 1L;
        ResponseEntity<Partidas> result = partidasController.deletarPartida(id);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(partidasService, times(1)).deletePartida(id);
    }

}