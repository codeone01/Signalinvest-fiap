package br.com.signalinvest.dao;
import br.com.signalinvest.model.Despesa;
import java.sql.*;
import java.util.*;
/** Recebe a conexão da transação; não fecha nem confirma a conexão do chamador. */
public final class DespesaDAO {
 private final Connection connection;
 public DespesaDAO(Connection connection) { this.connection=Objects.requireNonNull(connection); }
 public void insert(Despesa value) {
  Objects.requireNonNull(value);
  String sql="INSERT INTO TB_DESPESA (ID_DESPESA, ID_USUARIO, ID_CONTA, DS_DESPESA, VL_DESPESA, DT_DESPESA, TP_DESPESA) VALUES (?, ?, ?, ?, ?, ?, ?)";
  try(PreparedStatement ps=connection.prepareStatement(sql)) {
   ps.setLong(1, value.id());
   ps.setLong(2, value.idUsuario());
   ps.setLong(3, value.idConta());
   ps.setString(4, value.descricao());
   ps.setBigDecimal(5, value.valor());
   ps.setDate(6, java.sql.Date.valueOf(value.data()));
   ps.setString(7, value.tipo());
   if(ps.executeUpdate()!=1) throw new SQLException("INSERT não afetou exatamente uma linha");
  } catch(SQLException e) { throw new DaoException("Inserção de Despesa",e); }
 }
 public List<Despesa> getAll() {
  List<Despesa> result=new ArrayList<>();
  String sql="SELECT ID_DESPESA, ID_USUARIO, ID_CONTA, DS_DESPESA, VL_DESPESA, DT_DESPESA, TP_DESPESA FROM TB_DESPESA ORDER BY ID_DESPESA";
  try(PreparedStatement ps=connection.prepareStatement(sql); ResultSet rs=ps.executeQuery()) {
   while(rs.next()) result.add(new Despesa(rs.getLong("ID_DESPESA"), rs.getLong("ID_USUARIO"), rs.getLong("ID_CONTA"), rs.getString("DS_DESPESA"), rs.getBigDecimal("VL_DESPESA"), rs.getDate("DT_DESPESA").toLocalDate(), rs.getString("TP_DESPESA")));
   return result;
  } catch(SQLException e) { throw new DaoException("Consulta de Despesa",e); }
 }
}
