package com.meli.projetoFinal.service;

import com.meli.projetoFinal.exception.ConflitoDeDadosException;
import com.meli.projetoFinal.exception.DadoNaoEncontradoException;
import com.meli.projetoFinal.dto.request.ClubeDTO;
import com.meli.projetoFinal.model.Clube;
import com.meli.projetoFinal.model.Estado;
import com.meli.projetoFinal.repository.ClubeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ClubeService {

    @Autowired
    private ClubeRepository clubeRepository;


    public Clube cadastrarClube(ClubeDTO dto) {
        if (clubeRepository.existsByNomeAndEstado(dto.getNome(), Estado.valueOf(dto.getEstado().toUpperCase()))) {
            throw new ConflitoDeDadosException("Já existe um clube com esse nome nesse estado.");
        }
        Clube clube = new Clube();
        clube.setNome(dto.getNome().toUpperCase());
        clube.setEstado(Estado.valueOf(dto.getEstado().toUpperCase()));
        clube.setDataCriacao(dto.getDataCriacao());
        clube.setAtivo(dto.getAtivo());
        return clubeRepository.save(clube);
    }

    public Clube atualizarClube(Long id, ClubeDTO clubeDTO) {

        if (!clubeRepository.existsById(id)) {
            throw new DadoNaoEncontradoException("Clube não encontrado");
        }
        if (clubeDTO.getDataCriacao() == null || clubeDTO.getDataCriacao().isAfter(LocalDate.now())) {
            throw new ConflitoDeDadosException("Data de criação inválida ou no futuro");
        }

        Clube clube = clubeRepository.findById(id).orElseThrow();
        clube.setNome(clubeDTO.getNome().toUpperCase());
        clube.setEstado(Estado.valueOf(clubeDTO.getEstado().toUpperCase()));
        clube.setDataCriacao(clubeDTO.getDataCriacao());
        clube.setAtivo(clubeDTO.getAtivo());
        return clubeRepository.save(clube);
    }

    public Clube buscarClubePorId(Long id) {
        return clubeRepository.findById(id)
                .orElseThrow(() -> new DadoNaoEncontradoException("Clube não encontrado"));
    }

    public Page<Clube> buscarTodosClubes(String nome, Estado estado, Boolean ativo, Pageable pageable) {
        if (nome != null && estado != null && ativo != null) {
            return clubeRepository.findByNomeAndEstadoAndAtivo(nome.toUpperCase(), estado, ativo, pageable);
        } else if (nome != null) {
            return clubeRepository.findByNome(nome.toUpperCase(), pageable);
        } else if (estado != null) {
            return clubeRepository.findByEstado(estado, pageable);
        } else if (ativo != null) {
            return clubeRepository.findByAtivo(ativo, pageable);
        } else {
            return clubeRepository.findAll(pageable);
        }
    }

    public void deletarClube(Long id) {
        if (!clubeRepository.existsById(id)) {
            throw new DadoNaoEncontradoException("Clube não encontrado");
        }
        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new DadoNaoEncontradoException("Clube não encontrado"));
        clube.setAtivo(false);
        clubeRepository.save(clube);
    }


}