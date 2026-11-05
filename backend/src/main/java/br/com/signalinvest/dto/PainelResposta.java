package br.com.signalinvest.dto;

import java.math.BigDecimal;

public record PainelResposta(
        BigDecimal saldoCadastrado,
        BigDecimal totalReceitas,
        BigDecimal totalDespesas,
        BigDecimal resultadoLancamentos,
        long quantidadeContas
) {
}
