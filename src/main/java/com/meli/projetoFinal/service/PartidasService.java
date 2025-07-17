package com.meli.projetoFinal.service;

import com.meli.projetoFinal.exception.ConflitoDeDadosException;
import com.meli.projetoFinal.exception.DadoNaoEncontradoException;
import com.meli.projetoFinal.exception.DadosInvalidosException;
import com.meli.projetoFinal.dto.*;
import com.meli.projetoFinal.mapper.PartidasMapper;
import com.meli.projetoFinal.model.Clube;
import com.meli.projetoFinal.model.Estadio;
import com.meli.projetoFinal.model.Partidas;
import com.meli.projetoFinal.repository.ClubeRepository;
import com.meli.projetoFinal.repository.EstadioRepository;
import com.meli.projetoFinal.repository.PartidasRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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


    private  final PartidasRepository partidasRepository;

    private final ClubeRepository clubeRepository;

    private final EstadioRepository estadioRepository;

    public PartidasService(PartidasRepository partidasRepository, ClubeRepository clubeRepository, EstadioRepository estadioRepository) {
        this.partidasRepository = partidasRepository;
        this.clubeRepository = clubeRepository;
        this.estadioRepository = estadioRepository;
    }


    @Transactional
    public Partidas cadastrarPartida(PartidasDTO dto) {
        buscarEValidarClubes clubes = getBuscarEValidarClubes(dto);

        if (!clubes.clubeCasa.getAtivo() || !clubes.clubeVisitante.getAtivo()) {
            throw new ConflitoDeDadosException("Um dos clubes está inativo.");
        }
        if (dto.getDataPartida().isBefore(clubes.clubeCasa.getDataCriacao()) ||
                dto.getDataPartida().isBefore(clubes.clubeVisitante.getDataCriacao())) {
            throw new ConflitoDeDadosException("Data da partida não pode ser anterior à data de criação de um dos clubes.");
        }

        Estadio estadio = estadioRepository.findById(dto.getEstadioId())
                .orElseThrow(() -> new DadosInvalidosException("Estádio não encontrado."));

        LocalDate dataPartida = dto.getDataPartida();
        LocalDate min = dataPartida.minusDays(2);
        LocalDate max = dataPartida.plusDays(2);

        boolean clubeCasaConflito = partidasRepository.existsByClubeCasaAndDataPartidaBetween(clubes.clubeCasa, min, max);
        boolean clubeVisitanteConflito = partidasRepository.existsByClubeVisitanteAndDataPartidaBetween(clubes.clubeVisitante, min, max);

        if (clubeCasaConflito || clubeVisitanteConflito) {
            throw new ConflitoDeDadosException("Um dos clubes já possui outra partida marcada em menos de 48 horas.");
        }

        boolean estadioOcupado = partidasRepository.existsByEstadioAndDataPartida(estadio, dataPartida);
        if (estadioOcupado) {
            throw new ConflitoDeDadosException("O estádio já possui jogo marcado nesse dia.");
        }


        Partidas partida = new Partidas();
        partida.setClubeCasa(clubes.clubeCasa);
        partida.setClubeVisitante(clubes.clubeVisitante);
        partida.setEstadio(estadio);
        partida.setGolsCasa(dto.getGolsCasa());
        partida.setGolsVisitante(dto.getGolsVisitante());
        partida.setDataPartida(dto.getDataPartida());

        return partidasRepository.save(partida);
    }

    @Transactional
    public Partidas atualizarPartida(Long id, PartidasDTO dto) {
        Partidas partida = partidasRepository.findById(id)
                .orElseThrow(() -> new DadoNaoEncontradoException("Partida não encontrada."));

        buscarEValidarClubes clubes = getBuscarEValidarClubes(dto);

        if (!clubes.clubeCasa().getAtivo() || !clubes.clubeVisitante().getAtivo()) {
            throw new ConflitoDeDadosException("Um dos clubes está inativo.");
        }
        if (dto.getDataPartida().isBefore(clubes.clubeCasa().getDataCriacao()) ||
                dto.getDataPartida().isBefore(clubes.clubeVisitante().getDataCriacao())) {
            throw new ConflitoDeDadosException("Data da partida não pode ser anterior à data de criação de um dos clubes.");
        }

        Estadio estadio = estadioRepository.findById(dto.getEstadioId())
                .orElseThrow(() -> new DadosInvalidosException("Estádio não encontrado."));

        LocalDate dataPartida = dto.getDataPartida();
        LocalDate min = dataPartida.minusDays(2);
        LocalDate max = dataPartida.plusDays(2);

        boolean clubeCasaConflito = partidasRepository.existsByClubeCasaAndDataPartidaBetween(clubes.clubeCasa(), min, max);
        boolean clubeVisitanteConflito = partidasRepository.existsByClubeVisitanteAndDataPartidaBetween(clubes.clubeVisitante(), min, max);

        if (clubeCasaConflito || clubeVisitanteConflito) {
            throw new ConflitoDeDadosException("Um dos clubes já possui outra partida marcada em menos de 48 horas.");
        }

        boolean estadioOcupado = partidasRepository.existsByEstadioAndDataPartida(estadio, dataPartida);
        if (estadioOcupado) {
            throw new ConflitoDeDadosException("O estádio já possui jogo marcado nesse dia.");
        }

        partida.setClubeCasa(clubes.clubeCasa());

        return partidasRepository.save(partida);
    }

    public Partidas getPartida(Long id) {
        return partidasRepository.findById(id).orElseThrow(() -> new DadoNaoEncontradoException("Partida não encontrada."));
    }

    public Page<Partidas> getPartidas(Pageable pageable) {
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

        int vitorias = 0, empates = 0, derrotas = 0, golsFeitos = 0, golsSofridos = 0;

        for (Partidas partida : partidas) {
            boolean isCasa = partida.getClubeCasa().getId().equals(clubeId);
            int golsPro = isCasa ? partida.getGolsCasa() : partida.getGolsVisitante();
            int golsContra = isCasa ? partida.getGolsVisitante() : partida.getGolsCasa();

            golsFeitos += golsPro;
            golsSofridos += golsContra;

            if (golsPro > golsContra) vitorias++;
            else if (golsPro == golsContra) empates++;
            else derrotas++;
        }

        return new RetrospectoClubeDTO(clube.getId(), clube.getNome(), vitorias, empates, derrotas, golsFeitos, golsSofridos);
    }


    public List<RetrospectoAdversarioDTO> getRetrospectoContraAdversarios(Long clubeId) {
        Clube clube = getClube(clubeId);

        List<Partidas> todasPartidas = partidasRepository.findAll();
        List<Partidas> partidas = new ArrayList<>();
        for (int i = 0; i < todasPartidas.size(); i++) {
            Partidas p = todasPartidas.get(i);
            if ((p.getClubeCasa().getId().equals(clubeId)) || (p.getClubeVisitante().getId().equals(clubeId))) {
                partidas.add(p);
            }
        }

        Map<Long, RetrospectoAdversarioDTO> mapa = new HashMap<>();

        for (int i = 0; i < partidas.size(); i++) {
            Partidas partida = partidas.get(i);
            boolean isCasa;
            if (partida.getClubeCasa().getId().equals(clubeId)) {
                isCasa = true;
            } else {
                isCasa = false;
            }

            Clube adversario;
            if (isCasa) {
                adversario = partida.getClubeVisitante();
            } else {
                adversario = partida.getClubeCasa();
            }
            Long adversarioId = adversario.getId();

            RetrospectoAdversarioDTO dto;
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

            int golsPro, golsContra;
            if (isCasa) {
                golsPro = partida.getGolsCasa();
                golsContra = partida.getGolsVisitante();
            } else {
                golsPro = partida.getGolsVisitante();
                golsContra = partida.getGolsCasa();
            }

            dto.setGolsFeitos(dto.getGolsFeitos() + golsPro);
            dto.setGolsSofridos(dto.getGolsSofridos() + golsContra);

            if (golsPro > golsContra) {
                dto.setVitorias(dto.getVitorias() + 1);
            } else if (golsPro == golsContra) {
                dto.setEmpates(dto.getEmpates() + 1);
            } else {
                dto.setDerrotas(dto.getDerrotas() + 1);
            }

            mapa.put(adversarioId, dto);
        }

        List<RetrospectoAdversarioDTO> resultado = new ArrayList<>();
        for (RetrospectoAdversarioDTO dto : mapa.values()) {
            resultado.add(dto);
        }
        return resultado;
    }


    public ConfrontoDiretoDTO getRetrospectoConfronto(Long clubeAId, Long clubeBId) {
        Clube clubeA = clubeRepository.findById(clubeAId)
                .orElseThrow(() -> new DadoNaoEncontradoException("Clube A não encontrado"));
        Clube clubeB = clubeRepository.findById(clubeBId)
                .orElseThrow(() -> new DadoNaoEncontradoException("Clube B não encontrado"));

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

        int vitoriasA = 0, empatesA = 0, derrotasA = 0, golsFeitosA = 0, golsSofridosA = 0;
        int vitoriasB = 0, empatesB = 0, derrotasB = 0, golsFeitosB = 0, golsSofridosB = 0;

        for (Partidas partida : partidas) {
            boolean aCasa = partida.getClubeCasa().getId().equals(clubeAId);
            int golsA = aCasa ? partida.getGolsCasa() : partida.getGolsVisitante();
            int golsB = aCasa ? partida.getGolsVisitante() : partida.getGolsCasa();

            golsFeitosA += golsA;
            golsSofridosA += golsB;
            golsFeitosB += golsB;
            golsSofridosB += golsA;

            if (golsA > golsB) {
                vitoriasA++;
                derrotasB++;
            } else if (golsA == golsB) {
                empatesA++;
                empatesB++;
            } else {
                derrotasA++;
                vitoriasB++;
            }
        }

        RetrospectoClubeDTO retroA = new RetrospectoClubeDTO();
        retroA.setClubeId(clubeA.getId());
        retroA.setClubeNome(clubeA.getNome());
        retroA.setVitorias(vitoriasA);
        retroA.setEmpates(empatesA);
        retroA.setDerrotas(derrotasA);
        retroA.setGolsFeitos(golsFeitosA);
        retroA.setGolsSofridos(golsSofridosA);

        RetrospectoClubeDTO retroB = new RetrospectoClubeDTO();
        retroB.setClubeId(clubeB.getId());
        retroB.setClubeNome(clubeB.getNome());
        retroB.setVitorias(vitoriasB);
        retroB.setEmpates(empatesB);
        retroB.setDerrotas(derrotasB);
        retroB.setGolsFeitos(golsFeitosB);
        retroB.setGolsSofridos(golsSofridosB);

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

        if (ranking.isEmpty()) {
            throw new DadoNaoEncontradoException("Nenhum clube encontrado para o ranking");
        }

        return ranking;
    }

    private Clube getClube(Long clubeId) {
        return clubeRepository.findById(clubeId)
                .orElseThrow(() -> new DadoNaoEncontradoException("Clube não encontrado"));
    }

    private record buscarEValidarClubes(Clube clubeCasa, Clube clubeVisitante) {

    }

    private buscarEValidarClubes getBuscarEValidarClubes(PartidasDTO dto) {
        Clube clubeCasa = clubeRepository.findById(dto.getClubeCasa())
                .orElseThrow(() -> new DadoNaoEncontradoException("Clube casa não encontrado."));
        Clube clubeVisitante = clubeRepository.findById(dto.getClubeVisitante())
                .orElseThrow(() -> new DadoNaoEncontradoException("Clube visitante não encontrado."));
        return new buscarEValidarClubes(clubeCasa, clubeVisitante);
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
}
