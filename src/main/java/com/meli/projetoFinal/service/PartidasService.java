package com.meli.projetoFinal.service;

import com.meli.projetoFinal.dto.request.PartidasDTO;
import com.meli.projetoFinal.dto.response.*;
import com.meli.projetoFinal.exception.ConflitoDeDadosException;
import com.meli.projetoFinal.exception.DadoNaoEncontradoException;
import com.meli.projetoFinal.exception.DadosInvalidosException;
import com.meli.projetoFinal.model.Clube;
import com.meli.projetoFinal.model.Estadio;
import com.meli.projetoFinal.model.Partidas;
import com.meli.projetoFinal.repository.ClubeRepository;
import com.meli.projetoFinal.repository.EstadioRepository;
import com.meli.projetoFinal.repository.PartidasRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class PartidasService {


    private final PartidasRepository partidasRepository;

    private final ClubeRepository clubeRepository;

    private final EstadioRepository estadioRepository;

    public PartidasService(PartidasRepository partidasRepository, ClubeRepository clubeRepository, EstadioRepository estadioRepository) {
        this.partidasRepository = partidasRepository;
        this.clubeRepository = clubeRepository;
        this.estadioRepository = estadioRepository;
    }


    @Transactional
    public Partidas cadastrarPartida(PartidasDTO dto) {
        buscarEValidarClubesEstadios clubes = getBuscarEValidarClubesEstadio(dto);

       validarPartida(clubes.clubeCasa, clubes.clubeVisitante, dto.getDataPartida(), clubes.estadio);

        Partidas partida = new Partidas();
        partida.setClubeCasa(clubes.clubeCasa);
        partida.setClubeVisitante(clubes.clubeVisitante);
        partida.setEstadio(clubes.estadio);
        partida.setGolsCasa(dto.getGolsCasa());
        partida.setGolsVisitante(dto.getGolsVisitante());
        partida.setDataPartida(dto.getDataPartida());

        return partidasRepository.save(partida);
    }

    @Transactional
    public Partidas atualizarPartida(Long id, PartidasDTO dto) {
        Partidas partida = getPartida(id);
        buscarEValidarClubesEstadios clubes = getBuscarEValidarClubesEstadio(dto);

        validarPartida(clubes.clubeCasa, clubes.clubeVisitante, dto.getDataPartida(), clubes.estadio);

        partida.setClubeCasa(clubes.clubeCasa());
        partida.setClubeVisitante(clubes.clubeVisitante);
        partida.setEstadio(clubes.estadio);
        partida.setGolsCasa(dto.getGolsCasa());
        partida.setGolsVisitante(dto.getGolsVisitante());
        partida.setDataPartida(dto.getDataPartida());


        return partidasRepository.save(partida);
    }


    public Partidas getPartida(Long id) {
        return partidasRepository.findById(id).orElseThrow(() -> new DadoNaoEncontradoException("Partida não encontrada."));
    }

    public Page<Partidas> getPartidas(Pageable pageable, boolean goleadas) {
        if (goleadas) {
            return partidasRepository.buscarPartidasGoleadas(pageable,goleadas);
        }
        return partidasRepository.findAll(pageable);
    }

    public void deletePartida(Long id) {
        if (!partidasRepository.existsById(id)) {
            throw new DadoNaoEncontradoException("Partida não encontrado");
        }
        partidasRepository.deleteById(id);
    }

    public RetrospectoClubeDTO getRetrospectoClube(Long clubeId) {
        Clube clube = getClube(clubeId);

        List<Partidas> partidas = partidasRepository.findAll().stream()
                .filter(p -> p.getClubeCasa().getId().equals(clubeId) || p.getClubeVisitante().getId().equals(clubeId))
                .toList();

        return calcularRetrospectoClube(clube, clubeId, partidas);
    }



    public List<RetrospectoAdversarioDTO> getRetrospectoContraAdversarios(Long clubeId) {
        Map<Long, RetrospectoAdversarioDTO> mapa = new HashMap<>();

        VerificarSeExisteClube(clubeId);
        List<Partidas> partidas = buscarPartidasDoClube(clubeId);
        inserirDadosDoAdversario(clubeId, partidas, mapa);
        return getRetrospectoAdversarioDTOS(mapa);
    }


    public ConfrontoDiretoDTO getRetrospectoConfronto(Long clubeAId, Long clubeBId) {
        Clube clubeA = getClube(clubeAId);
        Clube clubeB = getClube(clubeBId);

        List<Partidas> partidas = filtrarConfrontosDiretos(clubeAId, clubeBId);

        RetrospectoClubeDTO retroA = calcularRetrospectoClube(clubeA, clubeAId, partidas);
        RetrospectoClubeDTO retroB = calcularRetrospectoClube(clubeB, clubeBId, partidas);

        ConfrontoDiretoDTO resp = new ConfrontoDiretoDTO();
        resp.setPartidas(converterParaConfrontoDTO(partidas));
        resp.setTime1(retroA);
        resp.setTime2(retroB);
        return resp;
    }

    public List<RankingDTO> getRanking(String criterio) {
        List<Partidas> partidas = partidasRepository.findAll();
        Map<Long, RankingDTO> rankingMap = new HashMap<>();

        for (Partidas partida : partidas) {
            atualizarRankingParaClubeCasa(partida, rankingMap);
            atualizarRankingParaClubeVisitante(partida, rankingMap);
        }
        List<RankingDTO> ranking = gerenciarTipoDeCriterioRaking(criterio, rankingMap);
        if (ranking.isEmpty()) {
            throw new DadoNaoEncontradoException("Nenhum clube encontrado para o ranking");
        }

        return ranking;
    }



    private void atualizarRankingParaClubeCasa(Partidas partida, Map<Long, RankingDTO> rankingMap) {
        Long idCasa = partida.getClubeCasa().getId();
        RankingDTO casa = rankingMap.getOrDefault(idCasa, new RankingDTO(idCasa, partida.getClubeCasa().getNome(), 0, 0, 0, 0));
        casa.setGols(casa.getGols() + partida.getGolsCasa());
        casa.setJogos(casa.getJogos() + 1);
        if (partida.getGolsCasa() > partida.getGolsVisitante()) {
            casa.setVitorias(casa.getVitorias() + 1);
            casa.setPontos(casa.getPontos() + 3);
        } else if (Objects.equals(partida.getGolsCasa(), partida.getGolsVisitante())) {
            casa.setPontos(casa.getPontos() + 1);
        }
        rankingMap.put(idCasa, casa);
    }

    private void atualizarRankingParaClubeVisitante(Partidas partida, Map<Long, RankingDTO> rankingMap) {
        Long idVisitante = partida.getClubeVisitante().getId();
        RankingDTO visitante = rankingMap.getOrDefault(idVisitante, new RankingDTO(idVisitante, partida.getClubeVisitante().getNome(), 0, 0, 0, 0));
        visitante.setGols(visitante.getGols() + partida.getGolsVisitante());
        visitante.setJogos(visitante.getJogos() + 1);
        if (partida.getGolsVisitante() > partida.getGolsCasa()) {
            visitante.setVitorias(visitante.getVitorias() + 1);
            visitante.setPontos(visitante.getPontos() + 3);
        } else if (Objects.equals(partida.getGolsCasa(), partida.getGolsVisitante())) {
            visitante.setPontos(visitante.getPontos() + 1);
        }
        rankingMap.put(idVisitante, visitante);
    }

    private static List<RankingDTO> gerenciarTipoDeCriterioRaking(String criterio, Map<Long, RankingDTO> rankingMap) {
        List<RankingDTO> ranking = new ArrayList<>(rankingMap.values());

        switch (criterio) {
            case "pontos":
                ranking.removeIf(r -> r.getPontos() <= 0);
                ranking.sort((a, b) -> Integer.compare(b.getPontos(), a.getPontos()));
                break;
            case "gols":
                ranking.removeIf(r -> r.getGols() <= 0);
                ranking.sort((a, b) -> Integer.compare(b.getGols(), a.getGols()));
                break;
            case "vitorias":
                ranking.removeIf(r -> r.getVitorias() <= 0);
                ranking.sort((a, b) -> Integer.compare(b.getVitorias(), a.getVitorias()));
                break;
            case "jogos":
                ranking.removeIf(r -> r.getJogos() <= 0);
                ranking.sort((a, b) -> Integer.compare(b.getJogos(), a.getJogos()));
                break;
            default:
                throw new IllegalArgumentException("Critério inválido");
        }
        return ranking;
    }

    private List<Partidas> filtrarConfrontosDiretos(Long clubeAId, Long clubeBId) {
        List<Partidas> todasPartidas = partidasRepository.findAll();
        List<Partidas> partidas = new ArrayList<>();
        for (Partidas p : todasPartidas) {
            boolean confrontoDireto =
                    (p.getClubeCasa().getId().equals(clubeAId) && p.getClubeVisitante().getId().equals(clubeBId)) ||
                            (p.getClubeCasa().getId().equals(clubeBId) && p.getClubeVisitante().getId().equals(clubeAId));
            if (confrontoDireto) {
                partidas.add(p);
            }
        }
        return partidas;
    }


    private RetrospectoClubeDTO calcularRetrospectoClube(Clube clube, Long clubeId, List<Partidas> partidas) {
        int vitorias = 0, empates = 0, derrotas = 0, golsFeitos = 0, golsSofridos = 0;

        for (Partidas partida : partidas) {
            boolean timeCasa = verificarTimeCasa(clubeId, partida);
            int golsPro = timeCasa ? partida.getGolsCasa() : partida.getGolsVisitante();
            int golsContra = timeCasa ? partida.getGolsVisitante() : partida.getGolsCasa();

            golsFeitos += golsPro;
            golsSofridos += golsContra;

            if (golsPro > golsContra) vitorias++;
            else if (golsPro == golsContra) empates++;
            else derrotas++;
        }

        return new RetrospectoClubeDTO(clube.getId(), clube.getNome(), vitorias, empates, derrotas, golsFeitos, golsSofridos);
    }

    private static int[] calcularGols(Partidas partida, Long clubeAId) {
        boolean ehTimeCasa = partida.getClubeCasa().getId().equals(clubeAId);
        int golsA = ehTimeCasa ? partida.getGolsCasa() : partida.getGolsVisitante();
        int golsB = ehTimeCasa ? partida.getGolsVisitante() : partida.getGolsCasa();
        return new int[]{golsA, golsB};
    }

    private Clube getClube(Long clubeId) {
        return clubeRepository.findById(clubeId)
                .orElseThrow(() -> new DadoNaoEncontradoException("Clube não encontrado"));
    }

    private boolean VerificarSeExisteClube(Long clubeId) {
        boolean existe = clubeRepository.existsById(clubeId);
        if (!existe) {
            throw new DadoNaoEncontradoException("Clube não encontrado");
        }
        return true;
    }

    private record buscarEValidarClubesEstadios(Clube clubeCasa, Clube clubeVisitante, Estadio estadio) {

    }

    private void validarPartida(Clube clubeCasa, Clube clubeVisitante, LocalDate dataPartida, Estadio estadio) {
        if (!clubeCasa.getAtivo() || !clubeVisitante.getAtivo()) {
            throw new ConflitoDeDadosException("Um dos clubes está inativo.");
        }
        if (dataPartida.isBefore(clubeCasa.getDataCriacao()) ||
                dataPartida.isBefore(clubeVisitante.getDataCriacao())) {
            throw new ConflitoDeDadosException("Data da partida não pode ser anterior à data de criação de um dos clubes.");
        }

        LocalDate min = dataPartida.minusDays(2);
        LocalDate max = dataPartida.plusDays(2);

        boolean clubeCasaConflito = partidasRepository.existsByClubeCasaAndDataPartidaBetween(clubeCasa, min, max);
        boolean clubeVisitanteConflito = partidasRepository.existsByClubeVisitanteAndDataPartidaBetween(clubeVisitante, min, max);

        if (clubeCasaConflito || clubeVisitanteConflito) {
            throw new ConflitoDeDadosException("Um dos clubes já possui outra partida marcada em menos de 48 horas.");
        }

        boolean estadioOcupado = partidasRepository.existsByEstadioAndDataPartida(estadio, dataPartida);
        if (estadioOcupado) {
            throw new ConflitoDeDadosException("O estádio já possui jogo marcado nesse dia.");
        }
    }

    private buscarEValidarClubesEstadios getBuscarEValidarClubesEstadio(PartidasDTO dto) {
        Clube clubeCasa = clubeRepository.findById(dto.getClubeCasa())
                .orElseThrow(() -> new DadoNaoEncontradoException("Clube casa não encontrado."));
        Clube clubeVisitante = clubeRepository.findById(dto.getClubeVisitante())
                .orElseThrow(() -> new DadoNaoEncontradoException("Clube visitante não encontrado."));
        Estadio estadio = estadioRepository.findById(dto.getEstadioId())
                .orElseThrow(() -> new DadosInvalidosException("Estádio não encontrado."));
        return new buscarEValidarClubesEstadios(clubeCasa, clubeVisitante, estadio);
    }

    private List<ConfrontoDiretoResponseDTO> converterParaConfrontoDTO(List<Partidas> partidas) {
        return partidas.stream().map(p -> {
            return new ConfrontoDiretoResponseDTO(
                    p.getId(),
                    p.getClubeCasa().getNome(),
                    p.getClubeVisitante().getNome(),
                    p.getEstadio().getNome(),
                    p.getGolsCasa(),
                    p.getGolsVisitante(),
                    p.getDataPartida().toString()
            );
        }).toList();
    }

    private List<Partidas> buscarPartidasDoClube(Long clubeId) {
        List<Partidas> todasPartidas = partidasRepository.findAll();
        List<Partidas> partidas = new ArrayList<>();
        for (Partidas p : todasPartidas) {
            if ((p.getClubeCasa().getId().equals(clubeId)) || (p.getClubeVisitante().getId().equals(clubeId))) {
                partidas.add(p);
            }
        }
        return partidas;
    }

    private static List<RetrospectoAdversarioDTO> getRetrospectoAdversarioDTOS(Map<Long, RetrospectoAdversarioDTO> mapa) {
        return new ArrayList<>(mapa.values());
    }

    private void inserirDadosDoAdversario(Long clubeId, List<Partidas> partidas, Map<Long, RetrospectoAdversarioDTO> mapa) {
        for (int i = 0; i < partidas.size(); i++) {
            Partidas partida = partidas.get(i);
            boolean timeCasa = verificarTimeCasa(clubeId, partida);

            Clube adversario = getIndicarAdversario(timeCasa, partida);
            RetrospectoAdversarioDTO dto = getRetrospectoAdversarioDTO(adversario, mapa);
            identificarGols(timeCasa, partida, dto);
            mapa.put(adversario.getId(), dto);
        }
    }

    private static RetrospectoAdversarioDTO getRetrospectoAdversarioDTO(Clube adversario, Map<Long, RetrospectoAdversarioDTO> mapa) {
        RetrospectoAdversarioDTO dto;
        Long adversarioId = adversario.getId();
        if (mapa.containsKey(adversarioId)) {
            dto = mapa.get(adversarioId);
        } else {
            dto = new RetrospectoAdversarioDTO();
            dto.setAdversarioId(adversarioId);
            dto.setAdversarioNome(adversario.getNome());
            dto.setGolsFeitos(0);
            dto.setGolsSofridos(0);
            dto.setVitorias(0);
            dto.setEmpates(0);
            dto.setDerrotas(0);
        }
        return dto;
    }

    private static void identificarGols(boolean timeCasa, Partidas partida, RetrospectoAdversarioDTO dto) {
        int golsPro, golsContra;
        if (timeCasa) {
            golsPro = partida.getGolsCasa();
            golsContra = partida.getGolsVisitante();
        } else {
            golsPro = partida.getGolsVisitante();
            golsContra = partida.getGolsCasa();
        }

        dto.setGolsFeitos(dto.getGolsFeitos() + golsPro);
        dto.setGolsSofridos(dto.getGolsSofridos() + golsContra);

        VerificarResultadoDoJogo(golsPro, golsContra, dto);
    }

    private static void VerificarResultadoDoJogo(int golsPro, int golsContra, RetrospectoAdversarioDTO dto) {
        if (golsPro > golsContra) {
            dto.setVitorias(dto.getVitorias() + 1);
        } else if (golsPro == golsContra) {
            dto.setEmpates(dto.getEmpates() + 1);
        } else {
            dto.setDerrotas(dto.getDerrotas() + 1);
        }
    }

    private Clube getIndicarAdversario(boolean timeCasa, Partidas partida) {
        Clube adversario;
        if (timeCasa) {
            adversario = partida.getClubeVisitante();
        } else {
            adversario = partida.getClubeCasa();
        }
        if (adversario == null) {
            throw new DadoNaoEncontradoException("Adversário nao encontrado");
        }
        return adversario;
    }

    private static boolean verificarTimeCasa(Long clubeId, Partidas partida) {
        return partida.getClubeCasa().getId().equals(clubeId);
    }

    private ConfrontoDiretoDTO gerarRetrospectosDosClubes(Clube clubeA, int vitoriasA, int empatesA, int derrotasA, int golsFeitosA, int golsSofridosA, Clube clubeB, int vitoriasB, int empatesB, int derrotasB, int golsFeitosB, int golsSofridosB, List<Partidas> partidas) {
        RetrospectoClubeDTO retroA = retrospectoClube(clubeA, vitoriasA, empatesA, derrotasA, golsFeitosA, golsSofridosA);
        RetrospectoClubeDTO retroB = retrospectoClube(clubeB, vitoriasB, empatesB, derrotasB, golsFeitosB, golsSofridosB);

        ConfrontoDiretoDTO resp = new ConfrontoDiretoDTO();
        resp.setPartidas(converterParaConfrontoDTO(partidas));
        resp.setTime1(retroA);
        resp.setTime2(retroB);
        return resp;
    }

    private RetrospectoClubeDTO retrospectoClube(Clube clubeA, int vitoriasA, int empatesA, int derrotasA, int golsFeitosA, int golsSofridosA) {
        RetrospectoClubeDTO retro = new RetrospectoClubeDTO();
        retro.setClubeId(clubeA.getId());
        retro.setClubeNome(clubeA.getNome());
        retro.setVitorias(vitoriasA);
        retro.setEmpates(empatesA);
        retro.setDerrotas(derrotasA);
        retro.setGolsFeitos(golsFeitosA);
        retro.setGolsSofridos(golsSofridosA);
        return retro;
    }
}
