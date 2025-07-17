package com.meli.projetoFinal.service;

import com.meli.projetoFinal.Exception.ConflitoDeDadosException;
import com.meli.projetoFinal.Exception.DadoNaoEncontradoException;
import com.meli.projetoFinal.dto.PartidasDTO;
import com.meli.projetoFinal.model.Clube;
import com.meli.projetoFinal.model.Estadio;
import com.meli.projetoFinal.model.Partidas;
import com.meli.projetoFinal.repository.ClubeRepository;
import com.meli.projetoFinal.repository.EstadioRepository;
import com.meli.projetoFinal.repository.PartidasRepository;
import com.meli.projetoFinal.templates.PartidasTemplate;
import jakarta.servlet.http.Part;
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
import static org.mockito.Mockito.*;

class PartidasServiceTest {

    @Mock
    private PartidasRepository partidasRepository;

    @Mock
    private ClubeRepository clubeRepository;

    @Mock
    private EstadioRepository estadioRepository;

    @InjectMocks
    private PartidasService partidasService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void cadastrarPartida_Sucesso() {
        Clube clubeCasa = PartidasTemplate.clubeCasa();
        Clube clubeVisitante = PartidasTemplate.clubeVisitante();
        when(partidasRepository.existsByEstadioAndDataPartida(any(), any())).thenReturn(false);
        when(partidasRepository.existsByClubeCasaAndDataPartidaBetween(any(), any(), any())).thenReturn(false);
        when(partidasRepository.existsByClubeVisitanteAndDataPartidaBetween(any(), any(), any())).thenReturn(false);

        when(partidasRepository.save(any())).thenReturn(new Partidas());

        when(clubeRepository.findById(clubeCasa.getId())).thenReturn(Optional.of(clubeCasa));
        when(clubeRepository.findById(clubeVisitante.getId())).thenReturn(Optional.of(clubeVisitante));
        when(estadioRepository.findById(any())).thenReturn(Optional.of(PartidasTemplate.estadio()));
        Partidas result = partidasService.cadastrarPartida(PartidasTemplate.payloadDeSucesso());

        Assertions.assertNotNull(result);
        verify(partidasRepository, times(1)).save(any(Partidas.class));
    }

    @Test
    void cadastrarPartidaComTimeVisitanteInativo() {
        Clube clubeCasa = PartidasTemplate.clubeCasa();
        Clube clubeVisitante = PartidasTemplate.clubeVisitanteInativo();
        when(partidasRepository.existsByEstadioAndDataPartida(any(), any())).thenReturn(false);
        when(partidasRepository.existsByClubeCasaAndDataPartidaBetween(any(), any(), any())).thenReturn(false);
        when(partidasRepository.existsByClubeVisitanteAndDataPartidaBetween(any(), any(), any())).thenReturn(false);

        when(partidasRepository.save(any())).thenReturn(new Partidas());

        when(clubeRepository.findById(clubeCasa.getId())).thenReturn(Optional.of(clubeCasa));
        when(clubeRepository.findById(clubeVisitante.getId())).thenReturn(Optional.of(clubeVisitante));
        when(estadioRepository.findById(any())).thenReturn(Optional.of(PartidasTemplate.estadio()));

        Assertions.assertThrows(ConflitoDeDadosException.class, () -> {
            partidasService.cadastrarPartida(PartidasTemplate.payloadDeSucesso());
        });
    }

    @Test
    void cadastrarPartidaComTimeCasanteInativo() {
        Clube clubeCasa = PartidasTemplate.clubeCasaInativo();
        Clube clubeVisitante = PartidasTemplate.clubeVisitante();
        when(partidasRepository.existsByEstadioAndDataPartida(any(), any())).thenReturn(false);
        when(partidasRepository.existsByClubeCasaAndDataPartidaBetween(any(), any(), any())).thenReturn(false);
        when(partidasRepository.existsByClubeVisitanteAndDataPartidaBetween(any(), any(), any())).thenReturn(false);

        when(partidasRepository.save(any())).thenReturn(new Partidas());

        when(clubeRepository.findById(clubeCasa.getId())).thenReturn(Optional.of(clubeCasa));
        when(clubeRepository.findById(clubeVisitante.getId())).thenReturn(Optional.of(clubeVisitante));
        when(estadioRepository.findById(any())).thenReturn(Optional.of(PartidasTemplate.estadio()));

        Assertions.assertThrows(ConflitoDeDadosException.class, () -> {
            partidasService.cadastrarPartida(PartidasTemplate.payloadDeSucesso());
        });

    }

    @Test
    void cadastrarPartidaComTimeCasaComPartidaDentroDeDoisDias() {
        Clube clubeCasa = PartidasTemplate.clubeCasa();
        Clube clubeVisitante = PartidasTemplate.clubeVisitante();
        when(partidasRepository.existsByEstadioAndDataPartida(any(), any())).thenReturn(false);
        when(partidasRepository.existsByClubeCasaAndDataPartidaBetween(any(), any(), any())).thenReturn(true);
        when(partidasRepository.existsByClubeVisitanteAndDataPartidaBetween(any(), any(), any())).thenReturn(false);

        when(partidasRepository.save(any())).thenReturn(new Partidas());

        when(clubeRepository.findById(clubeCasa.getId())).thenReturn(Optional.of(clubeCasa));
        when(clubeRepository.findById(clubeVisitante.getId())).thenReturn(Optional.of(clubeVisitante));
        when(estadioRepository.findById(any())).thenReturn(Optional.of(PartidasTemplate.estadio()));

        Assertions.assertThrows(ConflitoDeDadosException.class, () -> {
            partidasService.cadastrarPartida(PartidasTemplate.payloadDeSucesso());
        });
    }

    @Test
    void cadastrarPartidaComTimeVisitanteComPartidaDentroDeDoisDias() {
        Clube clubeCasa = PartidasTemplate.clubeCasa();
        Clube clubeVisitante = PartidasTemplate.clubeVisitante();
        when(partidasRepository.existsByEstadioAndDataPartida(any(), any())).thenReturn(false);
        when(partidasRepository.existsByClubeCasaAndDataPartidaBetween(any(), any(), any())).thenReturn(false);
        when(partidasRepository.existsByClubeVisitanteAndDataPartidaBetween(any(), any(), any())).thenReturn(true);

        when(partidasRepository.save(any())).thenReturn(new Partidas());

        when(clubeRepository.findById(clubeCasa.getId())).thenReturn(Optional.of(clubeCasa));
        when(clubeRepository.findById(clubeVisitante.getId())).thenReturn(Optional.of(clubeVisitante));
        when(estadioRepository.findById(any())).thenReturn(Optional.of(PartidasTemplate.estadio()));

        Assertions.assertThrows(ConflitoDeDadosException.class, () -> {
            partidasService.cadastrarPartida(PartidasTemplate.payloadDeSucesso());
        });
    }

    @Test
    void cadastrarPartidaComEstadioComPartidaMarcada() {
        Clube clubeCasa = PartidasTemplate.clubeCasa();
        Clube clubeVisitante = PartidasTemplate.clubeVisitante();
        when(partidasRepository.existsByEstadioAndDataPartida(any(), any())).thenReturn(true);
        when(partidasRepository.existsByClubeCasaAndDataPartidaBetween(any(), any(), any())).thenReturn(false);
        when(partidasRepository.existsByClubeVisitanteAndDataPartidaBetween(any(), any(), any())).thenReturn(false);

        when(partidasRepository.save(any())).thenReturn(new Partidas());

        when(clubeRepository.findById(clubeCasa.getId())).thenReturn(Optional.of(clubeCasa));
        when(clubeRepository.findById(clubeVisitante.getId())).thenReturn(Optional.of(clubeVisitante));
        when(estadioRepository.findById(any())).thenReturn(Optional.of(PartidasTemplate.estadio()));

        Assertions.assertThrows(ConflitoDeDadosException.class, () -> {
            partidasService.cadastrarPartida(PartidasTemplate.payloadDeSucesso());
        });
    }

    @Test
    void cadastrarPartidaComDataAnteriorACriacaoDoClube() {
        Clube clubeCasa = PartidasTemplate.clubeCasa();
        Clube clubeVisitante = PartidasTemplate.clubeVisitante();
        when(partidasRepository.existsByEstadioAndDataPartida(any(), any())).thenReturn(false);
        when(partidasRepository.existsByClubeCasaAndDataPartidaBetween(any(), any(), any())).thenReturn(false);
        when(partidasRepository.existsByClubeVisitanteAndDataPartidaBetween(any(), any(), any())).thenReturn(false);

        when(partidasRepository.save(any())).thenReturn(new Partidas());

        when(clubeRepository.findById(clubeCasa.getId())).thenReturn(Optional.of(clubeCasa));
        when(clubeRepository.findById(clubeVisitante.getId())).thenReturn(Optional.of(clubeVisitante));
        when(estadioRepository.findById(any())).thenReturn(Optional.of(PartidasTemplate.estadio()));

        Assertions.assertThrows(ConflitoDeDadosException.class, () -> {
            partidasService.cadastrarPartida(PartidasTemplate.partidaComDataAnteriorACriacaoDoClube());
        });
    }

    @Test
    void buscarPartidaPorId_Sucesso() {
        Long id = 1L;
        Partidas partida = new Partidas();
        when(partidasRepository.findById(id)).thenReturn(Optional.of(partida));

        Partidas resultado = partidasService.getPartida(id);

        Assertions.assertNotNull(resultado);
    }

    @Test
    void buscarPartidaPorId_Erro() {
        Long id = 1L;
        when(partidasRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(DadoNaoEncontradoException.class, () -> {
            partidasService.getPartida(id);
        });
    }

    @Test
    void  buscarPartidas(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<Partidas> page = new PageImpl<>(Arrays.asList(new Partidas()));
        when(partidasRepository.findAll(pageable)).thenReturn(page);
    }

    @Test
    void deletePartida_Sucesso() {
        Long id = 1L;
        when(partidasRepository.existsById(id)).thenReturn(true);

        partidasService.deletePartida(id);

        verify(partidasRepository, times(1)).deleteById(id);
    }

    @Test
    void deletePartida_PartidaNaoEncontrada() {
        Long id = 1L;
        when(partidasRepository.existsById(id)).thenReturn(false);

        Assertions.assertThrows(DadoNaoEncontradoException.class, () -> {
            partidasService.deletePartida(id);
        });
    }

    @Test
    void getPartidas_RetornaPaginaComPartidas() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Partidas> page = new PageImpl<>(Arrays.asList(new Partidas(), new Partidas()));
        when(partidasRepository.findAll(pageable)).thenReturn(page);

        Page<Partidas> resultado = partidasService.getPartidas(pageable);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(2, resultado.getContent().size());
        verify(partidasRepository, times(1)).findAll(pageable);
    }

    @Test
    void getPartidas_RetornaPaginaVaziaQuandoNaoHaPartidas() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Partidas> page = new PageImpl<>(Arrays.asList());
        when(partidasRepository.findAll(pageable)).thenReturn(page);

        Page<Partidas> resultado = partidasService.getPartidas(pageable);

        Assertions.assertNotNull(resultado);
        Assertions.assertTrue(resultado.getContent().isEmpty());
        verify(partidasRepository, times(1)).findAll(pageable);
    }


    @Test
    void atualizarPartida_Sucesso() {
        Long id = 1L;
        Partidas partida = new Partidas();
        Clube clubeCasa = PartidasTemplate.clubeCasa();
        Clube clubeVisitante = PartidasTemplate.clubeVisitante();
        Estadio estadio = PartidasTemplate.estadio();

        when(partidasRepository.findById(id)).thenReturn(Optional.of(partida));
        when(clubeRepository.findById(clubeCasa.getId())).thenReturn(Optional.of(clubeCasa));
        when(clubeRepository.findById(clubeVisitante.getId())).thenReturn(Optional.of(clubeVisitante));
        when(estadioRepository.findById(estadio.getId())).thenReturn(Optional.of(estadio));
        when(partidasRepository.existsByClubeCasaAndDataPartidaBetween(any(), any(), any())).thenReturn(false);
        when(partidasRepository.existsByClubeVisitanteAndDataPartidaBetween(any(), any(), any())).thenReturn(false);
        when(partidasRepository.existsByEstadioAndDataPartida(any(), any())).thenReturn(false);
        when(partidasRepository.save(any())).thenReturn(new Partidas());

        PartidasDTO dto = PartidasTemplate.payloadDeSucesso();
        Partidas resultado = partidasService.atualizarPartida(id, dto);

        Assertions.assertNotNull(resultado);
        verify(partidasRepository, times(1)).save(any(Partidas.class));

    }

    @Test
    void atualizarPartidaClubeCasaInativo() {
        Long id = 1L;
        Partidas partida = new Partidas();
        Clube clubeCasa = PartidasTemplate.clubeCasaInativo();
        Clube clubeVisitante = PartidasTemplate.clubeVisitante();
        Estadio estadio = PartidasTemplate.estadio();

        when(partidasRepository.findById(id)).thenReturn(Optional.of(partida));
        when(clubeRepository.findById(clubeCasa.getId())).thenReturn(Optional.of(clubeCasa));
        when(clubeRepository.findById(clubeVisitante.getId())).thenReturn(Optional.of(clubeVisitante));
        when(estadioRepository.findById(estadio.getId())).thenReturn(Optional.of(estadio));
        when(partidasRepository.existsByClubeCasaAndDataPartidaBetween(any(), any(), any())).thenReturn(false);
        when(partidasRepository.existsByClubeVisitanteAndDataPartidaBetween(any(), any(), any())).thenReturn(false);
        when(partidasRepository.existsByEstadioAndDataPartida(any(), any())).thenReturn(false);
        when(partidasRepository.save(any())).thenReturn(new Partidas());

        PartidasDTO dto = PartidasTemplate.payloadDeSucesso();


        Assertions.assertThrows(ConflitoDeDadosException.class, () -> {
            partidasService.atualizarPartida(id,dto);
        });

    }

    @Test
    void atualizarPartidaClubeVisitanteInativo() {
        Long id = 1L;
        Partidas partida = new Partidas();
        Clube clubeCasa = PartidasTemplate.clubeCasa();
        Clube clubeVisitante = PartidasTemplate.clubeVisitanteInativo();
        Estadio estadio = PartidasTemplate.estadio();

        when(partidasRepository.findById(id)).thenReturn(Optional.of(partida));
        when(clubeRepository.findById(clubeCasa.getId())).thenReturn(Optional.of(clubeCasa));
        when(clubeRepository.findById(clubeVisitante.getId())).thenReturn(Optional.of(clubeVisitante));
        when(estadioRepository.findById(estadio.getId())).thenReturn(Optional.of(estadio));
        when(partidasRepository.existsByClubeCasaAndDataPartidaBetween(any(), any(), any())).thenReturn(false);
        when(partidasRepository.existsByClubeVisitanteAndDataPartidaBetween(any(), any(), any())).thenReturn(false);
        when(partidasRepository.existsByEstadioAndDataPartida(any(), any())).thenReturn(false);
        when(partidasRepository.save(any())).thenReturn(new Partidas());

        PartidasDTO dto = PartidasTemplate.payloadDeSucesso();


        Assertions.assertThrows(ConflitoDeDadosException.class, () -> {
            partidasService.atualizarPartida(id,dto);
        });

    }


    @Test
    void atualizarPartidaComEstadioOcupadoNaData() {
        Long id = 1L;
        Partidas partida = new Partidas();
        Clube clubeCasa = PartidasTemplate.clubeCasa();
        Clube clubeVisitante = PartidasTemplate.clubeVisitante();
        Estadio estadio = PartidasTemplate.estadio();

        when(partidasRepository.findById(id)).thenReturn(Optional.of(partida));
        when(clubeRepository.findById(clubeCasa.getId())).thenReturn(Optional.of(clubeCasa));
        when(clubeRepository.findById(clubeVisitante.getId())).thenReturn(Optional.of(clubeVisitante));
        when(estadioRepository.findById(estadio.getId())).thenReturn(Optional.of(estadio));
        when(partidasRepository.existsByClubeCasaAndDataPartidaBetween(any(), any(), any())).thenReturn(false);
        when(partidasRepository.existsByClubeVisitanteAndDataPartidaBetween(any(), any(), any())).thenReturn(false);
        when(partidasRepository.existsByEstadioAndDataPartida(any(), any())).thenReturn(true);
        when(partidasRepository.save(any())).thenReturn(new Partidas());

        PartidasDTO dto = PartidasTemplate.payloadDeSucesso();

        Assertions.assertThrows(ConflitoDeDadosException.class, () -> {
            partidasService.atualizarPartida(id, dto);
        });
    }

    @Test
    void atualizarPartidaComTimeCasaComJogoRecente() {
        Long id = 1L;
        Partidas partida = new Partidas();
        Clube clubeCasa = PartidasTemplate.clubeCasa();
        Clube clubeVisitante = PartidasTemplate.clubeVisitante();
        Estadio estadio = PartidasTemplate.estadio();

        when(partidasRepository.findById(id)).thenReturn(Optional.of(partida));
        when(clubeRepository.findById(clubeCasa.getId())).thenReturn(Optional.of(clubeCasa));
        when(clubeRepository.findById(clubeVisitante.getId())).thenReturn(Optional.of(clubeVisitante));
        when(estadioRepository.findById(estadio.getId())).thenReturn(Optional.of(estadio));
        when(partidasRepository.existsByClubeCasaAndDataPartidaBetween(any(), any(), any())).thenReturn(true);
        when(partidasRepository.existsByClubeVisitanteAndDataPartidaBetween(any(), any(), any())).thenReturn(false);
        when(partidasRepository.existsByEstadioAndDataPartida(any(), any())).thenReturn(false);
        when(partidasRepository.save(any())).thenReturn(new Partidas());

        PartidasDTO dto = PartidasTemplate.payloadDeSucesso();

        Assertions.assertThrows(ConflitoDeDadosException.class, () -> {
            partidasService.atualizarPartida(id, dto);
        });
    }

    @Test
    void atualizarPartidaComTimevisitanteComJogoRecente() {
        Long id = 1L;
        Partidas partida = new Partidas();
        Clube clubeCasa = PartidasTemplate.clubeCasa();
        Clube clubeVisitante = PartidasTemplate.clubeVisitante();
        Estadio estadio = PartidasTemplate.estadio();

        when(partidasRepository.findById(id)).thenReturn(Optional.of(partida));
        when(clubeRepository.findById(clubeCasa.getId())).thenReturn(Optional.of(clubeCasa));
        when(clubeRepository.findById(clubeVisitante.getId())).thenReturn(Optional.of(clubeVisitante));
        when(estadioRepository.findById(estadio.getId())).thenReturn(Optional.of(estadio));
        when(partidasRepository.existsByClubeCasaAndDataPartidaBetween(any(), any(), any())).thenReturn(false);
        when(partidasRepository.existsByClubeVisitanteAndDataPartidaBetween(any(), any(), any())).thenReturn(true);
        when(partidasRepository.existsByEstadioAndDataPartida(any(), any())).thenReturn(false);
        when(partidasRepository.save(any())).thenReturn(new Partidas());

        PartidasDTO dto = PartidasTemplate.payloadDeSucesso();

        Assertions.assertThrows(ConflitoDeDadosException.class, () -> {
            partidasService.atualizarPartida(id, dto);
        });
    }

    @Test
    void atualizarPartidaComDataAnteriorDeCriacaoDoClube() {
        Long id = 1L;
        Partidas partida = new Partidas();
        Clube clubeCasa = PartidasTemplate.clubeCasa();
        Clube clubeVisitante = PartidasTemplate.clubeVisitante();
        Estadio estadio = PartidasTemplate.estadio();

        when(partidasRepository.findById(id)).thenReturn(Optional.of(partida));
        when(clubeRepository.findById(clubeCasa.getId())).thenReturn(Optional.of(clubeCasa));
        when(clubeRepository.findById(clubeVisitante.getId())).thenReturn(Optional.of(clubeVisitante));
        when(estadioRepository.findById(estadio.getId())).thenReturn(Optional.of(estadio));
        when(partidasRepository.existsByClubeCasaAndDataPartidaBetween(any(), any(), any())).thenReturn(false);
        when(partidasRepository.existsByClubeVisitanteAndDataPartidaBetween(any(), any(), any())).thenReturn(false);
        when(partidasRepository.existsByEstadioAndDataPartida(any(), any())).thenReturn(false);
        when(partidasRepository.save(any())).thenReturn(new Partidas());

        Assertions.assertThrows(ConflitoDeDadosException.class, () -> {
            partidasService.atualizarPartida(id, PartidasTemplate.partidaComDataAnteriorACriacaoDoClube());
        });

    }

    @Test
    void atualizarPartidaComDataAnteriorACriacaoDoClubeVisitante() {
        Long id = 1L;
        Partidas partida = new Partidas();
        Clube clubeCasa = PartidasTemplate.clubeCasa();
        Clube clubeVisitante = PartidasTemplate.clubeVisitante();
        Estadio estadio = PartidasTemplate.estadio();

        when(partidasRepository.findById(id)).thenReturn(Optional.of(partida));
        when(clubeRepository.findById(clubeCasa.getId())).thenReturn(Optional.of(clubeCasa));
        when(clubeRepository.findById(clubeVisitante.getId())).thenReturn(Optional.of(clubeVisitante));
        when(estadioRepository.findById(estadio.getId())).thenReturn(Optional.of(estadio));
        when(partidasRepository.existsByClubeCasaAndDataPartidaBetween(any(), any(), any())).thenReturn(false);
        when(partidasRepository.existsByClubeVisitanteAndDataPartidaBetween(any(), any(), any())).thenReturn(false);
        when(partidasRepository.existsByEstadioAndDataPartida(any(), any())).thenReturn(false);

        PartidasDTO dto = PartidasTemplate.partidaComDataAnteriorACriacaoDoClubeVisitante();

        Assertions.assertThrows(ConflitoDeDadosException.class, () -> {
            partidasService.atualizarPartida(id, dto);
        });
    }
}
