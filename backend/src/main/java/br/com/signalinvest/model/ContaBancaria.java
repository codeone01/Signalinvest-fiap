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

@Entity
@Table(name = "TB_CONTA_BANCARIA")
public class ContaBancaria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conta_sequence")
    @SequenceGenerator(name = "conta_sequence", sequenceName = "SQ_SI_CONTA", allocationSize = 1)
    @Column(name = "ID_CONTA", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    @Column(name = "NM_BANCO", nullable = false, length = 100)
    private String banco;

    @Column(name = "NR_AGENCIA", nullable = false, length = 20)
    private String agencia;

    @Column(name = "NR_CONTA", nullable = false, length = 30)
    private String numero;

    @Column(name = "TP_CONTA", nullable = false, length = 30)
    private String tipo;

    @Column(name = "VL_SALDO", nullable = false, precision = 14, scale = 2)
    private BigDecimal saldo;

    protected ContaBancaria() {
    }

    public ContaBancaria(Usuario usuario, String banco, String agencia, String numero,
                         String tipo, BigDecimal saldo) {
        this.usuario = usuario;
        atualizar(banco, agencia, numero, tipo, saldo);
    }

    public void atualizar(String banco, String agencia, String numero, String tipo, BigDecimal saldo) {
        this.banco = banco;
        this.agencia = agencia;
        this.numero = numero;
        this.tipo = tipo;
        this.saldo = saldo;
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getBanco() {
        return banco;
    }

    public String getAgencia() {
        return agencia;
    }

    public String getNumero() {
        return numero;
    }

    public String getTipo() {
        return tipo;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }
}
