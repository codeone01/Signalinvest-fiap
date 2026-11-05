# SignalInvest — Integração Java 17 + Oracle FIAP

## Integrantes
- José Eduardo Silva de Azevedo — RM 99642
- Marcello Loureiro Costa — RM 570621
- Mario Henrique Silverio de Souza — RM 573501
- Eduardo Vieira Da Silva — RM 571602
- Efraim Martins Buriti — RM 562586

## Escopo
Continuação da modelagem de `Signalinvest_Atividade_SQL_Oracle_v2(1).pdf`, seções 3.2.1–3.2.3. Implementa ContaBancaria, Receita e Despesa com os mesmos campos, tipos e relacionamentos. Cada DAO implementa `insert()` e `getAll()` com JDBC real, sem banco simulado. Os modelos são records imutáveis do Java 17, com acessores públicos e validação. O código também funciona no Java 21.

## Abrir e executar no IntelliJ
1. Extraia o ZIP. Abra a pasta GRUPO_SIGNALINVEST ou seu `pom.xml` como projeto Maven.
2. Selecione JDK 17 (ou 21) em Project Structure e aguarde a sincronização Maven. O IntelliJ pode usar o Maven integrado para baixar ojdbc11.
3. No SQL Developer, conecte-se **à instância Oracle disponibilizada pela FIAP**, com sua conta acadêmica. Não substitua por Oracle local ou outro banco.
4. Confira se TB_USUARIO do projeto anterior já existe e se há um usuário de teste válido. Este projeto não inventa nem substitui a estrutura dessa tabela. Se o schema estiver vazio, use `sql/00_usuario_teste.sql`, que cria o recorte de TB_USUARIO usado nesta integração e um usuário fictício ID 1. Se a tabela já existir, preserve sua estrutura e utilize um usuário válido. Não repita o script na conta RM570621: já foi executado. Consulte `sql/03_verificacao.sql` para obter seu ID.
5. Execute de `sql/01_tabelas.sql` apenas os CREATE TABLE das tabelas ausentes. Se já existem, confira a compatibilidade dos campos e restrições. Execute `sql/02_sequences.sql` com F5. Não crie sequences enquanto outras sessões estão inserindo IDs manualmente. Se uma sequence já existe, confira que seu próximo valor não colide com os IDs atuais; não a reinicialize sem avaliar os dados.
6. Abra `br.com.signalinvest.Teste`. Em Run > Edit Configurations, configure as variáveis abaixo no campo Environment variables. Use host, porta e SID/service fornecidos oficialmente pela FIAP, sem adivinhar valores.

| Variável | Conteúdo |
|---|---|
| FIAP_ORACLE_URL | URL JDBC da instância FIAP: `jdbc:oracle:thin:@HOST:PORTA:SID` ou `jdbc:oracle:thin:@//HOST:PORTA/SERVICE` |
| FIAP_ORACLE_USER | Seu usuário Oracle acadêmico |
| FIAP_ORACLE_PASSWORD | Sua senha Oracle |
| FIAP_TEST_USER_ID | ID de um usuário existente em TB_USUARIO |

7. Execute `Teste.main()`. O programa insere 5 contas + 5 receitas + 5 despesas, consulta as três tabelas e confere **todos os atributos** dos 15 novos objetos. Mostra os registros novos retornados. `getAll()` retorna todos os registros da tabela, inclusive os anteriores.
8. O COMMIT só ocorre após as três verificações. Em erro ocorre ROLLBACK e saída com código 1. Cada nova execução bem-sucedida adiciona 15 registros usando sequences; os IDs consumidos não são devolvidos no rollback.
9. Execute as consultas de `sql/03_verificacao.sql` após o Java para confirmar a persistência em outra sessão.

Alternativa com Maven instalado e as variáveis configuradas: `mvn clean compile exec:java`.

## Organização
- `src/main/java/br/com/signalinvest/model`: três entidades, BigDecimal para dinheiro e LocalDate para datas.
- `dao`: três DAOs com SELECT explícito, PreparedStatement, List<T>, ResultSet e try-with-resources; DaoException preserva a SQLException original.
- `infra`: ConnectionFactory, sem credenciais no código.
- `Teste`: teste de integração com transação e verificação de conteúdo.
- `sql`: DDL compatível com a modelagem anterior, sequences e consultas de conferência.
- `docs`: requisitos, roteiro de validação e estado real dos testes.

## Decisões
A conexão é compartilhada entre os DAOs para garantir atomicidade. Os DAOs fecham statements e result sets, e a classe Teste fecha a conexão. Nenhum DAO confirma isoladamente a transação. Os erros não são convertidos em lista vazia ou mensagem de sucesso. As contas fictícias de teste não movimentam saldo automaticamente: o escopo desta atividade é cadastro e consulta, não conciliação bancária. Não há dados pessoais reais nos exemplos.

## Entrega
Nome solicitado pelo grupo: **GRUPO_SIGNALINVEST.zip**. O ZIP contém a pasta do projeto Maven, diretamente importável no IntelliJ. Depois da validação real na FIAP, exporte/compacte novamente a pasta pelo IntelliJ, mantendo esse nome, caso a disciplina exija especificamente a exportação pelo IDE. Apenas um integrante envia na FIAP ON.

**Validação realizada pelo aluno:** execução no IntelliJ com Java 17, na instância Oracle FIAP e schema RM570621, com 5 registros por entidade, objetos conferidos, COMMIT e código de saída 0. Consulta posterior no SQL Developer confirmou os 15 registros. Consulte `docs/EVIDENCIAS.md` e `docs/resultado-oracle-fiap.txt`. Credenciais permanecem externas; configure seu acesso antes de executar.
