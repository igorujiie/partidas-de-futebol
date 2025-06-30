package com.meli.projetoFinal.controller;

import com.meli.projetoFinal.dto.ClubeRequestDTO;
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

    @PostMapping()
    public ResponseEntity<ClubeRequestDTO> cadastrar(@RequestBody ClubeRequestDTO clubeRequestDTO) {
        try {
            String mensagem = clubeService.cadastrarClube(clubeRequestDTO);
        }catch (Exception ex){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(clubeRequestDTO);

    }
}
