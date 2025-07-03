package com.meli.projetoFinal.controller;

import com.meli.projetoFinal.dto.ClubeRequestDTO;
import com.meli.projetoFinal.model.Clube;
import com.meli.projetoFinal.service.ClubeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("clube")
public class ClubeController {

    @Autowired
    private ClubeService clubeService;

    @PostMapping()
    public ResponseEntity<Clube> cadastrar(@RequestBody @Valid ClubeRequestDTO clubeRequestDTO) {
        Clube mensagem = clubeService.cadastrarClube(clubeRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Clube> atualizar(@PathVariable Long id, @RequestBody @Valid ClubeRequestDTO clubeRequestDTO) {
            Clube clubeAtualizado = clubeService.atualizarClube(id, clubeRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(clubeAtualizado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clube> buscar(@PathVariable Long id) {
            Clube buscarClube = clubeService.buscarClubePorId(id);
            return ResponseEntity.status(HttpStatus.OK).body(buscarClube);

    }

    @GetMapping()
    public ResponseEntity<Page<Clube>> buscarTodosClubes(Pageable pageable) {
            Page<Clube> clubes = clubeService.buscarTodosClubes(pageable);
            return ResponseEntity.status(HttpStatus.OK).body(clubes);
    }
}
