package com.meli.projetoFinal.service;

import com.meli.projetoFinal.Exception.DadoNaoEncontradoException;
import com.meli.projetoFinal.Exception.DadosInvalidosException;
import com.meli.projetoFinal.dto.PartidasDTO;
import com.meli.projetoFinal.model.Clube;
import com.meli.projetoFinal.model.Partidas;
import com.meli.projetoFinal.repository.ClubeRepository;
import com.meli.projetoFinal.repository.EstadioRepository;
import com.meli.projetoFinal.repository.PartidasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class PartidasService {

    @Autowired
    private PartidasRepository partidasRepository;
    @Autowired
    private ClubeRepository clubeRepository;
    @Autowired
    private EstadioRepository estadioRepository;

    public Partidas cadastrarPartida(PartidasDTO dto) {

        Clube clubeCasa = clubeRepository.findById(dto.getClubeCasaId())
                .orElseThrow(() -> new DadoNaoEncontradoException("Clube casa não encontrado"));
        Clube clubeVisitante = clubeRepository.findById(dto.getClubeVisitanteId())
                .orElseThrow(() -> new DadoNaoEncontradoException("Clube visitante não encontrado"));

        if(dto.getClubeCasaId() == null || dto.getClubeVisitanteId() == null ||
                dto.getEstadioId() == null || dto.getDataPartida() == null) {
            throw new DadosInvalidosException("Dados invalidos  ou incompletos");
        }

        if (dto.getDataPartida().isBefore(clubeCasa.getDataCriacao()) || dto.getDataPartida().isBefore(clubeVisitante.getDataCriacao())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data da partida não pode ser anterior à data de criação de um dos clubes envolvidos.");
        }
        Partidas partida = new Partidas();
        partida.setClubeCasa(clubeRepository.findById(dto.getClubeCasaId()).orElseThrow());
        partida.setClubeVisitante(clubeRepository.findById(dto.getClubeVisitanteId()).orElseThrow());
        partida.setEstadio(estadioRepository.findById(dto.getEstadioId()).orElseThrow());
        partida.setGolsCasa(dto.getGolsCasa());
        partida.setGolsVisitante(dto.getGolsVisitante());
        partida.setDataPartida(dto.getDataPartida());
        Partidas salva = partidasRepository.save(partida);
        return salva;
    }

  public List<PartidasDTO> listarPartidas() {
      List<Partidas> partidas = partidasRepository.findAll();
      List<PartidasDTO> dtos = new ArrayList<>();
      for (Partidas partida : partidas) {
          dtos.add(toDTO(partida));
      }
      return dtos;
  }

    public PartidasDTO buscarPorId(Long id) {
        Partidas partida = partidasRepository.findById(id).orElseThrow();
        return toDTO(partida);
    }

    private PartidasDTO toDTO(Partidas partida) {
        PartidasDTO dto = new PartidasDTO();
        dto.setClubeCasaId(partida.getClubeCasa().getId());
        dto.setClubeVisitanteId(partida.getClubeVisitante().getId());
        dto.setEstadioId(partida.getEstadio().getId());
        dto.setGolsCasa(partida.getGolsCasa());
        dto.setGolsVisitante(partida.getGolsVisitante());
        dto.setDataPartida(partida.getDataPartida());
        return dto;
    }
}