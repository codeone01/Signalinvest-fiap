package br.com.signalinvest.infra;
import java.sql.*;
/** Configuração externa: usar exclusivamente a instância Oracle fornecida pela FIAP. */
public final class ConnectionFactory {
 private ConnectionFactory() {}
 public static String required(String name) {
  String value=System.getenv(name);
  if(value==null || value.isBlank()) throw new IllegalArgumentException("Configure a variável " + name);
  return value;
 }
 public static Connection open() throws SQLException {
  String url=required("FIAP_ORACLE_URL");
  if(!url.startsWith("jdbc:oracle:thin:@")) throw new IllegalArgumentException("Use uma URL JDBC Oracle thin da FIAP.");
  // O aluno deve conferir o host oficial no portal; o prefixo não comprova a origem da instância.
  return DriverManager.getConnection(url,required("FIAP_ORACLE_USER"),required("FIAP_ORACLE_PASSWORD"));
 }
}
