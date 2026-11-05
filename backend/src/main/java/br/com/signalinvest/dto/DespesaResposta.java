package br.com.signalinvest.dto;

import br.com.signalinvest.model.Despesa;
import java.math.BigDecimal;
import java.time.LocalDate;

public record DespesaResposta(
        Long id,
        Long contaId,
        String conta,
        String descricao,
        BigDecimal valor,
        LocalDate data,
        String tipo
) {
    public static DespesaResposta de(Despesa despesa) {
        return new DespesaResposta(despesa.getId(), despesa.getConta().getId(),
                despesa.getConta().getBanco() + " • " + despesa.getConta().getNumero(),
                despesa.getDescricao(), despesa.getValor(), despesa.getData(), despesa.getTipo());
    }
}
