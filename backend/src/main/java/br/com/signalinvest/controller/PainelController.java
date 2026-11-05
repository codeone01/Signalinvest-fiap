package br.com.signalinvest.controller;

import br.com.signalinvest.auth.SessaoUsuario;
import br.com.signalinvest.dto.PainelResposta;
import br.com.signalinvest.service.PainelService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/painel")
public class PainelController {
    private final PainelService service;

    public PainelController(PainelService service) {
        this.service = service;
    }

    @GetMapping
    public PainelResposta consultar(HttpSession session) {
        return service.consultar(SessaoUsuario.obterId(session));
    }
}
