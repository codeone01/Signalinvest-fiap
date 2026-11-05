# Rastreabilidade

| Exigência | Implementação |
|---|---|
| Java 17 ou 21 | pom.xml: release 17 |
| Oracle | ojdbc11 e ConnectionFactory |
| Instância FIAP | Configuração externa; execução real na FIAP informada pelo aluno, documentada em EVIDENCIAS.md |
| Modelagem anterior | ContaBancaria, Receita, Despesa; mesmo DDL da atividade SQL v2 |
| No mínimo 3 entidades | 3 modelos e 3 DAOs |
| getAll SELECT e lista de objetos | Método público em cada DAO, List<T> |
| insert INSERT | Método público em cada DAO com parâmetros JDBC |
| Pelo menos 5 registros | 5 por entidade em Teste.iniciarTestes |
| Classe de teste e main | Teste.main e iniciarTestes |
| Consulta após cadastro | getAll de cada DAO após os 15 INSERTs |
| Apresentar e conferir resultados | verificar: comparação dos objetos e impressão |
| Try-catch | DAOs, conexão, transação e rollback |
| Exportação IntelliJ | Projeto Maven importável; etapa final pelo aluno no IDE |
| ZIP | GRUPO_SIGNALINVEST.zip |

Não é possível garantir nota máxima sem execução real na instância obrigatória e avaliação do professor.
