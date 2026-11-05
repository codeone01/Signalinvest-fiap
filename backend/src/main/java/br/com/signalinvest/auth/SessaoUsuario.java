package br.com.signalinvest.auth;

import br.com.signalinvest.exception.NaoAutorizadoException;
import jakarta.servlet.http.HttpSession;

public final class SessaoUsuario {
    public static final String USUARIO_ID = "USUARIO_ID";
    public static final String USUARIO_NOME = "USUARIO_NOME";

    private SessaoUsuario() {
    }

    public static Long obterId(HttpSession session) {
        Object valor = session.getAttribute(USUARIO_ID);
        if (valor instanceof Long id) {
            return id;
        }
        throw new NaoAutorizadoException("Faça login para continuar.");
    }
}
