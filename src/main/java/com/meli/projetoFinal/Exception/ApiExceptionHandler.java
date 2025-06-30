package com.meli.projetoFinal.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ConflitoDeDadosException.class)
    @ResponseStatus(HttpStatus.CONFLICT) // 409
    public String handleConflito(ConflitoDeDadosException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public String handleValidacao(MethodArgumentNotValidException ex) {
        return "Dados inválidos: " + ex.getBindingResult().toString();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleEnumErro(MethodArgumentTypeMismatchException ex) {
        return "Estado inválido: " + ex.getValue();
    }
}
