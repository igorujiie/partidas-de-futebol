package com.meli.projetoFinal.controller;

import com.meli.projetoFinal.dto.PartidasDTO;
import com.meli.projetoFinal.model.Partidas;
import com.meli.projetoFinal.service.PartidasService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

//    @GetMapping
//    public ResponseEntity<List<PartidasDTO>> listarPartidas() {
//        List<PartidasDTO> partidas = partidasService.listarPartidas();
//        return ResponseEntity.ok(partidas);
//    }
}
