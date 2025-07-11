package com.meli.projetoFinal.controller;

import com.meli.projetoFinal.dto.PartidasDTO;
import com.meli.projetoFinal.model.Partidas;
import com.meli.projetoFinal.service.PartidasService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



@RestController
@RequestMapping("/partidas")
public class PartidasController {

    @Autowired
    private PartidasService partidasService;

    @PostMapping
    public ResponseEntity<Partidas> criarPartida(@RequestBody @Valid PartidasDTO partidasDTO) {

        Partidas partida = partidasService.cadastrarPartida(partidasDTO);
        return ResponseEntity.status(201).body(partida);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Partidas> atualizarPartida(@PathVariable Long id, @RequestBody @Valid PartidasDTO partidasDTO) {
        Partidas partida = partidasService.atualizarPartida(id,partidasDTO);
        return ResponseEntity.status(200).body(partida);
    }

    @GetMapping
    public ResponseEntity<Page<Partidas>> listarPartidas(Pageable pageable) {
        Page<Partidas> partidas = partidasService.getPartidas(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(partidas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partidas> buscarPartida(@PathVariable Long id){
        Partidas partida = partidasService.getPartida(id);
        return ResponseEntity.status(HttpStatus.OK).body(partida);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Partidas> deletarPartida(@PathVariable Long id) {
        partidasService.deletePartida(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
