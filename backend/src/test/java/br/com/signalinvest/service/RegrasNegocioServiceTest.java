package br.com.signalinvest.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.signalinvest.dto.ContaRequisicao;
import br.com.signalinvest.dto.LancamentoRequisicao;
import br.com.signalinvest.exception.ConflitoException;
import br.com.signalinvest.exception.RecursoNaoEncontradoException;
import br.com.signalinvest.exception.RegraNegocioException;
import br.com.signalinvest.model.ContaBancaria;
import br.com.signalinvest.model.Usuario;
import br.com.signalinvest.repository.ContaBancariaRepository;
import br.com.signalinvest.repository.DespesaRepository;
import br.com.signalinvest.repository.ReceitaRepository;
import br.com.signalinvest.repository.UsuarioRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegrasNegocioServiceTest {
    private ContaBancariaRepository contasRepository;
    private ReceitaRepository receitasRepository;
    private DespesaRepository despesasRepository;
    private UsuarioRepository usuariosRepository;
    private ContaBancariaService contas;

    @BeforeEach
    void preparar() {
        contasRepository = mock(ContaBancariaRepository.class);
        receitasRepository = mock(ReceitaRepository.class);
        despesasRepository = mock(DespesaRepository.class);
        usuariosRepository = mock(UsuarioRepository.class);
        contas = new ContaBancariaService(contasRepository, usuariosRepository,
                receitasRepository, despesasRepository);
    }

    @Test
    void rejeitaReceitaComValorZero() {
        Usuario usuario = mock(Usuario.class);
        ContaBancaria conta = mock(ContaBancaria.class);
        when(usuariosRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(contasRepository.findByIdAndUsuarioId(8L, 1L)).thenReturn(Optional.of(conta));
        ReceitaService service = new ReceitaService(receitasRepository, usuariosRepository, contas);

        LancamentoRequisicao requisicao = new LancamentoRequisicao(8L, "Serviço",
                BigDecimal.ZERO, LocalDate.of(2026, 11, 5), "SERVICOS");

        assertThatThrownBy(() -> service.criar(1L, requisicao))
                .isInstanceOf(RegraNegocioException.class)
                .hasMessage("Valor deve ser maior que zero.");
        verify(receitasRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void rejeitaContaQueNaoPertenceAoUsuario() {
        Usuario usuario = mock(Usuario.class);
        when(usuariosRepository.findById(2L)).thenReturn(Optional.of(usuario));
        when(contasRepository.findByIdAndUsuarioId(8L, 2L)).thenReturn(Optional.empty());
        ReceitaService service = new ReceitaService(receitasRepository, usuariosRepository, contas);

        LancamentoRequisicao requisicao = new LancamentoRequisicao(8L, "Serviço",
                new BigDecimal("10.00"), LocalDate.of(2026, 11, 5), "SERVICOS");

        assertThatThrownBy(() -> service.criar(2L, requisicao))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("usuário autenticado");
        verify(receitasRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void impedeExcluirContaComLancamentos() {
        ContaBancaria conta = mock(ContaBancaria.class);
        when(contasRepository.findByIdAndUsuarioId(8L, 1L)).thenReturn(Optional.of(conta));
        when(receitasRepository.existsByContaId(8L)).thenReturn(true);

        assertThatThrownBy(() -> contas.excluir(1L, 8L))
                .isInstanceOf(ConflitoException.class)
                .hasMessageContaining("possui lançamentos");
        verify(contasRepository, never()).delete(conta);
    }

    @Test
    void atualizaAContaIndicadaPeloIdDaUrl() {
        ContaBancaria conta = mock(ContaBancaria.class);
        when(contasRepository.findByIdAndUsuarioId(27L, 1L)).thenReturn(Optional.of(conta));
        ContaRequisicao requisicao = new ContaRequisicao("Banco", "0001", "0099",
                "CORRENTE", new BigDecimal("20.00"));

        contas.atualizar(1L, 27L, requisicao);

        verify(contasRepository).findByIdAndUsuarioId(27L, 1L);
        verify(conta).atualizar("Banco", "0001", "0099", "CORRENTE", new BigDecimal("20.00"));
    }
}
