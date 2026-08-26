package com.controlefinanceiro.core.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o cálculo de saldo financeiro.
 *
 * Este é o principal alvo de teste unitário da Etapa 7: um cálculo simples
 * (saldo = total de entradas - total de saídas), isolado em uma classe
 * própria sem qualquer dependência de banco de dados, arquivo ou rede desde
 * a Etapa 6. Por não depender de infraestrutura externa, pode ser testado
 * de forma totalmente determinística e rápida.
 */
class ResumoFinanceiroTest {

    @Test
    void deveCalcularSaldoPositivoQuandoEntradasMaioresQueSaidas() {
        ResumoFinanceiro resumo = new ResumoFinanceiro(3000.0, 570.50);

        assertEquals(2429.50, resumo.getSaldo(), 0.001);
        assertFalse(resumo.isSaldoNegativo());
    }

    @Test
    void deveCalcularSaldoNegativoQuandoSaidasMaioresQueEntradas() {
        ResumoFinanceiro resumo = new ResumoFinanceiro(500.0, 800.0);

        assertEquals(-300.0, resumo.getSaldo(), 0.001);
        assertTrue(resumo.isSaldoNegativo());
    }

    @Test
    void deveCalcularSaldoZeroQuandoEntradasIguaisASaidas() {
        ResumoFinanceiro resumo = new ResumoFinanceiro(1000.0, 1000.0);

        assertEquals(0.0, resumo.getSaldo(), 0.001);
        assertFalse(resumo.isSaldoNegativo());
    }

    @Test
    void deveManterTotaisInformadosNoConstrutor() {
        ResumoFinanceiro resumo = new ResumoFinanceiro(1200.0, 450.0);

        assertEquals(1200.0, resumo.getTotalEntradas(), 0.001);
        assertEquals(450.0, resumo.getTotalSaidas(), 0.001);
    }
}
