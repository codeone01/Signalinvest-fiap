package br.com.signalinvest.dto;

import br.com.signalinvest.model.Receita;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ReceitaResposta(
        Long id,
        Long contaId,
        String conta,
        String descricao,
        BigDecimal valor,
        LocalDate data,
        String tipo
) {
    public static ReceitaResposta de(Receita receita) {
        return new ReceitaResposta(receita.getId(), receita.getConta().getId(),
                receita.getConta().getBanco() + " • " + receita.getConta().getNumero(),
                receita.getDescricao(), receita.getValor(), receita.getData(), receita.getTipo());
    }
}
