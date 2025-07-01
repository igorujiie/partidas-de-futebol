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
    @ResponseStatus(HttpStatus.CONFLICT)
    public String lidarConflito(ConflitoDeDadosException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String lidarValidacao(MethodArgumentNotValidException ex) {
        return "Dados inválidos: " + ex.getBindingResult().toString();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String lidarEnumErro(MethodArgumentTypeMismatchException ex) {
        return "Estado inválido: " + ex.getValue();
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String lidarNomeClubeInvalido(MethodArgumentNotValidException ex){
//        return "Nome do CLube deve ser maior que dois caracteres, valor inserido: " + ex.getBindingResult().toString();
//    }
}
