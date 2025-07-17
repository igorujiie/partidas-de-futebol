package com.meli.projetoFinal.service;

import com.meli.projetoFinal.exception.ConflitoDeDadosException;
import com.meli.projetoFinal.exception.DadoNaoEncontradoException;
import com.meli.projetoFinal.dto.EstadioDTO;
import com.meli.projetoFinal.model.Estadio;
import com.meli.projetoFinal.repository.EstadioRepository;
import com.meli.projetoFinal.templates.EstadioTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class EstadioServiceTest {

    @Mock
    private EstadioRepository estadioRepository;

    @InjectMocks
    private EstadioService estadioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cadastrarEstadio_Sucesso() {
        when(estadioRepository.existsEstadioByNome(anyString())).thenReturn(false);

        Estadio estadioSalvo = new Estadio();
        when(estadioRepository.save(any(Estadio.class))).thenReturn(estadioSalvo);

        Estadio result = estadioService.cadastrarEstadio(EstadioTemplate.payloadDeSucesso());
        Assertions.assertNotNull(result);
        verify(estadioRepository, times(1)).save(any(Estadio.class));
    }

    @Test
    void cadastrarEstadio_ConflitoDeDados() {
        when(estadioRepository.existsEstadioByNome(anyString())).thenReturn(true);
        Assertions.assertThrows(ConflitoDeDadosException.class, () -> estadioService.cadastrarEstadio(EstadioTemplate.payloadDeSucesso()));
    }

    @Test
    void cadastrarEstadio_DadosNomeNull() {
        when(estadioRepository.existsEstadioByNome(anyString())).thenReturn(false);
        Assertions.assertThrows(NullPointerException.class, () -> estadioService.cadastrarEstadio(EstadioTemplate.payloadDeDadosInvalidos()));
    }

    @Test
    void atualizarEstadio_Sucesso() {
        Long id = 1L;
        when(estadioRepository.existsById(id)).thenReturn(true);

        Estadio estadio = new Estadio();
        estadio.setNome("Estadio");
        when(estadioRepository.findById(id)).thenReturn(Optional.of(estadio));
        when(estadioRepository.save(any(Estadio.class))).thenReturn(estadio);

        Estadio result = estadioService.atualizarEstadio(id, EstadioTemplate.payloadDeSucesso());

        Assertions.assertNotNull(result);
        Assertions.assertEquals("ESTADIO 1", result.getNome());
    }

    @Test
    void atualizarEstadio_EstadioNaoEncontrado() {
        Long id = 1L;
        when(estadioRepository.existsById(id)).thenReturn(false);

        Assertions.assertThrows(DadoNaoEncontradoException.class, () -> estadioService.atualizarEstadio(id, EstadioTemplate.payloadDeSucesso()));
    }

    @Test
    void buscarEstadioPorIdSucesso(){
        Long id = 1L;
        Estadio estadio = new Estadio();
        when(estadioRepository.findById(id)).thenReturn(Optional.of(new Estadio()));

        Estadio result = estadioService.getEstadio(id);

        Assertions.assertNotNull(result);

    }

    @Test
    void buscarEstadioPorIdEstadioNaoEncontrado(){
        Long id = 1L;
        when(estadioRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(DadoNaoEncontradoException.class, () -> estadioService.getEstadio(id));
    }

    @Test
    void atualizarEstadio_ConflitoDeDados() {
        Long id = 1L;
        Estadio estadio = new Estadio();
        estadio.setNome("ESTADIO NOVO");
        when(estadioRepository.findById(id)).thenReturn(Optional.of(estadio));

        EstadioDTO estadioDTO = new EstadioDTO();
        estadioDTO.setNome("Estadio Novo");

        Assertions.assertThrows(ConflitoDeDadosException.class, () -> estadioService.atualizarEstadio(id, estadioDTO));
    }

    @Test
    void getEstadio_EstadioNaoEncontrado() {
        Long id = 1L;
        when(estadioRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(DadoNaoEncontradoException.class, () -> estadioService.getEstadio(id));
    }

    @Test
    void getEstadios_Sucesso() {
        Pageable pageable = mock(Pageable.class);
        Page<Estadio> page = mock(Page.class);
        when(estadioRepository.findAll(pageable)).thenReturn(page);

        Page<Estadio> result = estadioService.getEstadios(pageable);

        Assertions.assertNotNull(result);
        verify(estadioRepository, times(1)).findAll(pageable);
    }

    @Test
    void deleteEstadio_Sucesso() {
        Long id = 1L;

        estadioService.deleteEstadio(id);

        verify(estadioRepository, times(1)).deleteById(id);
    }



}