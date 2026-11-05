package br.com.signalinvest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TB_USUARIO")
public class Usuario {
    @Id
    @Column(name = "ID_USUARIO", nullable = false)
    private Long id;

    @Column(name = "NM_USUARIO", nullable = false, length = 150)
    private String nome;

    @Column(name = "DS_EMAIL", nullable = false, length = 200)
    private String email;

    @Column(name = "DS_SENHA_HASH", nullable = false, length = 255)
    private String senhaHash;

    @Column(name = "ST_USUARIO", nullable = false)
    private String status;

    protected Usuario() {
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public String getStatus() {
        return status;
    }
}
