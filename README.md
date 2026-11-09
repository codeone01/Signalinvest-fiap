# Signalinvest — Projeto final FIAP

Aplicação full stack para gestão de contas bancárias, receitas e despesas. O backend usa sessão HTTP, restringe todos os dados ao usuário autenticado e persiste exclusivamente no Oracle da FIAP. O frontend é uma SPA responsiva em React.

## Tecnologias e versões

- Java 21
- Maven 3.9 ou superior
- Spring Boot 3.5.5
- Spring Web e Spring Data JPA
- Oracle JDBC `ojdbc11` 23.3.0.23.09
- Node.js 22 e npm 11, versões usadas na validação local
- React/React DOM com faixa `^19.1.0` (19.3.1 no lock atual)
- TypeScript 5.8.3 e Vite com faixa `^6.3.5` (6.4.5 no lock atual)
- React Router 7.18.5, CSS e `fetch`

O projeto não usa H2 ou outro banco substituto. O Hibernate está configurado com `spring.jpa.hibernate.ddl-auto=validate`: ao iniciar, ele apenas confere o schema e nunca cria ou recria tabelas.

## Estrutura

```text
backend/     API Spring Boot, testes e scripts Oracle
frontend/    SPA React + TypeScript
referencias/ materiais originais das fases anteriores
```

## 1. Conferir e preparar o Oracle

Conecte o SQL Developer à conta acadêmica da FIAP que será usada pelo backend. Abra `backend/sql/00_inspecao_schema.sql` e execute com **F5**. O script é somente leitura e mostra:

- colunas, tipos, nulabilidade e valores padrão;
- PKs, FKs, `UNIQUE` e `CHECK`;
- colunas `IDENTITY`;
- sequences e seus próximos valores;
- triggers e seus corpos;
- total e maior ID de cada tabela.

Guarde a saída para confirmar que existem `TB_USUARIO`, `TB_CONTA_BANCARIA`, `TB_RECEITA` e `TB_DESPESA`. O DAO anterior documenta que o schema FIAP já utilizou `SQ_SI_CONTA`, `SQ_SI_RECEITA` e `SQ_SI_DESPESA`. Não execute criação de sequences antes de conferir o resultado.

### Schema que já contém as tabelas

1. Execute apenas `00_inspecao_schema.sql`.
2. Se as tabelas e as três sequences já estiverem compatíveis, não execute nenhum DDL.
3. Preserve todos os registros existentes.
4. Opcionalmente, prepare o login demonstrativo com `03_usuario_demonstracao.sql`.
5. Execute `04_verificacao.sql`.

### Schema vazio

1. A tabela `TB_USUARIO` da modelagem anterior deve existir.
2. Execute `01_criar_tabelas_schema_vazio.sql` uma única vez.
3. Execute novamente `00_inspecao_schema.sql`.
4. Somente se as colunas de ID não forem `IDENTITY` e as sequences estiverem ausentes, execute `02_criar_sequences_se_necessario.sql`. O script começa cada sequence em `MAX(ID) + 1` e não altera sequences existentes.
5. Execute `03_usuario_demonstracao.sql` e `04_verificacao.sql`.

Nenhum script contém `DROP` ou limpeza de dados. O script do usuário atualiza somente o hash e o status do e-mail demonstrativo, ou insere esse usuário quando ele ainda não existe.

## 2. Usuário demonstrativo da aplicação

Depois de executar `backend/sql/03_usuario_demonstracao.sql`:

- Login: `demo@signalinvest.example`
- Senha: `Signal@2026`

A senha é armazenada como PBKDF2-HMAC-SHA256 com salt e 210.000 iterações. Usuários existentes não são atualizados. O backend não aceita marcadores antigos ou texto simples como senha.

## 3. Executar o backend

Instale o JDK 21 e confirme que Java e Maven usam a mesma versão:

```powershell
java -version
mvn -version
```

No PowerShell, configure a conexão apenas na sessão atual:

```powershell
$env:FIAP_ORACLE_URL = 'jdbc:oracle:thin:@//HOST:PORTA/SERVICE'
$env:FIAP_ORACLE_USER = 'SEU_USUARIO'
$env:FIAP_ORACLE_PASSWORD = 'SUA_SENHA'
$env:FRONTEND_ORIGEM = 'http://localhost:5173'
cd backend
mvn test
mvn spring-boot:run
```

Para uma URL com SID, use o formato oficial fornecido pela FIAP, como `jdbc:oracle:thin:@HOST:PORTA:SID`. Não grave os valores reais em `application.properties` ou no ZIP.

Como alternativa, copie `backend/.env.local.example` para `backend/.env.local`, preencha os valores Oracle somente localmente e inicie com `./start-backend.ps1`. O arquivo `.env.local` é ignorado pelo Git e nunca é enviado ao frontend.

O backend inicia em `http://localhost:8080`. A sessão expira após 30 minutos. Em ambiente HTTPS, defina também `SESSION_COOKIE_SECURE=true`.

## 4. Executar o frontend

Em outro terminal:

```powershell
cd frontend
npm install
npm run dev
```

Acesse `http://localhost:5173`. O Vite encaminha `/api` ao backend, e todas as chamadas usam `credentials: 'include'` para enviar o cookie de sessão.

Para validar o pacote de produção:

```powershell
npm run build
npm run preview
```

O `package-lock.json` deve acompanhar o `package.json`. Não inclua `frontend/node_modules` nem pastas `target`/`dist` no ZIP final.

## Regras implementadas

- Conta, receita e despesa sempre pertencem ao usuário da sessão; o frontend nunca informa `ID_USUARIO`.
- Consultas, edições e exclusões combinam o ID da URL com o usuário autenticado.
- Valores usam `BigDecimal`; receita e despesa devem ser maiores que zero e ter no máximo duas casas decimais.
- Uma receita ou despesa só pode usar uma conta do usuário autenticado.
- Contas com receitas ou despesas vinculadas retornam conflito e não são excluídas.
- `VL_SALDO` é o saldo cadastrado e editável. Criar, editar ou excluir lançamentos não o altera.
- O painel calcula total de receitas, total de despesas e resultado dos lançamentos diretamente dos registros do usuário.
- `POST` retorna `201` com `Location`; `PUT` retorna `200`; `DELETE` retorna `204`.
- Erros de validação retornam `400`, ausência `404`, sessão inválida `401` e conflito `409`, com mensagem em português.

## Roteiro curto de avaliação

1. Tente abrir `/contas` sem sessão e confirme o redirecionamento ao login.
2. Entre com o usuário demonstrativo.
3. Crie uma conta e confira o `201` e o registro no SQL Developer.
4. Edite a conta; o ID considerado é o da URL.
5. Cadastre uma receita e uma despesa nessa conta.
6. Confira os três totais no painel e confirme que o saldo cadastrado permaneceu igual.
7. Tente cadastrar valor zero e observe a rejeição.
8. Tente usar via API uma conta de outro usuário e observe `404`.
9. Tente excluir a conta vinculada e observe `409`.
10. Exclua os lançamentos, depois a conta, e confirme os `204`.
11. Acesse uma rota inexistente para conferir a página 404.

## Validações desta entrega

Executadas localmente:

- compilação do backend e 6 testes automatizados com Temurin JDK 21.0.13; o `pom.xml` permanece configurado para Java 21;
- PBKDF2, valor zero, conta de outro usuário, ID da URL e bloqueio de exclusão vinculada;
- instalação npm sem vulnerabilidades conhecidas no momento da execução;
- build TypeScript/Vite de produção.

Pendentes no ambiente de entrega:

- executar `00_inspecao_schema.sql` com as credenciais acadêmicas;
- executar a API contra a instância Oracle FIAP;
- confirmar ao vivo as estruturas, sequences, triggers e maiores IDs;
- realizar o roteiro completo pelo navegador contra a Oracle.

As variáveis Oracle não estavam configuradas no terminal usado para esta implementação, portanto nenhum dado da FIAP foi alterado.
