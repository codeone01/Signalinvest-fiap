package br.com.signalinvest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoRequisicao(
        Long contaId,
        String descricao,
        BigDecimal valor,
        LocalDate data,
        String tipo
) {
}
