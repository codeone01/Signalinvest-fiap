package br.com.signalinvest.service;

import br.com.signalinvest.dto.LancamentoRequisicao;
import br.com.signalinvest.dto.ReceitaResposta;
import br.com.signalinvest.exception.RecursoNaoEncontradoException;
import br.com.signalinvest.exception.RegraNegocioException;
import br.com.signalinvest.model.ContaBancaria;
import br.com.signalinvest.model.Receita;
import br.com.signalinvest.model.Usuario;
import br.com.signalinvest.repository.ReceitaRepository;
import br.com.signalinvest.repository.UsuarioRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReceitaService {
    private final ReceitaRepository receitas;
    private final UsuarioRepository usuarios;
    private final ContaBancariaService contas;

    public ReceitaService(ReceitaRepository receitas, UsuarioRepository usuarios,
                          ContaBancariaService contas) {
        this.receitas = receitas;
        this.usuarios = usuarios;
        this.contas = contas;
    }

    @Transactional(readOnly = true)
    public List<ReceitaResposta> listar(Long usuarioId) {
        return receitas.findAllByUsuarioIdOrderByDataDescIdDesc(usuarioId).stream()
                .map(ReceitaResposta::de).toList();
    }

    @Transactional(readOnly = true)
    public ReceitaResposta buscar(Long usuarioId, Long id) {
        return ReceitaResposta.de(receitaDoUsuario(usuarioId, id));
    }

    @Transactional
    public ReceitaResposta criar(Long usuarioId, LancamentoRequisicao requisicao) {
        Usuario usuario = usuarios.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário da sessão não encontrado."));
        DadosLancamento dados = validar(usuarioId, requisicao);
        Receita receita = new Receita(usuario, dados.conta(), dados.descricao(),
                dados.valor(), dados.data(), dados.tipo());
        return ReceitaResposta.de(receitas.save(receita));
    }

    @Transactional
    public ReceitaResposta atualizar(Long usuarioId, Long id, LancamentoRequisicao requisicao) {
        Receita receita = receitaDoUsuario(usuarioId, id);
        DadosLancamento dados = validar(usuarioId, requisicao);
        receita.atualizar(dados.conta(), dados.descricao(), dados.valor(), dados.data(), dados.tipo());
        return ReceitaResposta.de(receita);
    }

    @Transactional
    public void excluir(Long usuarioId, Long id) {
        receitas.delete(receitaDoUsuario(usuarioId, id));
    }

    private Receita receitaDoUsuario(Long usuarioId, Long id) {
        return receitas.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Receita não encontrada para o usuário autenticado."));
    }

    private DadosLancamento validar(Long usuarioId, LancamentoRequisicao requisicao) {
        if (requisicao == null) {
            throw new RegraNegocioException("Informe os dados da receita.");
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
