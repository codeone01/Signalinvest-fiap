package br.com.signalinvest.config;

import br.com.signalinvest.auth.SessaoUsuario;
import br.com.signalinvest.exception.NaoAutorizadoException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AutenticacaoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        HttpSession sessao = request.getSession(false);
        if (sessao == null || !(sessao.getAttribute(SessaoUsuario.USUARIO_ID) instanceof Long)) {
            throw new NaoAutorizadoException("Faça login para continuar.");
        }
        return true;
    }
}
