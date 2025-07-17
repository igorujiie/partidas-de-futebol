package com.meli.projetoFinal.controller;

import com.meli.projetoFinal.dto.ClubeDTO;
import com.meli.projetoFinal.model.Clube;
import com.meli.projetoFinal.model.Estado;
import com.meli.projetoFinal.service.ClubeService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("clube")
@Tag(name = "Clube")
public class ClubeController {

    @Autowired
    private ClubeService clubeService;

    @PostMapping()
    public ResponseEntity<Clube> cadastrar(@RequestBody @Valid ClubeDTO clubeDTO) {
        Clube mensagem = clubeService.cadastrarClube(clubeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Clube> atualizar(@PathVariable Long id, @RequestBody @Valid ClubeDTO clubeDTO) {
            Clube clubeAtualizado = clubeService.atualizarClube(id, clubeDTO);
            return ResponseEntity.status(HttpStatus.OK).body(clubeAtualizado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clube> buscar(@PathVariable Long id) {
            Clube buscarClube = clubeService.buscarClubePorId(id);
            return ResponseEntity.status(HttpStatus.OK).body(buscarClube);

    }

    @GetMapping()
    public ResponseEntity<Page<Clube>> buscarTodosClubes(@RequestParam(required = false) String nome,
                                                         @RequestParam(required = false) Estado estado,
                                                         @RequestParam(required = false) Boolean ativo,
                                                         Pageable pageable) {
            Page<Clube> clubes = clubeService.buscarTodosClubes(nome, estado,ativo, pageable);
            return ResponseEntity.status(HttpStatus.OK).body(clubes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clubeService.deletarClube(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
