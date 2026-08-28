package com.controlefinanceiro.web.controller.dto;

import com.controlefinanceiro.core.model.ResumoFinanceiro;

public record ResumoResponse(double totalEntradas, double totalSaidas, double saldo, boolean saldoNegativo) {

    public static ResumoResponse de(ResumoFinanceiro resumo) {
        return new ResumoResponse(
                resumo.getTotalEntradas(),
                resumo.getTotalSaidas(),
                resumo.getSaldo(),
                resumo.isSaldoNegativo()
        );
    }
}
