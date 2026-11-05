package br.com.signalinvest.controller;

import br.com.signalinvest.auth.SessaoUsuario;
import br.com.signalinvest.dto.ContaRequisicao;
import br.com.signalinvest.dto.ContaResposta;
import br.com.signalinvest.service.ContaBancariaService;
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
@RequestMapping("/api/contas")
public class ContaBancariaController {
    private final ContaBancariaService service;

    public ContaBancariaController(ContaBancariaService service) {
        this.service = service;
    }

    @GetMapping
    public List<ContaResposta> listar(HttpSession session) {
        return service.listar(SessaoUsuario.obterId(session));
    }

    @GetMapping("/{id}")
    public ContaResposta buscar(@PathVariable Long id, HttpSession session) {
        return service.buscar(SessaoUsuario.obterId(session), id);
    }

    @PostMapping
    public ResponseEntity<ContaResposta> criar(@RequestBody ContaRequisicao requisicao,
                                               HttpSession session) {
        ContaResposta criada = service.criar(SessaoUsuario.obterId(session), requisicao);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(criada.id()).toUri();
        return ResponseEntity.created(local).body(criada);
    }

    @PutMapping("/{id}")
    public ContaResposta atualizar(@PathVariable Long id, @RequestBody ContaRequisicao requisicao,
                                   HttpSession session) {
        return service.atualizar(SessaoUsuario.obterId(session), id, requisicao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, HttpSession session) {
        service.excluir(SessaoUsuario.obterId(session), id);
        return ResponseEntity.noContent().build();
    }
}
