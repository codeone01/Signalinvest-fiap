# Signalinvest · Guia da Fase 07 da FIAP

**Curso:** Análise e Desenvolvimento de Sistemas — FIAP  
**Atividade:** Finalizando o Fintech com Chave de Ouro!  
**Uso no repositório:** referência para implementar e revisar a entrega. As instruções de execução e o login demonstrativo devem estar também no `README.md` da pasta final.

## Integrantes

Nomes e RMs conforme a capa de *Signalinvest_Atividade_SQL_Oracle_v2.pdf*, p. 1.

| Integrante | RM |
|---|---|
| José Eduardo Silva de Azevedo | 99642 |
| Marcello Loureiro Costa | 570621 |
| Mario Henrique Silverio de Souza | 573501 |
| Eduardo Vieira Da Silva | 571602 |
| Efraim Martins Buriti | 562586 |

## Escopo da entrega

O Signalinvest acompanha investimentos e apoia a análise de ativos e startups. A atividade SQL v2 acrescentou um módulo financeiro ao modelo da Fase 3. Para a entrega final, o recorte recomendado é implementar **Conta Bancária, Receita e Despesa**, do formulário React até a tabela Oracle. O usuário dá suporte à autenticação e identifica o titular dos dados. As três entidades financeiras, por si só, devem atender aos critérios de CRUD.

O enunciado determina as obrigações da entrega. Os capítulos abaixo demonstram técnicas e dão exemplos de versões. As decisões de implementação específicas do Signalinvest estão indicadas como recomendações.

## O que aproveitar de cada capítulo

As páginas abaixo correspondem aos PDFs da Fase 07 enviados pelo grupo.

| Capítulo | Onde consultar | Aplicação no Signalinvest |
|---|---|---|
| 01 · Juntando tudo | § 1.1, a partir da p. 3 | Usar a integração entre disciplinas como roteiro de demonstração: ação na tela, requisição à API e registro no banco. |
| 02 · Estudo de Caso Parte I | HTTP e REST, pp. 13–19; projeto Spring, pp. 20–43; entidade, repositório, serviço, controller e testes, da p. 44 ao fim do estudo | Adaptar o CRUD de produto para cada entidade financeira. Conferir as respostas no Insomnia e a persistência no Oracle SQL Developer, como no exemplo da p. 66. |
| 03 · Node.js e TypeScript | npm e dependências, pp. 18–29; em especial p. 25 | Usar npm no frontend, declarar as dependências e versionar o arquivo de lock. Tipar os dados dos formulários e da API. |
| 04 · ReactJS e Vite | Projeto React + TypeScript, pp. 20–26; estrutura e configuração, pp. 27–32; assets, pp. 34–36 | Criar a SPA com Vite. Aproveitar os arquivos oficiais da marca em componentes de login e navegação. |
| 05 · Estrutura, Props e React Router | Componentização e props, pp. 18–32; CSS, p. 37; rotas, a partir da p. 46 | Compartilhar a estrutura das páginas, passar dados e ações por props e usar rotas com parâmetros para consulta e edição. |
| 06 · State, Effect e Context API | Formulários, pp. 15–18; consulta à API, pp. 19–23; contexto, pp. 24–31 | Usar estado nos campos, efeito para carregar a consulta e, se útil, contexto para compartilhar o usuário autenticado entre páginas. |
| 07 · Next.js | App Router, pp. 8–11; componentes de servidor e cliente, pp. 24–31 | Conteúdo complementar. Para esta entrega, a opção recomendada é a SPA com Vite e React Router dos capítulos 04 e 05. |
| 08 · Next.js, consumo de API e mock | Fetch, pp. 19–22; operações e interface CRUD, pp. 23–42 | Reaproveitar a sequência de chamadas HTTP e o tratamento do formulário. Na entrega, o destino dessas chamadas é a API Spring Boot conectada ao Oracle da FIAP. |
| 09 · Estudo de Caso Parte II | Login, pp. 15–17; painel e operações, pp. 18–32; detalhe, a partir da p. 37 | Adaptar a navegação do estudo de caso: entrar, listar, abrir detalhe, cadastrar, editar e excluir. A validação do login fica no backend Java. |
| 10 · Grand finale | Revisão das disciplinas, pp. 3–7 | Usar a revisão de banco, Java e frontend para conferir se a mesma operação funciona nas três camadas. |

## Tecnologias demonstradas e escolhas para o repositório

| Parte | Evidência no curso | Decisão recomendada |
|---|---|---|
| Backend | Cap. 02: Java 21, Maven, empacotamento JAR, Spring Boot 3.5.5 no exemplo, Spring Web, Spring Data JPA e Oracle Driver (`ojdbc11`). | Usar esse conjunto como referência. O projeto DAO anterior do grupo foi executado com JDK 17; se o grupo adotar o exemplo de Java 21, alinhar o JDK do terminal, o Maven e o `pom.xml`. Registrar as versões efetivamente testadas no README. |
| Persistência | Cap. 02: `@Entity`, `@Table`, `@Id`, repositório que estende `JpaRepository` e exemplo de `GenerationType.SEQUENCE` com `@SequenceGenerator`. | Usar as tabelas e sequências reais do grupo. Conferir no DDL e na instância o nome da sequência e seu incremento antes de definir o gerador JPA. |
| API | Cap. 02: `@Service`, `@RestController`, mapeamentos HTTP e `@ResponseStatus`. | Ter model, repository, service e controller para cada um dos três recursos. O serviço valida as regras e o controller traduz o resultado em HTTP. |
| Frontend | Cap. 04, código-fonte 10: o exemplo declara React e React DOM `^19.1.0`, Vite `^6.3.5` e TypeScript `~5.8.3`. Cap. 05: `react-router-dom` e `BrowserRouter`. | Usar React + TypeScript + Vite, React Router, CSS e `fetch`. Conservar as versões resolvidas no `package-lock.json`. As faixas do exemplo são referências didáticas. |
| Autenticação | Cap. 09, §§ 3.2–3.3: formulário e exemplo de autenticação administrativa em Next.js. | Implementar o fluxo equivalente com React e Spring. Buscar o usuário no Oracle, validar a senha contra o hash e verificar a sessão nas rotas de dados. A escolha do mecanismo deve respeitar as dependências usadas no curso. |

**Regra para dependências.** O enunciado limita bibliotecas e frameworks aos apresentados no curso. Antes de introduzir uma dependência direta, identificar em qual material ela foi usada e qual necessidade do projeto atende. O build final deve funcionar com os arquivos declarados no repositório.

## Dados e regras dos três CRUDs

Fonte do recorte: *Signalinvest_Atividade_SQL_Oracle_v2.pdf*, §§ 3.2.1–3.2.3, pp. 6–7. A modelagem definitiva da Fase 3 explica o domínio geral; o SQL v2 documenta a evolução financeira. Antes de implementar, comparar esse DDL com o schema que o grupo efetivamente usou na fase DAO.

| Recurso | Tabela e chave | Atributos do DDL, além da chave |
|---|---|---|
| Conta bancária | `TB_CONTA_BANCARIA.ID_CONTA` | `ID_USUARIO`, `NM_BANCO`, `NR_AGENCIA`, `NR_CONTA`, `TP_CONTA`, `VL_SALDO` |
| Receita | `TB_RECEITA.ID_RECEITA` | `ID_USUARIO`, `ID_CONTA`, `DS_RECEITA`, `VL_RECEITA`, `DT_RECEITA`, `TP_RECEITA` |
| Despesa | `TB_DESPESA.ID_DESPESA` | `ID_USUARIO`, `ID_CONTA`, `DS_DESPESA`, `VL_DESPESA`, `DT_DESPESA`, `TP_DESPESA` |

- As três tabelas usam campos obrigatórios. O saldo da conta tem valor padrão zero. Os valores usam `NUMBER(14,2)`; usar `BigDecimal` no Java e validar até duas casas decimais. Agência e número da conta são textos: preservar zeros à esquerda.
- Receita e despesa exigem valor maior que zero. Cada lançamento aponta para a combinação `(ID_CONTA, ID_USUARIO)` da conta. No formulário, listar apenas as contas do usuário autenticado. No serviço, conferir a titularidade também na inclusão e na edição.
- O DDL contém um saldo armazenado. Antes de definir se um lançamento altera esse saldo, conferir o comportamento do projeto DAO e se há triggers no banco. Se a regra for atualizar o saldo, cobrir também edição e exclusão na mesma transação, evitando aplicar o efeito duas vezes.
- Para a exclusão de uma conta vinculada, a recomendação é preservar o histórico e devolver conflito com mensagem clara. Considerar também vínculos existentes com investimentos, ainda que o CRUD de investimento fique fora deste recorte.

## Roteiro de aceite para a atividade

| Requisito | Como conferir na entrega |
|---|---|
| Quatro camadas para ao menos três entidades | Localizar entidade JPA, repository, service e controller para cada recurso. Verificar que os endpoints chamam o serviço e que as regras também valem quando a API é usada diretamente. |
| CRUD e status HTTP | Em `/api/contas`, `/api/receitas` e `/api/despesas` (rotas propostas), testar GET de lista e por ID com 200, POST com 201, PUT pelo ID da URL com 200 e recurso atualizado, DELETE com 204 e corpo vazio. Reconsultar o ID excluído e esperar 404. |
| Regras de negócio | Tentar cadastrar receita de valor zero, editar despesa para conta de outro usuário e excluir conta ainda vinculada. Conferir tanto a mensagem quanto os dados que permaneceram no banco. |
| Oracle da FIAP | Criar ou conferir as tabelas no schema do grupo. Configurar conexão por variáveis de ambiente. Pela tela, salvar um registro, conferir no SQL Developer e recarregar a aplicação para demonstrar a persistência. |
| SPA e autenticação | Abrir login, página inicial, páginas dos três recursos e rota desconhecida. Verificar componentes com props e hooks, login inválido, acesso direto a rota protegida e retorno após autenticação. |
| Integração dos dois projetos | Criar uma conta, lançar receita e despesa, editar valores e excluir os registros de teste na ordem correta. Conferir o efeito de cada ação ao voltar à listagem. |
| Documentação e ZIP | Incluir `backend/`, `frontend/` e `README.md`. Documentar como preparar o schema e o usuário de teste, login e senha da aplicação, versões e comandos de inicialização. Manter credenciais Oracle fora do pacote. Incluir `package.json` e `package-lock.json`, excluir `node_modules/`, extrair o ZIP em uma pasta limpa e validar `npm install`, build e execução de ponta a ponta antes do upload. |

**Convenções do grupo.** Escrever a interface, as mensagens de validação, o README e os nomes do domínio em português do Brasil. Preservar os identificadores técnicos das APIs. Usar comentários apenas quando explicarem uma regra ou decisão que o próprio código não torne clara.

**Situação deste guia.** A lista de integrantes, o DDL citado e os exemplos da Fase 07 foram conferidos nos documentos. Os comandos de inicialização, as versões finais, os nomes das sequências no ambiente atual e os resultados de teste devem ser preenchidos ou confirmados a partir da implementação e da instância Oracle da FIAP.
