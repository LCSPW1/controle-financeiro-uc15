package com.controlefinanceiro.web.exception;

import com.controlefinanceiro.core.exception.AutenticacaoException;
import com.controlefinanceiro.core.exception.PersistenciaException;
import com.controlefinanceiro.core.exception.ValidacaoException;
import com.controlefinanceiro.web.controller.dto.ErroResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Ponto único de tradução das exceções de negócio do
 * controle-financeiro-core (Etapas 6/7) para respostas HTTP.
 *
 * Esta classe é o motivo pelo qual as exceções personalizadas criadas na
 * Etapa 6 (ValidacaoException, AutenticacaoException, PersistenciaException)
 * — que até então só apareciam em stack traces de console — passam a ter
 * um significado direto para quem consome a API: cada tipo de exceção do
 * domínio já nasce mapeado para o código HTTP correto, sem que os
 * controllers precisem de nenhum bloco try/catch próprio.
 */
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
