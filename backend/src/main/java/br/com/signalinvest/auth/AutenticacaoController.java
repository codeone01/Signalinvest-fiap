package br.com.signalinvest.auth;

import br.com.signalinvest.model.Usuario;
import br.com.signalinvest.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AutenticacaoController {
    private final AutenticacaoService autenticacao;
    private final UsuarioRepository usuarios;

    public AutenticacaoController(AutenticacaoService autenticacao, UsuarioRepository usuarios) {
        this.autenticacao = autenticacao;
        this.usuarios = usuarios;
    }

    @PostMapping("/login")
    public SessaoResposta login(@RequestBody LoginRequisicao requisicao, HttpServletRequest request) {
        Usuario usuario = autenticacao.autenticar(requisicao.email(), requisicao.senha());
        HttpSession anterior = request.getSession(false);
        if (anterior != null) {
            anterior.invalidate();
        }
        HttpSession sessao = request.getSession(true);
        sessao.setAttribute(SessaoUsuario.USUARIO_ID, usuario.getId());
        sessao.setAttribute(SessaoUsuario.USUARIO_NOME, usuario.getNome());
        return resposta(usuario);
    }

    @GetMapping("/sessao")
    public SessaoResposta sessao(HttpSession session) {
        Long usuarioId = SessaoUsuario.obterId(session);
        Usuario usuario = usuarios.findById(usuarioId)
                .orElseThrow(() -> new br.com.signalinvest.exception.NaoAutorizadoException(
                        "A sessão não corresponde a um usuário válido."));
        return resposta(usuario);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession sessao = request.getSession(false);
        if (sessao != null) {
            sessao.invalidate();
        }
        return ResponseEntity.noContent().build();
    }

    private SessaoResposta resposta(Usuario usuario) {
        return new SessaoResposta(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}
