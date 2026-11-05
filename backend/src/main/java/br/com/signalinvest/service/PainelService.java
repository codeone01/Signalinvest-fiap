package br.com.signalinvest.service;

import br.com.signalinvest.dto.PainelResposta;
import br.com.signalinvest.repository.ContaBancariaRepository;
import br.com.signalinvest.repository.DespesaRepository;
import br.com.signalinvest.repository.ReceitaRepository;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PainelService {
    private final ContaBancariaRepository contas;
    private final ReceitaRepository receitas;
    private final DespesaRepository despesas;

    public PainelService(ContaBancariaRepository contas, ReceitaRepository receitas,
                         DespesaRepository despesas) {
        this.contas = contas;
        this.receitas = receitas;
        this.despesas = despesas;
    }

    @Transactional(readOnly = true)
    public PainelResposta consultar(Long usuarioId) {
        var contasUsuario = contas.findAllByUsuarioIdOrderByIdDesc(usuarioId);
        BigDecimal saldo = contasUsuario.stream().map(item -> item.getSaldo())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalReceitas = receitas.somarPorUsuario(usuarioId);
        BigDecimal totalDespesas = despesas.somarPorUsuario(usuarioId);
        return new PainelResposta(saldo, totalReceitas, totalDespesas,
                totalReceitas.subtract(totalDespesas), contasUsuario.size());
    }
}
