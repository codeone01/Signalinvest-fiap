package br.com.signalinvest.dto;

import br.com.signalinvest.model.ContaBancaria;
import java.math.BigDecimal;

public record ContaResposta(
        Long id,
        String banco,
        String agencia,
        String numero,
        String tipo,
        BigDecimal saldo
) {
    public static ContaResposta de(ContaBancaria conta) {
        return new ContaResposta(conta.getId(), conta.getBanco(), conta.getAgencia(),
                conta.getNumero(), conta.getTipo(), conta.getSaldo());
    }
}
