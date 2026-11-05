package br.com.signalinvest.dao;
import br.com.signalinvest.model.ContaBancaria;
import java.sql.*;
import java.util.*;
/** Recebe a conexão da transação; não fecha nem confirma a conexão do chamador. */
public final class ContaBancariaDAO {
 private final Connection connection;
 public ContaBancariaDAO(Connection connection) { this.connection=Objects.requireNonNull(connection); }
 public void insert(ContaBancaria value) {
  Objects.requireNonNull(value);
  String sql="INSERT INTO TB_CONTA_BANCARIA (ID_CONTA, ID_USUARIO, NM_BANCO, NR_AGENCIA, NR_CONTA, TP_CONTA, VL_SALDO) VALUES (?, ?, ?, ?, ?, ?, ?)";
  try(PreparedStatement ps=connection.prepareStatement(sql)) {
   ps.setLong(1, value.id());
   ps.setLong(2, value.idUsuario());
   ps.setString(3, value.banco());
   ps.setString(4, value.agencia());
   ps.setString(5, value.numero());
   ps.setString(6, value.tipo());
   ps.setBigDecimal(7, value.saldo());
   if(ps.executeUpdate()!=1) throw new SQLException("INSERT não afetou exatamente uma linha");
  } catch(SQLException e) { throw new DaoException("Inserção de ContaBancaria",e); }
 }
 public List<ContaBancaria> getAll() {
  List<ContaBancaria> result=new ArrayList<>();
  String sql="SELECT ID_CONTA, ID_USUARIO, NM_BANCO, NR_AGENCIA, NR_CONTA, TP_CONTA, VL_SALDO FROM TB_CONTA_BANCARIA ORDER BY ID_CONTA";
  try(PreparedStatement ps=connection.prepareStatement(sql); ResultSet rs=ps.executeQuery()) {
   while(rs.next()) result.add(new ContaBancaria(rs.getLong("ID_CONTA"), rs.getLong("ID_USUARIO"), rs.getString("NM_BANCO"), rs.getString("NR_AGENCIA"), rs.getString("NR_CONTA"), rs.getString("TP_CONTA"), rs.getBigDecimal("VL_SALDO")));
   return result;
  } catch(SQLException e) { throw new DaoException("Consulta de ContaBancaria",e); }
 }
}
