package br.com.signalinvest.auth;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class Pbkdf2SenhaTest {
    private static final String HASH_DEMO =
            "pbkdf2_sha256$210000$FxgqNaXRFu2/y8bl05+PUg==$s1iPfApDbq7qBVqD5J1C/377FVmz+K8DNbM59REr5Ok=";

    private final Pbkdf2Senha senhas = new Pbkdf2Senha();

    @Test
    void confereSenhaDemonstrativa() {
        assertThat(senhas.confere("Signal@2026".toCharArray(), HASH_DEMO)).isTrue();
    }

    @Test
    void rejeitaSenhaIncorretaEFormatoAntigo() {
        assertThat(senhas.confere("outra".toCharArray(), HASH_DEMO)).isFalse();
        assertThat(senhas.confere("qualquer".toCharArray(), "SEM_LOGIN_APENAS_TESTE_JDBC")).isFalse();
    }
}
