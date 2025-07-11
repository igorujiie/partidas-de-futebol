package com.meli.projetoFinal.service;

import com.meli.projetoFinal.Exception.ConflitoDeDadosException;
import com.meli.projetoFinal.Exception.DadoNaoEncontradoException;
import com.meli.projetoFinal.Exception.DadosInvalidosException;
import com.meli.projetoFinal.dto.PartidasDTO;
import com.meli.projetoFinal.model.Clube;
import com.meli.projetoFinal.model.Estadio;
import com.meli.projetoFinal.model.Partidas;
import com.meli.projetoFinal.repository.ClubeRepository;
import com.meli.projetoFinal.repository.EstadioRepository;
import com.meli.projetoFinal.repository.PartidasRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
public class PartidasService {

    @Autowired
    private PartidasRepository partidasRepository;
    @Autowired
    private ClubeRepository clubeRepository;
    @Autowired
    private EstadioRepository estadioRepository;


    @Transactional
    public Partidas cadastrarPartida(PartidasDTO dto) {
        Clube clubeCasa = clubeRepository.findById(dto.getClubeCasa())
                .orElseThrow(() -> new DadoNaoEncontradoException("Clube casa não encontrado."));
        Clube clubeVisitante = clubeRepository.findById(dto.getClubeVisitante())
                .orElseThrow(() -> new ConflitoDeDadosException("Clube visitante não encontrado."));

        if (!clubeCasa.getAtivo() || !clubeVisitante.getAtivo()) {
            throw new ConflitoDeDadosException("Um dos clubes está inativo.");
        }
        if (dto.getDataPartida().isBefore(clubeCasa.getDataCriacao()) ||
                dto.getDataPartida().isBefore(clubeVisitante.getDataCriacao())) {
            throw new ConflitoDeDadosException("Data da partida não pode ser anterior à data de criação de um dos clubes.");
        }

        Estadio estadio = estadioRepository.findById(dto.getEstadioId())
                .orElseThrow(() -> new DadosInvalidosException("Estádio não encontrado."));

        LocalDate dataPartida = dto.getDataPartida();
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


        Partidas partida = new Partidas();
        partida.setClubeCasa(clubeCasa);
        partida.setClubeVisitante(clubeVisitante);
        partida.setEstadio(estadio);
        partida.setGolsCasa(dto.getGolsCasa());
        partida.setGolsVisitante(dto.getGolsVisitante());
        partida.setDataPartida(dto.getDataPartida());

        return partidasRepository.save(partida);
    }

    private PartidasDTO toDTO(Partidas partida) {
        PartidasDTO dto = new PartidasDTO();
        dto.setClubeCasa(partida.getClubeCasa().getId());
        dto.setClubeVisitante(partida.getClubeVisitante().getId());
        dto.setEstadioId(partida.getEstadio().getId());
        dto.setGolsCasa(partida.getGolsCasa());
        dto.setGolsVisitante(partida.getGolsVisitante());
        dto.setDataPartida(partida.getDataPartida());
        return dto;
    }

}