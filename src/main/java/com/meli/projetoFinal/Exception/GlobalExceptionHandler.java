package com.meli.projetoFinal.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConflitoDeDadosException.class)
    public ResponseEntity<String> handleConflito(ConflitoDeDadosException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(DadosInvalidosException.class)
    public ResponseEntity<String> handleDadosInvalidos(DadosInvalidosException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(DadoNaoEncontradoException.class)
    public ResponseEntity<String> handleDadoNaoEncontrado(DadoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
