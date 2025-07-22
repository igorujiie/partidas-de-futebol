package com.meli.projetoFinal.controller;

import com.meli.projetoFinal.dto.request.EstadioDTO;
import com.meli.projetoFinal.model.Estadio;
import com.meli.projetoFinal.service.EstadioService;
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

class EstadioControllerTest {

    @Mock
    private EstadioService estadioService;

    @InjectMocks
    private EstadioController controller;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void CadastrarEstadioComSucesso() {
        Estadio estadio = new Estadio();
        EstadioDTO estadioDTO = new EstadioDTO();

        when(estadioService.cadastrarEstadio(estadioDTO)).thenReturn(estadio);

        ResponseEntity<Estadio> result = controller.cadastrarEstadio(estadioDTO);
        assertEquals(estadio, result.getBody());
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        verify(estadioService, times(1)).cadastrarEstadio(estadioDTO);

    }

    @Test
    void atualizarEstadioComSucesso() {
        Estadio estadio = new Estadio();
        EstadioDTO estadioDTO = new EstadioDTO();
        Long id = 1L;

        when(estadioService.atualizarEstadio(id, estadioDTO)).thenReturn(estadio);
        ResponseEntity<Estadio> result = controller.atualizarEstadio(id, estadioDTO);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(estadio, result.getBody());
        verify(estadioService, times(1)).atualizarEstadio(id, estadioDTO);
    }

    @Test
    void buscarEstadioPorIdComSucesso() {
        Long id = 1L;
        Estadio estadio = new Estadio();

        when(estadioService.getEstadio(id)).thenReturn(estadio);
        ResponseEntity<Estadio> result = controller.getEstadio(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(estadio, result.getBody());
        verify(estadioService, times(1)).getEstadio(id);
    }

    @Test
    void buscarEstadios(){
        Page<Estadio> estadios = mock(Page.class);
        Pageable pageable = mock(Pageable.class);

        when(estadioService.getEstadios(pageable)).thenReturn(estadios);
        ResponseEntity<Page<Estadio>> result = controller.getEstadios(pageable);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(estadios, result.getBody());
        verify(estadioService, times(1)).getEstadios(pageable);




    }

}