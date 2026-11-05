package br.com.signalinvest.service;

import br.com.signalinvest.dto.ContaRequisicao;
import br.com.signalinvest.dto.ContaResposta;
import br.com.signalinvest.exception.ConflitoException;
import br.com.signalinvest.exception.RecursoNaoEncontradoException;
import br.com.signalinvest.model.ContaBancaria;
import br.com.signalinvest.model.Usuario;
import br.com.signalinvest.repository.ContaBancariaRepository;
import br.com.signalinvest.repository.DespesaRepository;
import br.com.signalinvest.repository.ReceitaRepository;
import br.com.signalinvest.repository.UsuarioRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContaBancariaService {
    private final ContaBancariaRepository contas;
    private final UsuarioRepository usuarios;
    private final ReceitaRepository receitas;
    private final DespesaRepository despesas;

    public ContaBancariaService(ContaBancariaRepository contas, UsuarioRepository usuarios,
                                ReceitaRepository receitas, DespesaRepository despesas) {
        this.contas = contas;
        this.usuarios = usuarios;
        this.receitas = receitas;
        this.despesas = despesas;
    }

    @Transactional(readOnly = true)
    public List<ContaResposta> listar(Long usuarioId) {
        return contas.findAllByUsuarioIdOrderByIdDesc(usuarioId).stream()
                .map(ContaResposta::de).toList();
    }

    @Transactional(readOnly = true)
    public ContaResposta buscar(Long usuarioId, Long id) {
        return ContaResposta.de(contaDoUsuario(usuarioId, id));
    }

    @Transactional
    public ContaResposta criar(Long usuarioId, ContaRequisicao requisicao) {
        Usuario usuario = usuarios.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário da sessão não encontrado."));
        DadosConta dados = validar(requisicao);
        ContaBancaria conta = new ContaBancaria(usuario, dados.banco(), dados.agencia(),
                dados.numero(), dados.tipo(), dados.saldo());
        return ContaResposta.de(contas.save(conta));
    }

    @Transactional
    public ContaResposta atualizar(Long usuarioId, Long id, ContaRequisicao requisicao) {
        ContaBancaria conta = contaDoUsuario(usuarioId, id);
        DadosConta dados = validar(requisicao);
        conta.atualizar(dados.banco(), dados.agencia(), dados.numero(), dados.tipo(), dados.saldo());
        return ContaResposta.de(conta);
    }

    @Transactional
    public void excluir(Long usuarioId, Long id) {
        ContaBancaria conta = contaDoUsuario(usuarioId, id);
        if (receitas.existsByContaId(id) || despesas.existsByContaId(id)) {
            throw new ConflitoException("A conta possui lançamentos e não pode ser excluída.");
        }
        contas.delete(conta);
    }

    @Transactional(readOnly = true)
    public ContaBancaria contaDoUsuario(Long usuarioId, Long contaId) {
        return contas.findByIdAndUsuarioId(contaId, usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Conta bancária não encontrada para o usuário autenticado."));
    }

    private DadosConta validar(ContaRequisicao requisicao) {
        if (requisicao == null) {
            throw new br.com.signalinvest.exception.RegraNegocioException("Informe os dados da conta.");
        }
        return new DadosConta(
                Validacao.texto(requisicao.banco(), "Banco", 100),
                Validacao.texto(requisicao.agencia(), "Agência", 20),
                Validacao.texto(requisicao.numero(), "Número da conta", 30),
                Validacao.texto(requisicao.tipo(), "Tipo da conta", 30),
                Validacao.dinheiro(requisicao.saldo(), "Saldo cadastrado", false)
        );
    }

    private record DadosConta(String banco, String agencia, String numero,
                              String tipo, BigDecimal saldo) {
    }
}
