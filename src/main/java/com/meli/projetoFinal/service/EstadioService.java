package com.meli.projetoFinal.service;

import com.meli.projetoFinal.Exception.ConflitoDeDadosException;
import com.meli.projetoFinal.Exception.DadoNaoEncontradoException;
import com.meli.projetoFinal.dto.EstadioDTO;
import com.meli.projetoFinal.model.Estadio;
import com.meli.projetoFinal.repository.EstadioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadioService {

    @Autowired
    private EstadioRepository estadioRepository;

    public Estadio cadastrarEstadio(EstadioDTO estadioDTO) {
        if (estadioRepository.existsEstadioByNome(estadioDTO.getNome())) {
            throw new ConflitoDeDadosException("Ja existe um estadio com esse nome");
        }
        Estadio estadio = new Estadio();
        estadio.setNome(estadioDTO.getNome());
        return estadioRepository.save(estadio);
    }


    public Estadio atualizarEstadio(Long id, EstadioDTO estadioDTO) {
         Estadio estadio = estadioRepository.findById(id)
             .orElseThrow(() -> new DadoNaoEncontradoException("Estádio não encontrado"));

         if(estadio.getNome().equals(estadioDTO.getNome())) {
             throw new ConflitoDeDadosException("Ja existe um estadio com esse nome");
         }
         estadio.setNome(estadioDTO.getNome());
         return estadioRepository.save(estadio);
    }

    public Estadio getEstadio(Long id) {
        return estadioRepository.findById(id).orElseThrow(() -> new DadoNaoEncontradoException("Estádio não encontrado"));
    }

    public Page<Estadio> getEstadios(Pageable pageable) {
        return estadioRepository.findAll(pageable);
    }

    public void deleteEstadio(Long id) {
        estadioRepository.deleteById(id);
    }
}
