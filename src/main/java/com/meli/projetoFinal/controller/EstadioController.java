package com.meli.projetoFinal.controller;

import com.meli.projetoFinal.dto.request.EstadioDTO;
import com.meli.projetoFinal.model.Estadio;
import com.meli.projetoFinal.service.EstadioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("estadio")
@Tag(name = "Estadio")
public class EstadioController {

    @Autowired
    private EstadioService estadioService;



    @PostMapping
    public ResponseEntity<Estadio> cadastrarEstadio(@RequestBody EstadioDTO estadioDTO) {
        Estadio estadio = estadioService.cadastrarEstadio(estadioDTO);
        return ResponseEntity.status(201).body(estadio);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Estadio> atualizarEstadio(@PathVariable Long id, @RequestBody EstadioDTO estadioDTO) {
        Estadio estadio = estadioService.atualizarEstadio(id, estadioDTO);
        return ResponseEntity.status(200).body(estadio);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estadio> getEstadio(@PathVariable Long id) {
        Estadio estadio = estadioService.getEstadio(id);
        return ResponseEntity.status(200).body(estadio);
    }

    @GetMapping
    public ResponseEntity<Page<Estadio>> getEstadios(Pageable pageable) {
        Page<Estadio> estadios = estadioService.getEstadios( pageable);
        return ResponseEntity.status(200).body(estadios);
    }

}
