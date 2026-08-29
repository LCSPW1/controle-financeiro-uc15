package com.controlefinanceiro.web.controller;

import com.controlefinanceiro.core.model.Movimentacao;
import com.controlefinanceiro.core.model.TipoMovimentacao;
import com.controlefinanceiro.core.model.Usuario;
import com.controlefinanceiro.core.service.MovimentacaoService;
import com.controlefinanceiro.web.controller.dto.MovimentacaoResponse;
import com.controlefinanceiro.web.controller.dto.NovaMovimentacaoRequest;
import com.controlefinanceiro.web.controller.dto.ResumoResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;

    public MovimentacaoController(MovimentacaoService movimentacaoService) {
        this.movimentacaoService = movimentacaoService;
    }

    @PostMapping("/movimentacoes")
    public ResponseEntity<MovimentacaoResponse> registrar(@RequestBody NovaMovimentacaoRequest requisicao) {
        Usuario usuario = new Usuario();
        usuario.setId(requisicao.idUsuario());

        TipoMovimentacao tipo = converterTipo(requisicao.tipo());

        Movimentacao movimentacao = movimentacaoService.registrarMovimentacao(
                usuario, requisicao.valor(), requisicao.descricao(), tipo, requisicao.idCategoria());

        return ResponseEntity.status(HttpStatus.CREATED).body(MovimentacaoResponse.de(movimentacao));
    }

    @GetMapping("/movimentacoes")
    public List<MovimentacaoResponse> listar(@RequestParam("idUsuario") int idUsuario) {
        return movimentacaoService.listarMovimentacoes(idUsuario).stream()
                .map(MovimentacaoResponse::de)
                .toList();
    }

    @GetMapping("/resumo")
    public ResumoResponse resumo(@RequestParam("idUsuario") int idUsuario) {
        return ResumoResponse.de(movimentacaoService.gerarResumo(idUsuario));
    }

    private TipoMovimentacao converterTipo(String tipoTexto) {
        if (tipoTexto == null) {
            return null;
        }
        try {
            return TipoMovimentacao.valueOf(tipoTexto.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de movimentação inválido: " + tipoTexto);
        }
    }
}
