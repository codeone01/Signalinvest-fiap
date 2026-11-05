package br.com.signalinvest;
import br.com.signalinvest.dao.*;
import br.com.signalinvest.infra.ConnectionFactory;
import br.com.signalinvest.model.*;
import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
/** Executar após os scripts SQL, usando a instância Oracle da FIAP. */
public final class Teste {
 private Teste() {}
 public static void main(String[] args) {
  try(Connection c=ConnectionFactory.open()) {
   c.setAutoCommit(false);
   try {
    iniciarTestes(c);
    c.commit();
    System.out.println("SUCESSO: 5 contas, 5 receitas e 5 despesas verificadas; COMMIT efetuado.");
   } catch(SQLException | RuntimeException e) {
    try { c.rollback(); System.err.println("ROLLBACK: nenhum cadastro desta execução foi confirmado."); }
    catch(SQLException rollback) { e.addSuppressed(rollback); }
    throw e;
   }
  } catch(SQLException | RuntimeException e) {
   System.err.println("FALHA: " + e.getMessage());
   if(e instanceof SQLException sql) System.err.println("Oracle="+sql.getErrorCode()+", SQLState="+sql.getSQLState());
   System.exit(1);
  }
 }
 public static void iniciarTestes(Connection c) throws SQLException {
  long usuario=Long.parseLong(ConnectionFactory.required("FIAP_TEST_USER_ID"));
  try(PreparedStatement ps=c.prepareStatement("SELECT ID_USUARIO FROM TB_USUARIO WHERE ID_USUARIO = ?")) {
   ps.setLong(1,usuario);
   try(ResultSet rs=ps.executeQuery()) {
    if(!rs.next()) throw new IllegalArgumentException("FIAP_TEST_USER_ID deve identificar um usuário existente em TB_USUARIO.");
   }
  }
  ContaBancariaDAO contas=new ContaBancariaDAO(c);
  ReceitaDAO receitas=new ReceitaDAO(c);
  DespesaDAO despesas=new DespesaDAO(c);
  List<ContaBancaria> contasEsperadas=new ArrayList<>();
  List<Receita> receitasEsperadas=new ArrayList<>();
  List<Despesa> despesasEsperadas=new ArrayList<>();
  for(int i=1;i<=5;i++) {
   long contaId=nextId(c,"SQ_SI_CONTA");
   ContaBancaria conta=new ContaBancaria(contaId,usuario,"Banco teste "+i,"0001",Long.toString(contaId),"CORRENTE",new BigDecimal("1000.00"));
   Receita receita=new Receita(nextId(c,"SQ_SI_RECEITA"),usuario,contaId,"Receita JDBC "+i,new BigDecimal("250.00").multiply(BigDecimal.valueOf(i)),LocalDate.of(2026,10,i),"SERVICOS");
   Despesa despesa=new Despesa(nextId(c,"SQ_SI_DESPESA"),usuario,contaId,"Despesa JDBC "+i,new BigDecimal("50.00").multiply(BigDecimal.valueOf(i)),LocalDate.of(2026,10,i),"OPERACIONAL");
   contas.insert(conta); receitas.insert(receita); despesas.insert(despesa);
   contasEsperadas.add(conta); receitasEsperadas.add(receita); despesasEsperadas.add(despesa);
  }
  verificar("Contas",contasEsperadas,contas.getAll());
  verificar("Receitas",receitasEsperadas,receitas.getAll());
  verificar("Despesas",despesasEsperadas,despesas.getAll());
 }
 private static long nextId(Connection c,String sequence) throws SQLException {
  // Nome restrito a constantes internas; valores de negócio usam PreparedStatement.
  if(!Set.of("SQ_SI_CONTA","SQ_SI_RECEITA","SQ_SI_DESPESA").contains(sequence)) throw new IllegalArgumentException("Sequence inválida");
  try(PreparedStatement ps=c.prepareStatement("SELECT "+sequence+".NEXTVAL FROM DUAL"); ResultSet rs=ps.executeQuery()) {
   if(!rs.next()) throw new SQLException("Sequence não retornou valor");
   return rs.getLong(1);
  }
 }
 private static <T> void verificar(String entidade,List<T> expected,List<T> actual) {
  // Compara todos os atributos dos 5 objetos; aceita registros anteriores na tabela.
  for(T value:expected) if(!actual.contains(value)) throw new IllegalStateException(entidade+": objeto ausente ou divergente: "+value);
  System.out.println(entidade+": SELECT retornou "+actual.size()+" registros; 5 novos objetos conferidos:");
  expected.forEach(System.out::println);
 }
}
