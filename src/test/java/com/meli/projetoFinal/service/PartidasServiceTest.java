package com.meli.projetoFinal.service;

import com.meli.projetoFinal.dto.request.PartidasDTO;
import com.meli.projetoFinal.dto.response.ConfrontoDiretoDTO;
import com.meli.projetoFinal.dto.response.RankingDTO;
import com.meli.projetoFinal.dto.response.RetrospectoAdversarioDTO;
import com.meli.projetoFinal.dto.response.RetrospectoClubeDTO;
import com.meli.projetoFinal.exception.ConflitoDeDadosException;
import com.meli.projetoFinal.exception.DadoNaoEncontradoException;
import com.meli.projetoFinal.model.Clube;
import com.meli.projetoFinal.model.Estadio;
import com.meli.projetoFinal.model.Partidas;
import com.meli.projetoFinal.repository.ClubeRepository;
import com.meli.projetoFinal.repository.EstadioRepository;
import com.meli.projetoFinal.repository.PartidasRepository;
import com.meli.projetoFinal.templates.PartidasTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
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
    void buscarPartidas() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Partidas> page = new PageImpl<>(List.of(new Partidas()));
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
        Pageable pageable = PageRequest.of(0, 10, Sort.by("dataPartida").descending());
        Page<Partidas> page = new PageImpl<>(Arrays.asList(new Partidas(), new Partidas()));
        boolean goleada = false;
        when(partidasRepository.findAll(pageable)).thenReturn(page);
        when(partidasRepository.buscarPartidasGoleadas(pageable,goleada)).thenReturn(page);

        Page<Partidas> resultado = partidasService.getPartidas(pageable,goleada);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(2, resultado.getContent().size());
        verify(partidasRepository, times(1)).findAll(pageable);
    }

    @Test
    void getPartidas_RetornaPaginaVaziaQuandoNaoHaPartidas() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Partidas> page = new PageImpl<>(List.of());
        boolean goleada = false;

        when(partidasRepository.findAll(pageable)).thenReturn(page);

        Page<Partidas> resultado = partidasService.getPartidas(pageable, goleada);

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
            partidasService.atualizarPartida(id, dto);
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
            partidasService.atualizarPartida(id, dto);
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
        when(estadioRepository.findById(any())).thenReturn(Optional.of(PartidasTemplate.estadio()));
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

    @Test
    void getRetrospectoClubeRetornaRetrospectoCorreto() {
        Long clubeId = 1L;
        Clube clube = new Clube();
        clube.setId(clubeId);
        clube.setNome("Clube A");

        Clube clubeVisitante1 = new Clube();
        clubeVisitante1.setId(2L);

        Clube clubeVisitante2 = new Clube();
        clubeVisitante2.setId(3L);

        Estadio estadio = new Estadio();
        estadio.setNome("Estadio A");
        estadio.setId(1L);

        Partidas partida1 = new Partidas();
        partida1.setClubeCasa(clube);
        partida1.setClubeVisitante(clubeVisitante1);
        partida1.setGolsCasa(2);
        partida1.setGolsVisitante(1);
        partida1.setEstadio(estadio);
        partida1.setDataPartida(LocalDate.now().minusDays(7));

        Partidas partida2 = new Partidas();
        partida2.setClubeCasa(clubeVisitante2);
        partida2.setClubeVisitante(clube);
        partida2.setGolsCasa(1);
        partida2.setGolsVisitante(1);
        partida2.setEstadio(estadio);
        partida2.setDataPartida(LocalDate.now().minusDays(1));

        when(clubeRepository.findById(clubeId)).thenReturn(Optional.of(clube));
        when(partidasRepository.findAll()).thenReturn(Arrays.asList(partida1, partida2));

        RetrospectoClubeDTO resultado = partidasService.getRetrospectoClube(clubeId);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1, resultado.getVitorias());
        Assertions.assertEquals(1, resultado.getEmpates());
        Assertions.assertEquals(0, resultado.getDerrotas());
        Assertions.assertEquals(3, resultado.getGolsFeitos());
        Assertions.assertEquals(2, resultado.getGolsSofridos());
    }

    @Test
    void getRetrospectoClubeClubeNaoEncontrado() {
        Long clubeId = 1L;
        when(clubeRepository.findById(clubeId)).thenReturn(Optional.empty());

        Assertions.assertThrows(DadoNaoEncontradoException.class, () -> {
            partidasService.getRetrospectoClube(clubeId);
        });
    }

    @Test
    void getRetrospectoContraAdversariosRetornaRetrospectoCorreto() {
        Long clubeId = 1L;
        Clube clube = new Clube();
        clube.setId(clubeId);
        clube.setNome("Clube A");

        Clube adversario = new Clube();
        adversario.setId(2L);
        adversario.setNome("Clube B");

        Estadio estadio = new Estadio();
        estadio.setNome("Estadio A");
        estadio.setId(1L);


        Partidas partida = new Partidas();
        partida.setClubeCasa(clube);
        partida.setClubeVisitante(adversario);
        partida.setGolsCasa(3);
        partida.setGolsVisitante(2);
        partida.setEstadio(estadio);
        partida.setDataPartida(LocalDate.now());

        when(clubeRepository.existsById(clubeId)).thenReturn(true);
        when(partidasRepository.findAll()).thenReturn(List.of(partida));

        List<RetrospectoAdversarioDTO> resultado = partidasService.getRetrospectoContraAdversarios(clubeId);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1, resultado.size());
        RetrospectoAdversarioDTO dto = resultado.getFirst();
        Assertions.assertEquals(adversario.getId(), dto.getAdversarioId());
        Assertions.assertEquals(3, dto.getGolsFeitos());
        Assertions.assertEquals(2, dto.getGolsSofridos());
        Assertions.assertEquals(1, dto.getVitorias());
        Assertions.assertEquals(0, dto.getEmpates());
        Assertions.assertEquals(0, dto.getDerrotas());
    }

    @Test
    void getRetrospectoContraAdversariosClubeNaoEncontrado() {
        Long clubeId = 1L;
        when(clubeRepository.existsById(clubeId)).thenReturn(false);

        Assertions.assertThrows(DadoNaoEncontradoException.class, () -> {
            partidasService.getRetrospectoContraAdversarios(clubeId);
        });
    }

    @Test
    void getRetrospectoConfrontoRetornaRetrospectoCorreto() {
        Long clubeAId = 1L;
        Long clubeBId = 2L;

        Clube clubeA = new Clube();
        clubeA.setId(clubeAId);
        clubeA.setNome("Clube A");

        Clube clubeB = new Clube();
        clubeB.setId(clubeBId);
        clubeB.setNome("Clube B");

        Estadio estadio = new Estadio();
        estadio.setNome("Estadio A");
        estadio.setId(1L);

        Partidas partida = new Partidas();
        partida.setClubeCasa(clubeA);
        partida.setClubeVisitante(clubeB);
        partida.setGolsCasa(2);
        partida.setGolsVisitante(1);
        partida.setEstadio(estadio);
        partida.setDataPartida(LocalDate.now());

        when(clubeRepository.findById(clubeAId)).thenReturn(Optional.of(clubeA));
        when(clubeRepository.findById(clubeBId)).thenReturn(Optional.of(clubeB));
        when(partidasRepository.findAll()).thenReturn(List.of(partida));

        ConfrontoDiretoDTO resultado = partidasService.getRetrospectoConfronto(clubeAId, clubeBId);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1, resultado.getTime1().getVitorias());
        Assertions.assertEquals(0, resultado.getTime1().getEmpates());
        Assertions.assertEquals(0, resultado.getTime1().getDerrotas());
        Assertions.assertEquals(2, resultado.getTime1().getGolsFeitos());
        Assertions.assertEquals(1, resultado.getTime1().getGolsSofridos());
    }

    @Test
    void getRetrospectoConfrontoClubeNaoEncontrado() {
        Long clubeAId = 1L;
        Long clubeBId = 2L;

        when(clubeRepository.findById(clubeAId)).thenReturn(Optional.empty());

        Assertions.assertThrows(DadoNaoEncontradoException.class, () -> {
            partidasService.getRetrospectoConfronto(clubeAId, clubeBId);
        });
    }



    @Test
    void getRetrospectoConfrontoRetornaEmpateParaAmbosOsClubes() {
        Long clubeAId = 1L;
        Long clubeBId = 2L;

        Clube clubeA = new Clube();
        clubeA.setId(clubeAId);
        clubeA.setNome("Clube A");

        Clube clubeB = new Clube();
        clubeB.setId(clubeBId);
        clubeB.setNome("Clube B");

        Estadio estadio = new Estadio();
        estadio.setId(1L);
        estadio.setNome("Estadio");

        Partidas partida = new Partidas();
        partida.setClubeCasa(clubeA);
        partida.setClubeVisitante(clubeB);
        partida.setGolsCasa(2);
        partida.setGolsVisitante(2);
        partida.setEstadio(estadio);
        partida.setDataPartida(LocalDate.now());

        when(clubeRepository.findById(clubeAId)).thenReturn(Optional.of(clubeA));
        when(clubeRepository.findById(clubeBId)).thenReturn(Optional.of(clubeB));
        when(partidasRepository.findAll()).thenReturn(List.of(partida));

        ConfrontoDiretoDTO resultado = partidasService.getRetrospectoConfronto(clubeAId, clubeBId);

        Assertions.assertEquals(1, resultado.getTime1().getEmpates());
        Assertions.assertEquals(1, resultado.getTime2().getEmpates());
        Assertions.assertEquals(0, resultado.getTime1().getVitorias());
        Assertions.assertEquals(0, resultado.getTime2().getVitorias());
    }

    @Test
    void getRetrospectoConfrontoClubeBTemVitoria() {
        Long clubeAId = 1L;
        Long clubeBId = 2L;

        Clube clubeA = new Clube();
        clubeA.setId(clubeAId);
        clubeA.setNome("Clube A");

        Clube clubeB = new Clube();
        clubeB.setId(clubeBId);
        clubeB.setNome("Clube B");

        Estadio estadio = new Estadio();
        estadio.setId(1L);
        estadio.setNome("Estadio");

        Partidas partida = new Partidas();
        partida.setClubeCasa(clubeA);
        partida.setClubeVisitante(clubeB);
        partida.setGolsCasa(1);
        partida.setGolsVisitante(3);
        partida.setEstadio(estadio);
        partida.setDataPartida(LocalDate.now());

        when(clubeRepository.findById(clubeAId)).thenReturn(Optional.of(clubeA));
        when(clubeRepository.findById(clubeBId)).thenReturn(Optional.of(clubeB));
        when(partidasRepository.findAll()).thenReturn(List.of(partida));

        ConfrontoDiretoDTO resultado = partidasService.getRetrospectoConfronto(clubeAId, clubeBId);

        Assertions.assertEquals(1, resultado.getTime2().getVitorias());
        Assertions.assertEquals(1, resultado.getTime1().getDerrotas());
        Assertions.assertEquals(0, resultado.getTime1().getVitorias());
        Assertions.assertEquals(0, resultado.getTime2().getDerrotas());
    }
    @Test
    void getRankingRetornaRankingPorVitorias() {
        Clube clube1 = new Clube();
        clube1.setId(1L);
        clube1.setNome("Clube 1");
        Clube clube2 = new Clube();
        clube2.setId(2L);
        clube2.setNome("Clube 2");

        Partidas partida = new Partidas();
        partida.setClubeCasa(clube1);
        partida.setClubeVisitante(clube2);
        partida.setGolsCasa(2);
        partida.setGolsVisitante(1);

        when(partidasRepository.findAll()).thenReturn(List.of(partida));

        List<RankingDTO> ranking = partidasService.getRanking("vitorias");

        Assertions.assertEquals(1, ranking.size());
        Assertions.assertEquals(clube1.getNome(), ranking.getFirst().getClubeNome());
        Assertions.assertEquals(1, ranking.getFirst().getVitorias());
    }

    @Test
    void getRankingRetornaRankingPorJogos() {
        Clube clube1 = new Clube();
        clube1.setId(1L);
        clube1.setNome("Clube 1");
        Clube clube2 = new Clube();
        clube2.setId(2L);
        clube2.setNome("Clube 2");

        Partidas partida = new Partidas();
        partida.setClubeCasa(clube1);
        partida.setClubeVisitante(clube2);
        partida.setGolsCasa(0);
        partida.setGolsVisitante(0);

        when(partidasRepository.findAll()).thenReturn(List.of(partida));

        List<RankingDTO> ranking = partidasService.getRanking("jogos");

        Assertions.assertEquals(2, ranking.size());
        Assertions.assertTrue(ranking.stream().allMatch(r -> r.getJogos() == 1));
    }

    @Test
    void getRankingLancaExcecaoParaCriterioInvalido() {
        Clube clube1 = new Clube();
        clube1.setId(1L);
        clube1.setNome("Clube 1");
        Clube clube2 = new Clube();
        clube2.setId(2L);
        clube2.setNome("Clube 2");

        Partidas partida = new Partidas();
        partida.setClubeCasa(clube1);
        partida.setClubeVisitante(clube2);
        partida.setGolsCasa(1);
        partida.setGolsVisitante(1);

        when(partidasRepository.findAll()).thenReturn(List.of(partida));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            partidasService.getRanking("qualquercoisa");
        });
    }

    @Test
    void getRankingLancaExcecaoQuandoRankingVazio() {
        Clube clube1 = new Clube();
        clube1.setId(1L);
        clube1.setNome("Clube 1");
        Clube clube2 = new Clube();
        clube2.setId(2L);
        clube2.setNome("Clube 2");

        Partidas partida = new Partidas();
        partida.setClubeCasa(clube1);
        partida.setClubeVisitante(clube2);
        partida.setGolsCasa(0);
        partida.setGolsVisitante(0);

        when(partidasRepository.findAll()).thenReturn(List.of(partida));

        Assertions.assertThrows(DadoNaoEncontradoException.class, () -> {
            partidasService.getRanking("gols");
        });
    }
}
