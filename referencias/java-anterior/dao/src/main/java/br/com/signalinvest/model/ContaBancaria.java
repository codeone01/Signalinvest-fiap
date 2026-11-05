package br.com.signalinvest.model;
import java.math.BigDecimal;
import java.time.LocalDate;
/** Objeto imutável; dinheiro usa BigDecimal e datas usam LocalDate. */
public record ContaBancaria(long id, long idUsuario, String banco, String agencia, String numero, String tipo, BigDecimal saldo) {
 public ContaBancaria {
  if(id<=0 || idUsuario<=0) throw new IllegalArgumentException("IDs devem ser positivos");
  if(banco==null || banco.isBlank() || banco.length()>100) throw new IllegalArgumentException("banco inválido");
  if(agencia==null || agencia.isBlank() || agencia.length()>20) throw new IllegalArgumentException("agencia inválido");
  if(numero==null || numero.isBlank() || numero.length()>30) throw new IllegalArgumentException("numero inválido");
  if(tipo==null || tipo.isBlank() || tipo.length()>30) throw new IllegalArgumentException("tipo inválido");
  if(saldo==null || saldo.scale()>2 || saldo.precision()-saldo.scale()>12) throw new IllegalArgumentException("saldo inválido para NUMBER(14,2)");
  saldo = saldo.setScale(2);
 }
}
