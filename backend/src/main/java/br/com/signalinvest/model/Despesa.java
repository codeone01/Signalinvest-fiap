package br.com.signalinvest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "TB_DESPESA")
public class Despesa {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "despesa_sequence")
    @SequenceGenerator(name = "despesa_sequence", sequenceName = "SQ_SI_DESPESA", allocationSize = 1)
    @Column(name = "ID_DESPESA", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_CONTA", nullable = false)
    private ContaBancaria conta;

    @Column(name = "DS_DESPESA", nullable = false, length = 200)
    private String descricao;

    @Column(name = "VL_DESPESA", nullable = false, precision = 14, scale = 2)
    private BigDecimal valor;

    @Column(name = "DT_DESPESA", nullable = false)
    private LocalDate data;

    @Column(name = "TP_DESPESA", nullable = false, length = 50)
    private String tipo;

    protected Despesa() {
    }

    public Despesa(Usuario usuario, ContaBancaria conta, String descricao,
                   BigDecimal valor, LocalDate data, String tipo) {
        this.usuario = usuario;
        atualizar(conta, descricao, valor, data, tipo);
    }

    public void atualizar(ContaBancaria conta, String descricao, BigDecimal valor,
                          LocalDate data, String tipo) {
        this.conta = conta;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public ContaBancaria getConta() {
        return conta;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDate getData() {
        return data;
    }

    public String getTipo() {
        return tipo;
    }
}
