package br.com.signalinvest.dao;
public class DaoException extends RuntimeException {
 private static final long serialVersionUID=1L;
 public DaoException(String operation, java.sql.SQLException cause) {
  super(operation + " falhou (Oracle=" + cause.getErrorCode() + ", SQLState=" + cause.getSQLState() + "). Confira conexão, permissões e estrutura da tabela.", cause);
 }
}
