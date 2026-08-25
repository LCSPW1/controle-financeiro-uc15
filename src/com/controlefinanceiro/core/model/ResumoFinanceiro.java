package com.controlefinanceiro.core.model;

public class ResumoFinanceiro {

    private final double totalEntradas;
    private final double totalSaidas;

    public ResumoFinanceiro(double totalEntradas, double totalSaidas) {
        this.totalEntradas = totalEntradas;
        this.totalSaidas = totalSaidas;
    }

    public double getTotalEntradas() {
        return totalEntradas;
    }

    public double getTotalSaidas() {
        return totalSaidas;
    }

    public double getSaldo() {
        return totalEntradas - totalSaidas;
    }

    public boolean isSaldoNegativo() {
        return getSaldo() < 0;
    }
}
