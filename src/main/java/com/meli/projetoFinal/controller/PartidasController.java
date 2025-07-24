package com.meli.projetoFinal.controller;

import com.meli.projetoFinal.dto.request.PartidasDTO;
import com.meli.projetoFinal.dto.response.ConfrontoDiretoDTO;
import com.meli.projetoFinal.dto.response.RankingDTO;
import com.meli.projetoFinal.dto.response.RetrospectoAdversarioDTO;
import com.meli.projetoFinal.dto.response.RetrospectoClubeDTO;
import com.meli.projetoFinal.model.Partidas;
import com.meli.projetoFinal.service.PartidasService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/partidas")
@Tag(name = "Partidas")
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
        Partidas partida = partidasService.atualizarPartida(id, partidasDTO);
        return ResponseEntity.status(200).body(partida);
    }

    @GetMapping
    public ResponseEntity<Page<Partidas>> listarPartidas(Pageable pageable, @RequestParam(required = false) boolean goleada) {
        Page<Partidas> partidas = partidasService.getPartidas(pageable, goleada);
        return ResponseEntity.status(HttpStatus.OK).body(partidas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partidas> buscarPartida(@PathVariable Long id) {
        Partidas partida = partidasService.getPartida(id);
        return ResponseEntity.status(HttpStatus.OK).body(partida);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Partidas> deletarPartida(@PathVariable Long id) {
        partidasService.deletePartida(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/retrospecto/{clubeId}")
    public ResponseEntity<RetrospectoClubeDTO> getRetrospectoClube(@PathVariable Long clubeId,
                                                                   @RequestParam(required = false) String tipoClubePartida) {
        RetrospectoClubeDTO dto = partidasService.getRetrospectoClube(clubeId, tipoClubePartida);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/retrospecto/adversarios/{clubeId}")
    public ResponseEntity<List<RetrospectoAdversarioDTO>> getRetrospectoContraAdversarios(@PathVariable Long clubeId) {
        List<RetrospectoAdversarioDTO> lista = partidasService.getRetrospectoContraAdversarios(clubeId);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/retrospecto/confronto")
    public ResponseEntity<ConfrontoDiretoDTO> getConfrontosDiretos(
            @RequestParam Long clubeId, @RequestParam Long adversarioId) {
        ConfrontoDiretoDTO dto = partidasService.getRetrospectoConfronto(clubeId, adversarioId);
        return ResponseEntity.ok(dto);
    }
    
    
    @GetMapping("/ranking")
    public ResponseEntity<List<RankingDTO>> getRanking(@RequestParam String criterio) {
        List<RankingDTO> ranking = partidasService.getRanking(criterio);
        return ResponseEntity.ok(ranking);
    }
}
