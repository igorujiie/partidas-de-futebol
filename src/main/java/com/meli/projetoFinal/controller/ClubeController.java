package com.meli.projetoFinal.controller;

import com.meli.projetoFinal.dto.ClubeRequestDTO;
import com.meli.projetoFinal.model.Clube;
import com.meli.projetoFinal.service.ClubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("clube")
public class ClubeController {

    @Autowired
    private ClubeService clubeService;

    @PostMapping("/ciarClube")
    public ResponseEntity<ClubeRequestDTO> cadastrar(@RequestBody ClubeRequestDTO clubeRequestDTO) {
        try {
            ClubeRequestDTO mensagem = clubeService.cadastrarClube(clubeRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
        }catch (Exception ex){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClubeRequestDTO> atualizar(@PathVariable Long id, @RequestBody ClubeRequestDTO clubeRequestDTO) {
        try {
            ClubeRequestDTO clubeAtualizado = clubeService.atualizarClube(id, clubeRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(clubeAtualizado);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
