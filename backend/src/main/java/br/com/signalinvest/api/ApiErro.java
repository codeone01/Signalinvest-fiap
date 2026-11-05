package br.com.signalinvest.api;

import java.time.OffsetDateTime;

public record ApiErro(
        OffsetDateTime instante,
        int status,
        String erro,
        String mensagem,
        String caminho
) {
}
