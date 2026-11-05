package br.com.signalinvest.dto;

import java.math.BigDecimal;

public record ContaRequisicao(
        String banco,
        String agencia,
        String numero,
        String tipo,
        BigDecimal saldo
) {
}
