package com.meli.projetoFinal.controller;

import com.meli.projetoFinal.dto.ClubeRequestDTO;
import com.meli.projetoFinal.model.Clube;
import com.meli.projetoFinal.service.ClubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("clube")
public class ClubeController {

    @Autowired
    private ClubeService clubeService;

    @PostMapping()
    public ResponseEntity<Clube> cadastrar(@RequestBody ClubeRequestDTO clubeRequestDTO) {
        try {
            Clube mensagem = clubeService.cadastrarClube(clubeRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
        }catch (Exception ex){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Clube> atualizar(@PathVariable Long id, @RequestBody ClubeRequestDTO clubeRequestDTO) {
        try {
            Clube clubeAtualizado = clubeService.atualizarClube(id, clubeRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(clubeAtualizado);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clube> buscar(@PathVariable Long id) {
        try{
            Clube buscarClube = clubeService.buscarClubePorId(id);
            return ResponseEntity.status(HttpStatus.OK).body(buscarClube);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping()
    public ResponseEntity<List<Clube>> buscarTodosClubes() {
        try {
            List<Clube> clubes = clubeService.buscarTodosClubes();
            return ResponseEntity.status(HttpStatus.OK).body(clubes);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
