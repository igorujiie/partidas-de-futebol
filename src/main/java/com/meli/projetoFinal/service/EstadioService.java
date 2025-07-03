package com.meli.projetoFinal.service;

import com.meli.projetoFinal.dto.EstadioDTO;
import com.meli.projetoFinal.model.Estadio;
import com.meli.projetoFinal.repository.EstadioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadioService {

    @Autowired
    private EstadioRepository estadioRepository;

    public Estadio cadastrarEstadio(EstadioDTO estadioDTO) {
        Estadio estadio = new Estadio();
        estadio.setNome(estadioDTO.getNome());
        estadioRepository.save(estadio);
        return estadio;
    }


    public Estadio atualizarEstadio(Long id, EstadioDTO estadioDTO) {
         Estadio estadio = estadioRepository.findById(id)
             .orElseThrow(() -> new RuntimeException("Estádio não encontrado"));
         estadio.setNome(estadioDTO.getNome());
         return estadioRepository.save(estadio);
    }

    public Estadio getEstadio(Long id) {
        return estadioRepository.findById(id).orElseThrow(() -> new RuntimeException("Estádio não encontrado"));
    }

    public List<Estadio> getEstadios() {
        return estadioRepository.findAll();
    }

    public void deleteEstadio(Long id) {
        estadioRepository.deleteById(id);
    }
}
