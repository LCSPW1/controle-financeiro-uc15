package com.controlefinanceiro.core.app;

import com.controlefinanceiro.core.exception.AutenticacaoException;
import com.controlefinanceiro.core.exception.ValidacaoException;
import com.controlefinanceiro.core.model.Categoria;
import com.controlefinanceiro.core.model.ResumoFinanceiro;
import com.controlefinanceiro.core.model.TipoMovimentacao;
import com.controlefinanceiro.core.model.Usuario;
import com.controlefinanceiro.core.repository.memory.InMemoryCategoriaRepository;
import com.controlefinanceiro.core.repository.memory.InMemoryMovimentacaoRepository;
import com.controlefinanceiro.core.repository.memory.InMemoryUsuarioRepository;
import com.controlefinanceiro.core.service.CategoriaService;
import com.controlefinanceiro.core.service.MovimentacaoService;
import com.controlefinanceiro.core.service.UsuarioService;

public final class ControleFinanceiroCoreApp {

    private static int totalTestes = 0;
    private static int totalFalhas = 0;

    public static void main(String[] args) {
        System.out.println("== Testes da camada de negocio (controle-financeiro-core) ==\n");

        InMemoryUsuarioRepository usuarioRepository = new InMemoryUsuarioRepository();
        InMemoryCategoriaRepository categoriaRepository = new InMemoryCategoriaRepository();
        InMemoryMovimentacaoRepository movimentacaoRepository = new InMemoryMovimentacaoRepository();

        UsuarioService usuarioService = new UsuarioService(usuarioRepository);
        CategoriaService categoriaService = new CategoriaService(categoriaRepository);
        MovimentacaoService movimentacaoService = new MovimentacaoService(movimentacaoRepository, categoriaService);

        categoriaRepository.adicionar(new Categoria(1, "Alimentação"));
        categoriaRepository.adicionar(new Categoria(2, "Salário"));

        testarCadastroDeUsuarioComSucesso(usuarioService, usuarioRepository);
        testarCadastroComEmailDuplicadoDeveFalhar(usuarioService);
        testarLoginComCredenciaisCorretas(usuarioService);
        testarLoginComCredenciaisErradasDeveFalhar(usuarioService);
        testarListagemDeCategorias(categoriaService);
        testarCategoriaInvalidaDeveFalhar(categoriaService);
        Usuario usuarioTeste = usuarioRepository.autenticar("maria@teste.com", "123456");
        testarRegistroDeMovimentacoesEResumo(movimentacaoService, usuarioTeste);
        testarValorInvalidoDeveFalhar(movimentacaoService, usuarioTeste);
        testarResumoNaoMisturaMovimentacoesDeOutroUsuario(usuarioService, usuarioRepository, movimentacaoService);

        System.out.println("\n== Resumo ==");
        System.out.println("Total de testes: " + totalTestes);
        System.out.println("Falhas: " + totalFalhas);
        System.out.println(totalFalhas == 0 ? "TODOS OS TESTES PASSARAM." : "HÁ TESTES FALHANDO.");

        if (totalFalhas > 0) {
            System.exit(1);
        }
    }

    private static void testarCadastroDeUsuarioComSucesso(UsuarioService service,
                                                            InMemoryUsuarioRepository repo) {
        iniciarTeste("Cadastro de usuário com dados válidos");
        try {
            service.cadastrar("Maria Silva", "maria@teste.com", "123456");
            assertTrue(repo.existeEmail("maria@teste.com"), "usuário deveria ter sido persistido");
            sucesso();
        } catch (Exception e) {
            falha(e);
        }
    }

    private static void testarCadastroComEmailDuplicadoDeveFalhar(UsuarioService service) {
        iniciarTeste("Cadastro com e-mail já existente deve lançar ValidacaoException");
        try {
            service.cadastrar("Maria Duplicada", "maria@teste.com", "outrasenha");
            falha(new AssertionError("esperava ValidacaoException, nenhuma exceção foi lançada"));
        } catch (ValidacaoException e) {
            sucesso();
        } catch (Exception e) {
            falha(e);
        }
    }

    private static void testarLoginComCredenciaisCorretas(UsuarioService service) {
        iniciarTeste("Login com e-mail e senha corretos");
        try {
            Usuario usuario = service.autenticar("maria@teste.com", "123456");
            assertTrue(usuario != null && "Maria Silva".equals(usuario.getNome()), "usuário retornado incorreto");
            sucesso();
        } catch (Exception e) {
            falha(e);
        }
    }

    private static void testarLoginComCredenciaisErradasDeveFalhar(UsuarioService service) {
        iniciarTeste("Login com senha incorreta deve lançar AutenticacaoException");
        try {
            service.autenticar("maria@teste.com", "senhaErrada");
            falha(new AssertionError("esperava AutenticacaoException, nenhuma exceção foi lançada"));
        } catch (AutenticacaoException e) {
            sucesso();
        } catch (Exception e) {
            falha(e);
        }
    }

    private static void testarListagemDeCategorias(CategoriaService service) {
        iniciarTeste("Listagem de categorias cadastradas");
        try {
            assertTrue(service.listarCategorias().size() == 2, "esperava 2 categorias cadastradas");
            sucesso();
        } catch (Exception e) {
            falha(e);
        }
    }

    private static void testarCategoriaInvalidaDeveFalhar(CategoriaService service) {
        iniciarTeste("Selecionar categoria com id 0 (placeholder) deve lançar ValidacaoException");
        try {
            service.buscarCategoriaValida(0);
            falha(new AssertionError("esperava ValidacaoException, nenhuma exceção foi lançada"));
        } catch (ValidacaoException e) {
            sucesso();
        } catch (Exception e) {
            falha(e);
        }
    }

    private static void testarRegistroDeMovimentacoesEResumo(MovimentacaoService service, Usuario usuario) {
        iniciarTeste("Registrar entrada e saída e calcular resumo (saldo = entradas - saidas)");
        try {
            service.registrarMovimentacao(usuario, 3000.0, "Salário do mês", TipoMovimentacao.ENTRADA, 2);
            service.registrarMovimentacao(usuario, 450.50, "Supermercado", TipoMovimentacao.SAIDA, 1);
            service.registrarMovimentacao(usuario, 120.0, "Restaurante", TipoMovimentacao.SAIDA, 1);

            ResumoFinanceiro resumo = service.gerarResumo(usuario.getId());

            assertTrue(resumo.getTotalEntradas() == 3000.0, "total de entradas incorreto");
            assertTrue(resumo.getTotalSaidas() == 570.50, "total de saídas incorreto");
            assertTrue(resumo.getSaldo() == 2429.50, "saldo incorreto");
            assertTrue(!resumo.isSaldoNegativo(), "saldo não deveria ser negativo");
            sucesso();
        } catch (Exception e) {
            falha(e);
        }
    }

    private static void testarValorInvalidoDeveFalhar(MovimentacaoService service, Usuario usuario) {
        iniciarTeste("Registrar movimentação com valor <= 0 deve lançar ValidacaoException");
        try {
            service.registrarMovimentacao(usuario, 0.0, "Valor inválido", TipoMovimentacao.SAIDA, 1);
            falha(new AssertionError("esperava ValidacaoException, nenhuma exceção foi lançada"));
        } catch (ValidacaoException e) {
            sucesso();
        } catch (Exception e) {
            falha(e);
        }
    }

    private static void testarResumoNaoMisturaMovimentacoesDeOutroUsuario(
            UsuarioService usuarioService,
            InMemoryUsuarioRepository usuarioRepository,
            MovimentacaoService movimentacaoService) {

        iniciarTeste("Regressão do bug original: resumo de um usuário não deve incluir "
                + "movimentações de outro usuário");
        try {
            usuarioService.cadastrar("Joao Souza", "joao@teste.com", "abcdef");
            Usuario joao = usuarioRepository.autenticar("joao@teste.com", "abcdef");

            movimentacaoService.registrarMovimentacao(joao, 999.0, "Movimentação do João",
                    TipoMovimentacao.ENTRADA, 2);

            Usuario maria = usuarioRepository.autenticar("maria@teste.com", "123456");
            ResumoFinanceiro resumoMaria = movimentacaoService.gerarResumo(maria.getId());

            assertTrue(resumoMaria.getTotalEntradas() == 3000.0,
                    "o resumo de Maria não deveria incluir a movimentação de João");
            sucesso();
        } catch (Exception e) {
            falha(e);
        }
    }

    private static void iniciarTeste(String nome) {
        totalTestes++;
        System.out.print("[TESTE " + totalTestes + "] " + nome + " ... ");
    }

    private static void sucesso() {
        System.out.println("OK");
    }

    private static void falha(Throwable causa) {
        totalFalhas++;
        System.out.println("FALHOU (" + causa.getClass().getSimpleName() + ": " + causa.getMessage() + ")");
    }

    private static void assertTrue(boolean condicao, String mensagemSeFalhar) {
        if (!condicao) {
            throw new AssertionError(mensagemSeFalhar);
        }
    }
}
