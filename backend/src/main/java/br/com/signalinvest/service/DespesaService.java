package br.com.signalinvest.service;

import br.com.signalinvest.dto.DespesaResposta;
import br.com.signalinvest.dto.LancamentoRequisicao;
import br.com.signalinvest.exception.RecursoNaoEncontradoException;
import br.com.signalinvest.exception.RegraNegocioException;
import br.com.signalinvest.model.ContaBancaria;
import br.com.signalinvest.model.Despesa;
import br.com.signalinvest.model.Usuario;
import br.com.signalinvest.repository.DespesaRepository;
import br.com.signalinvest.repository.UsuarioRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DespesaService {
    private final DespesaRepository despesas;
    private final UsuarioRepository usuarios;
    private final ContaBancariaService contas;

    public DespesaService(DespesaRepository despesas, UsuarioRepository usuarios,
                          ContaBancariaService contas) {
        this.despesas = despesas;
        this.usuarios = usuarios;
        this.contas = contas;
    }

    @Transactional(readOnly = true)
    public List<DespesaResposta> listar(Long usuarioId) {
        return despesas.findAllByUsuarioIdOrderByDataDescIdDesc(usuarioId).stream()
                .map(DespesaResposta::de).toList();
    }

    @Transactional(readOnly = true)
    public DespesaResposta buscar(Long usuarioId, Long id) {
        return DespesaResposta.de(despesaDoUsuario(usuarioId, id));
    }

    @Transactional
    public DespesaResposta criar(Long usuarioId, LancamentoRequisicao requisicao) {
        Usuario usuario = usuarios.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário da sessão não encontrado."));
        DadosLancamento dados = validar(usuarioId, requisicao);
        Despesa despesa = new Despesa(usuario, dados.conta(), dados.descricao(),
                dados.valor(), dados.data(), dados.tipo());
        return DespesaResposta.de(despesas.save(despesa));
    }

    @Transactional
    public DespesaResposta atualizar(Long usuarioId, Long id, LancamentoRequisicao requisicao) {
        Despesa despesa = despesaDoUsuario(usuarioId, id);
        DadosLancamento dados = validar(usuarioId, requisicao);
        despesa.atualizar(dados.conta(), dados.descricao(), dados.valor(), dados.data(), dados.tipo());
        return DespesaResposta.de(despesa);
    }

    @Transactional
    public void excluir(Long usuarioId, Long id) {
        despesas.delete(despesaDoUsuario(usuarioId, id));
    }

    private Despesa despesaDoUsuario(Long usuarioId, Long id) {
        return despesas.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Despesa não encontrada para o usuário autenticado."));
    }

    private DadosLancamento validar(Long usuarioId, LancamentoRequisicao requisicao) {
        if (requisicao == null) {
            throw new RegraNegocioException("Informe os dados da despesa.");
        }
        Long contaId = Validacao.id(requisicao.contaId(), "Conta");
        ContaBancaria conta = contas.contaDoUsuario(usuarioId, contaId);
        return new DadosLancamento(conta,
                Validacao.texto(requisicao.descricao(), "Descrição", 200),
                Validacao.dinheiro(requisicao.valor(), "Valor", true),
                Validacao.data(requisicao.data()),
                Validacao.texto(requisicao.tipo(), "Tipo", 50));
    }

    private record DadosLancamento(ContaBancaria conta, String descricao,
                                   BigDecimal valor, LocalDate data, String tipo) {
    }
}
