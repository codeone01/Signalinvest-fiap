package br.com.signalinvest.dao;
import br.com.signalinvest.model.Receita;
import java.sql.*;
import java.util.*;
/** Recebe a conexão da transação; não fecha nem confirma a conexão do chamador. */
public final class ReceitaDAO {
 private final Connection connection;
 public ReceitaDAO(Connection connection) { this.connection=Objects.requireNonNull(connection); }
 public void insert(Receita value) {
  Objects.requireNonNull(value);
  String sql="INSERT INTO TB_RECEITA (ID_RECEITA, ID_USUARIO, ID_CONTA, DS_RECEITA, VL_RECEITA, DT_RECEITA, TP_RECEITA) VALUES (?, ?, ?, ?, ?, ?, ?)";
  try(PreparedStatement ps=connection.prepareStatement(sql)) {
   ps.setLong(1, value.id());
   ps.setLong(2, value.idUsuario());
   ps.setLong(3, value.idConta());
   ps.setString(4, value.descricao());
   ps.setBigDecimal(5, value.valor());
   ps.setDate(6, java.sql.Date.valueOf(value.data()));
   ps.setString(7, value.tipo());
   if(ps.executeUpdate()!=1) throw new SQLException("INSERT não afetou exatamente uma linha");
  } catch(SQLException e) { throw new DaoException("Inserção de Receita",e); }
 }
 public List<Receita> getAll() {
  List<Receita> result=new ArrayList<>();
  String sql="SELECT ID_RECEITA, ID_USUARIO, ID_CONTA, DS_RECEITA, VL_RECEITA, DT_RECEITA, TP_RECEITA FROM TB_RECEITA ORDER BY ID_RECEITA";
  try(PreparedStatement ps=connection.prepareStatement(sql); ResultSet rs=ps.executeQuery()) {
   while(rs.next()) result.add(new Receita(rs.getLong("ID_RECEITA"), rs.getLong("ID_USUARIO"), rs.getLong("ID_CONTA"), rs.getString("DS_RECEITA"), rs.getBigDecimal("VL_RECEITA"), rs.getDate("DT_RECEITA").toLocalDate(), rs.getString("TP_RECEITA")));
   return result;
  } catch(SQLException e) { throw new DaoException("Consulta de Receita",e); }
 }
}
