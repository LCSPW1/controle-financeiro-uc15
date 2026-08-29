package com.controlefinanceiro.web.exception;

import com.controlefinanceiro.core.exception.AutenticacaoException;
import com.controlefinanceiro.core.exception.PersistenciaException;
import com.controlefinanceiro.core.exception.ValidacaoException;
import com.controlefinanceiro.web.controller.dto.ErroResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<ErroResponse> tratarValidacao(ValidacaoException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErroResponse(e.getMessage()));
    }

    @ExceptionHandler(AutenticacaoException.class)
    public ResponseEntity<ErroResponse> tratarAutenticacao(AutenticacaoException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErroResponse(e.getMessage()));
    }

    @ExceptionHandler(PersistenciaException.class)
    public ResponseEntity<ErroResponse> tratarPersistencia(PersistenciaException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroResponse("Erro ao acessar o banco de dados. Tente novamente."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> tratarArgumentoInvalido(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErroResponse("Requisição inválida: " + e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> tratarErroGenerico(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroResponse("Erro inesperado no servidor."));
    }
}
