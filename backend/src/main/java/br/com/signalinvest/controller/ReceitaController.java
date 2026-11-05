package br.com.signalinvest.controller;

import br.com.signalinvest.auth.SessaoUsuario;
import br.com.signalinvest.dto.LancamentoRequisicao;
import br.com.signalinvest.dto.ReceitaResposta;
import br.com.signalinvest.service.ReceitaService;
import jakarta.servlet.http.HttpSession;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/receitas")
public class ReceitaController {
    private final ReceitaService service;

    public ReceitaController(ReceitaService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReceitaResposta> listar(HttpSession session) {
        return service.listar(SessaoUsuario.obterId(session));
    }

    @GetMapping("/{id}")
    public ReceitaResposta buscar(@PathVariable Long id, HttpSession session) {
        return service.buscar(SessaoUsuario.obterId(session), id);
    }

    @PostMapping
    public ResponseEntity<ReceitaResposta> criar(@RequestBody LancamentoRequisicao requisicao,
                                                 HttpSession session) {
        ReceitaResposta criada = service.criar(SessaoUsuario.obterId(session), requisicao);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(criada.id()).toUri();
        return ResponseEntity.created(local).body(criada);
    }

    @PutMapping("/{id}")
    public ReceitaResposta atualizar(@PathVariable Long id, @RequestBody LancamentoRequisicao requisicao,
                                     HttpSession session) {
        return service.atualizar(SessaoUsuario.obterId(session), id, requisicao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, HttpSession session) {
        service.excluir(SessaoUsuario.obterId(session), id);
        return ResponseEntity.noContent().build();
    }
}
