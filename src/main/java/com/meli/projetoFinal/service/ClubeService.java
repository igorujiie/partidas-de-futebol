package com.meli.projetoFinal.service;

import com.meli.projetoFinal.Exception.ConflitoDeDadosException;
import com.meli.projetoFinal.dto.ClubeRequestDTO;
import com.meli.projetoFinal.model.Clube;
import com.meli.projetoFinal.repository.ClubeRepository;
import org.springframework.stereotype.Service;

@Service
public class ClubeService {
    private final ClubeRepository clubeRepository;

    public ClubeService(ClubeRepository clubeRepository) {
        this.clubeRepository = clubeRepository;
    }

    public ClubeRequestDTO cadastrarClube(ClubeRequestDTO dto) {
        if (clubeRepository.existsByNomeAndEstado(dto.getNome(), dto.getEstado())) {
            throw new ConflitoDeDadosException("Já existe um clube com esse nome nesse estado.");
        }
        ClubeRequestDTO clube = new ClubeRequestDTO();
        clube.setNome(dto.getNome());
        clube.setEstado(dto.getEstado());
        clube.setDataCriacao(dto.getDataCriacao());
        return clubeRepository.save(clube);
    }

    public ClubeRequestDTO atualizarClube(Long id, ClubeRequestDTO clubeRequestDTO) {
        ClubeRequestDTO clube = clubeRepository.findById(id).orElseThrow();
        clube.setNome(clubeRequestDTO.getNome());
        clube.setEstado(clubeRequestDTO.getEstado());
        clube.setDataCriacao(clubeRequestDTO.getDataCriacao());
        return clubeRepository.save(clube);
    }
}
