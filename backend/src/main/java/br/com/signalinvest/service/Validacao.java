package br.com.signalinvest.service;

import br.com.signalinvest.exception.RegraNegocioException;
import java.math.BigDecimal;
import java.time.LocalDate;

final class Validacao {
    private Validacao() {
    }

    static String texto(String valor, String campo, int limite) {
        if (valor == null || valor.isBlank()) {
            throw new RegraNegocioException(campo + " é obrigatório.");
        }
        String normalizado = valor.trim();
        if (normalizado.length() > limite) {
            throw new RegraNegocioException(campo + " deve ter no máximo " + limite + " caracteres.");
        }
        return normalizado;
    }

    static BigDecimal dinheiro(BigDecimal valor, String campo, boolean positivo) {
        if (valor == null) {
            throw new RegraNegocioException(campo + " é obrigatório.");
        }
        if (valor.scale() > 2 || valor.precision() - valor.scale() > 12) {
            throw new RegraNegocioException(campo + " deve respeitar o formato NUMBER(14,2).");
        }
        if (positivo && valor.signum() <= 0) {
            throw new RegraNegocioException(campo + " deve ser maior que zero.");
        }
        return valor.setScale(2);
    }

    static LocalDate data(LocalDate valor) {
        if (valor == null) {
            throw new RegraNegocioException("Data é obrigatória.");
        }
        return valor;
    }

    static Long id(Long valor, String campo) {
        if (valor == null || valor <= 0) {
            throw new RegraNegocioException(campo + " é obrigatório.");
        }
        return valor;
    }
}
