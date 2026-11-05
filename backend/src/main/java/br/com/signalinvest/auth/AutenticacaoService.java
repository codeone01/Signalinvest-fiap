package br.com.signalinvest.auth;

import br.com.signalinvest.exception.NaoAutorizadoException;
import br.com.signalinvest.model.Usuario;
import br.com.signalinvest.repository.UsuarioRepository;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutenticacaoService {
    private final UsuarioRepository usuarios;
    private final Pbkdf2Senha senhas;

    public AutenticacaoService(UsuarioRepository usuarios, Pbkdf2Senha senhas) {
        this.usuarios = usuarios;
        this.senhas = senhas;
    }

    @Transactional(readOnly = true)
    public Usuario autenticar(String email, String senha) {
        if (email == null || email.isBlank() || senha == null || senha.isBlank()) {
            throw new NaoAutorizadoException("E-mail e senha são obrigatórios.");
        }
        Usuario usuario = usuarios.findByEmailIgnoreCase(email.trim().toLowerCase(Locale.ROOT))
                .filter(item -> "ATIVO".equalsIgnoreCase(item.getStatus()))
                .orElseThrow(() -> new NaoAutorizadoException("E-mail ou senha inválidos."));
        if (!senhas.confere(senha.toCharArray(), usuario.getSenhaHash())) {
            throw new NaoAutorizadoException("E-mail ou senha inválidos.");
        }
        return usuario;
    }
}
