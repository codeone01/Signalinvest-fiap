package br.com.signalinvest.model;
import java.math.BigDecimal;
import java.time.LocalDate;
/** Objeto imutável; dinheiro usa BigDecimal e datas usam LocalDate. */
public record Despesa(long id, long idUsuario, long idConta, String descricao, BigDecimal valor, LocalDate data, String tipo) {
 public Despesa {
  if(id<=0 || idUsuario<=0) throw new IllegalArgumentException("IDs devem ser positivos");
  if(idConta<=0) throw new IllegalArgumentException("Conta inválida");
  if(descricao==null || descricao.isBlank() || descricao.length()>200) throw new IllegalArgumentException("descricao inválido");
  if(valor==null || valor.scale()>2 || valor.precision()-valor.scale()>12 || valor.signum()<=0) throw new IllegalArgumentException("valor inválido para NUMBER(14,2)");
  java.util.Objects.requireNonNull(data, "data");
  if(tipo==null || tipo.isBlank() || tipo.length()>50) throw new IllegalArgumentException("tipo inválido");
  valor = valor.setScale(2);
 }
}
