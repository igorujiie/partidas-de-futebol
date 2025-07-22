package com.meli.projetoFinal.controller;

import com.meli.projetoFinal.dto.request.ClubeDTO;
import com.meli.projetoFinal.model.Clube;
import com.meli.projetoFinal.model.Estado;
import com.meli.projetoFinal.service.ClubeService;
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

class ClubeControllerTest {

    @Mock
    private ClubeService clubeService;

    @InjectMocks
    private ClubeController clubeController;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void cadastrarClubeComSucesso(){

        Clube clube = new Clube();
        ClubeDTO clubeDTO = new ClubeDTO();

        when(clubeService.cadastrarClube(clubeDTO)).thenReturn(clube);

        ResponseEntity<Clube> response = clubeController.cadastrar(clubeDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(clube, response.getBody());
        verify(clubeService, times(1)).cadastrarClube(clubeDTO);
    }

    @Test
    void atualizarClubeComSucesso(){
        Clube clube = new Clube();
        ClubeDTO clubeDTO = new ClubeDTO();
        Long id = 1L;

        when(clubeService.atualizarClube(id, clubeDTO)).thenReturn(clube);
        ResponseEntity<Clube> response = clubeController.atualizar(id, clubeDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clube, response.getBody());
        verify(clubeService, times(1)).atualizarClube(id, clubeDTO);
    }

    @Test
    void buscarPorIdCoSucesso(){
        Clube clube = new Clube();
        Long id = 1L;

        when(clubeService.buscarClubePorId(id)).thenReturn(clube);
        ResponseEntity<Clube> response = clubeController.buscar(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clube, response.getBody());
        verify(clubeService, times(1)).buscarClubePorId(id);
    }

@Test
    void buscarTodosClubesComFiltros() {
        Page<Clube> clubes = mock(Page.class);
        String nome = "Clube Exemplo";
        Estado estado = Estado.SP;
        Boolean ativo = true;
        Pageable pageable = mock(Pageable.class);

        when(clubeService.buscarTodosClubes(nome, estado, ativo, pageable)).thenReturn(clubes);

        ResponseEntity<Page<Clube>> response = clubeController.buscarTodosClubes(nome, estado, ativo, pageable);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clubes, response.getBody());
        verify(clubeService, times(1)).buscarTodosClubes(nome, estado, ativo, pageable);
    }

    @Test
    void buscarTodosClubesSemFiltros() {
        Page<Clube> clubes = mock(Page.class);
        Pageable pageable = mock(Pageable.class);

        when(clubeService.buscarTodosClubes(null, null, null, pageable)).thenReturn(clubes);

        ResponseEntity<Page<Clube>> response = clubeController.buscarTodosClubes(null, null, null, pageable);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clubes, response.getBody());
        verify(clubeService, times(1)).buscarTodosClubes(null, null, null, pageable);
    }

    @Test
    void buscarTodosClubesComNomeNulo() {
        Page<Clube> clubes = mock(Page.class);
        Estado estado = Estado.RJ;
        Boolean ativo = false;
        Pageable pageable = mock(Pageable.class);

        when(clubeService.buscarTodosClubes(null, estado, ativo, pageable)).thenReturn(clubes);

        ResponseEntity<Page<Clube>> response = clubeController.buscarTodosClubes(null, estado, ativo, pageable);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clubes, response.getBody());
        verify(clubeService, times(1)).buscarTodosClubes(null, estado, ativo, pageable);
    }

    @Test
    void deletarClubeComSucesso(){
        Long id = 1L;
        doNothing().when(clubeService).deletarClube(id);
        ResponseEntity<Void> response = clubeController.deletar(id);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clubeService, times(1)).deletarClube(id);
    }

}