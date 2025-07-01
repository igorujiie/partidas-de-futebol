package com.meli.projetoFinal.service;

import com.meli.projetoFinal.Exception.ConflitoDeDadosException;
import com.meli.projetoFinal.dto.ClubeRequestDTO;
import com.meli.projetoFinal.model.Clube;
import com.meli.projetoFinal.repository.ClubeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClubeService {

    private final ClubeRepository clubeRepository;


    public ClubeService(ClubeRepository clubeRepository) {
        this.clubeRepository = clubeRepository;
    }

    public Clube cadastrarClube(ClubeRequestDTO dto) {
        if (clubeRepository.existsByNomeAndEstado(dto.getNome(), dto.getEstado())) {
            throw new ConflitoDeDadosException("Já existe um clube com esse nome nesse estado.");
        }
        Clube clube = new Clube();
        clube.setNome(dto.getNome());
        clube.setEstado(dto.getEstado());
        clube.setDataCriacao(dto.getDataCriacao());
        clube.setAtivo(dto.getAtivo());
        return clubeRepository.save(clube);
    }

    public Clube atualizarClube(Long id, ClubeRequestDTO clubeRequestDTO) {
        if(clubeRequestDTO.getNome().length() <3){
            throw  new ConflitoDeDadosException("Nome deve ter no minimo 2 caracteres.");
        }
        Clube clube = clubeRepository.findById(id).orElseThrow();
        clube.setNome(clubeRequestDTO.getNome());
        clube.setEstado(clubeRequestDTO.getEstado());
        clube.setDataCriacao(clubeRequestDTO.getDataCriacao());
        clube.setAtivo(clubeRequestDTO.getAtivo());
        return clubeRepository.save(clube);
    }

    public Clube buscarClubePorId(Long id) {
        return clubeRepository.findById(id).orElseThrow();
    }

    public List<Clube> buscarTodosClubes() {
        return clubeRepository.findAll();
    }
}
