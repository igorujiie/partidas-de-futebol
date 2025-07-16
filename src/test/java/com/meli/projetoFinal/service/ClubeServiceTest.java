package com.meli.projetoFinal.service;

import com.meli.projetoFinal.Exception.ConflitoDeDadosException;
import com.meli.projetoFinal.Exception.DadoNaoEncontradoException;
import com.meli.projetoFinal.model.Clube;
import com.meli.projetoFinal.model.Estado;
import com.meli.projetoFinal.repository.ClubeRepository;
import com.meli.projetoFinal.templates.ClubeTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ClubeServiceTest {
    @Mock
    private ClubeRepository clubeRepository;

    @InjectMocks
    private ClubeService clubeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void cadastrarClube_Sucesso() {
        when(clubeRepository.existsByNomeAndEstado(anyString(), any(Estado.class))).thenReturn(false);

        Clube clubeSalvo = new Clube();
        when(clubeRepository.save(any(Clube.class))).thenReturn(clubeSalvo);


        Clube result = clubeService.cadastrarClube(ClubeTemplate.payloadDeSucesso());

        Assertions.assertNotNull(result);
        verify(clubeRepository, times(1)).save(any(Clube.class));
    }

    @Test
    void cadastrarClube_ConflitoDeDados() {
        when(clubeRepository.existsByNomeAndEstado(anyString(), any(Estado.class))).thenReturn(true);

        Assertions.assertThrows(ConflitoDeDadosException.class, () -> clubeService.cadastrarClube(ClubeTemplate.payloadDeDadosInvalidos()));
    }

    @Test
    void atualizarClube_Sucesso() {
        Long id = 1L;
        when(clubeRepository.existsById(id)).thenReturn(true);

        Clube clube = new Clube();
        when(clubeRepository.findById(id)).thenReturn(Optional.of(clube));
        when(clubeRepository.save(any(Clube.class))).thenReturn(clube);

        Clube resultado = clubeService.atualizarClube(id, ClubeTemplate.payloadDeSucesso());

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("SANTOS", resultado.getNome());
        Assertions.assertEquals(Estado.SP, resultado.getEstado());
    }

    @Test
    void atualizarClube_ClubeNaoEncontrado() {
        Long id = 1L;
        when(clubeRepository.existsById(id)).thenReturn(false);

        Assertions.assertThrows(DadoNaoEncontradoException.class, () -> clubeService.atualizarClube(id, ClubeTemplate.payloadDeDadosInvalidos()));
    }

    @Test
    void atualizarClube_DataFuturaOuNula() {
        Long id = 1L;
        when(clubeRepository.existsById(id)).thenReturn(true);
        Assertions.assertThrows(ConflitoDeDadosException.class, () -> clubeService.atualizarClube(id, ClubeTemplate.payloadDataFutura()));
        Assertions.assertThrows(ConflitoDeDadosException.class, () -> clubeService.atualizarClube(id, ClubeTemplate.payloadDDataNula()));
    }

    @Test
    void buscarClubePorId_Sucesso() {
        Long id = 1L;
        Clube clube = new Clube();
        when(clubeRepository.findById(id)).thenReturn(Optional.of(clube));

        Clube encontrado = clubeService.buscarClubePorId(id);

        Assertions.assertNotNull(encontrado);
    }

    @Test
    void buscarClubePorId_NaoEncontrado() {
        Long id = 1L;
        when(clubeRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(DadoNaoEncontradoException.class, () -> clubeService.buscarClubePorId(id));
    }

    @Test
    void buscarTodosClubes_CasosDiferentes() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Clube> page = new PageImpl<>(Arrays.asList(new Clube()));
        // Todos filtros
        when(clubeRepository.findByNomeAndEstadoAndAtivo(anyString(), any(Estado.class), anyBoolean(), eq(pageable)))
                .thenReturn(page);
        Assertions.assertEquals(page, clubeService.buscarTodosClubes("Nome", Estado.SP, true, pageable));
        // Nome
        when(clubeRepository.findByNome(anyString(), eq(pageable))).thenReturn(page);
        Assertions.assertEquals(page, clubeService.buscarTodosClubes("Nome", null, null, pageable));
        // Estado
        when(clubeRepository.findByEstado(any(Estado.class), eq(pageable))).thenReturn(page);
        Assertions.assertEquals(page, clubeService.buscarTodosClubes(null, Estado.SP, null, pageable));
        // Ativo
        when(clubeRepository.findByAtivo(anyBoolean(), eq(pageable))).thenReturn(page);
        Assertions.assertEquals(page, clubeService.buscarTodosClubes(null, null, true, pageable));
        // Nenhum filtro
        when(clubeRepository.findAll(pageable)).thenReturn(page);
        Assertions.assertEquals(page, clubeService.buscarTodosClubes(null, null, null, pageable));
    }

    @Test
    void deletarClube_Sucesso() {
        Long id = 1L;
        Clube clube = new Clube();
        when(clubeRepository.existsById(id)).thenReturn(true);
        when(clubeRepository.findById(id)).thenReturn(Optional.of(clube));
        when(clubeRepository.save(any(Clube.class))).thenReturn(clube);

        clubeService.deletarClube(id);
        verify(clubeRepository, times(1)).save(clube);
    }

    @Test
    void deletarClube_ClubeNaoEncontrado() {
        Long id = 1L;
        when(clubeRepository.existsById(id)).thenReturn(false);

        Assertions.assertThrows(DadoNaoEncontradoException.class, () -> clubeService.deletarClube(id));
    }
}

