package com.controlefinanceiro.web.controller;

import com.controlefinanceiro.core.model.Usuario;
import com.controlefinanceiro.core.service.UsuarioService;
import com.controlefinanceiro.web.controller.dto.CadastroUsuarioRequest;
import com.controlefinanceiro.web.controller.dto.LoginRequest;
import com.controlefinanceiro.web.controller.dto.UsuarioResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioResponse> cadastrar(@RequestBody CadastroUsuarioRequest requisicao) {
        usuarioService.cadastrar(requisicao.nome(), requisicao.email(), requisicao.senha());

        // Após cadastrar, autentica com a mesma senha para devolver os
        // dados do usuário recém-criado (incluindo o id gerado pelo banco).
        Usuario usuario = usuarioService.autenticar(requisicao.email(), requisicao.senha());

        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.de(usuario));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<UsuarioResponse> login(@RequestBody LoginRequest requisicao) {
        Usuario usuario = usuarioService.autenticar(requisicao.email(), requisicao.senha());
        return ResponseEntity.ok(UsuarioResponse.de(usuario));
    }
}
