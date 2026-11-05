# Validação da integração Oracle FIAP

Execução realizada pelo aluno em 13/10/2026, no IntelliJ IDEA, e comunicada nesta conversa por cópia da saída do console e da consulta SQL. Este registro documenta esses resultados; não representa execução remota pelo assistente.

- Java: Microsoft OpenJDK 17.0.18.8.
- Driver: ojdbc11 23.3.0.23.09.
- Instância: oracle.fiap.com.br:1521:orcl.
- Schema: RM570621 (integrante Marcello Loureiro Costa).
- Usuário da aplicação: ID 1, fictício.
- ContaBancariaDAO: 5 INSERTs e getAll com 5 objetos conferidos.
- ReceitaDAO: 5 INSERTs e getAll com 5 objetos conferidos.
- DespesaDAO: 5 INSERTs e getAll com 5 objetos conferidos.
- Todos os atributos dos 15 novos objetos comparados pelo programa.
- COMMIT efetuado; processo finalizado com código 0.
- Consulta posterior no SQL Developer confirmou 5 registros em cada tabela, em outra conexão.

## Correção aplicada
O primeiro teste encontrou diferença de escala no BigDecimal (1000 versus 1000.00). O programa executou rollback. Os três modelos agora normalizam valores monetários com setScale(2), depois das validações, preservando duas casas decimais sem arredondar valores inválidos. A execução seguinte passou com IDs 6 a 10. A consulta retornou somente os cinco registros confirmados por entidade, compatível com o rollback anterior.

## Tratamento de falhas observado
- Configuração ausente: mensagem de erro e saída 1.
- Falha na conferência: rollback e saída 1.
- Indisponibilidade do servidor e tabela inexistente: tratamento implementado, mas sem teste de integração específico registrado.

Saída da execução e consulta em resultado-oracle-fiap.txt. Não há senha neste pacote.
