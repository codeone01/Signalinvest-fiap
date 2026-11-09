# Handoff — Signalinvest FIAP

## Estado atual

- Repositório: `https://github.com/codeone01/Signalinvest-fiap.git`
- Branch principal: `main`
- Backend: Spring Boot 3.5.5, Java 21, Spring Web e Spring Data JPA.
- Frontend: React, TypeScript, Vite, React Router e `fetch`.
- CRUDs: Conta Bancária, Receita e Despesa.
- Autenticação: sessão HTTP no backend, cookie `SIGNALINVEST_SESSION` e senha PBKDF2.
- Hibernate: `ddl-auto=validate`; o Spring não cria nem altera tabelas.
- Nenhuma credencial Oracle foi versionada.

## Pré-requisitos no novo computador

Instale:

1. JDK 21.
2. Maven 3.9 ou superior.
3. Node.js 22 e npm.
4. SQL Developer, se for executar a inspeção Oracle.

Confirme:

```powershell
java -version
mvn -version
node --version
npm --version
```

Java e Maven devem mostrar Java 21.

## Baixar o projeto

```powershell
git clone https://github.com/codeone01/Signalinvest-fiap.git
cd Signalinvest-fiap
```

## Preparar o Oracle

1. Conecte-se à conta acadêmica da FIAP no SQL Developer.
2. Execute somente `backend/sql/00_inspecao_schema.sql` com **F5**.
3. Confirme `TB_USUARIO`, `TB_CONTA_BANCARIA`, `TB_RECEITA` e `TB_DESPESA`.
4. Confira colunas, tipos, chaves, identity, sequences, triggers e maiores IDs.
5. Não execute scripts de criação antes dessa conferência.
6. Se o schema estiver compatível, execute `backend/sql/03_usuario_demonstracao.sql` e depois `backend/sql/04_verificacao.sql`.
7. Só use `backend/sql/01_criar_tabelas_schema_vazio.sql` em schema realmente vazio.
8. Só use `backend/sql/02_criar_sequences_se_necessario.sql` se a inspeção confirmar que as sequences são necessárias e estão ausentes.

Os scripts não devem ser executados contra outro banco ou contra uma conta sem autorização.

## Configurar o backend

No PowerShell, configure os valores oficiais da FIAP apenas na sessão atual. Não grave senha em arquivos nem no Git:

```powershell
$env:FIAP_ORACLE_URL = 'jdbc:oracle:thin:@//HOST:PORTA/SERVICE'
$env:FIAP_ORACLE_USER = 'USUARIO_FIAP'
$env:FIAP_ORACLE_PASSWORD = 'SENHA_FIAP'
$env:FRONTEND_ORIGEM = 'http://localhost:5173'
```

Para SID, use o formato fornecido pela FIAP, por exemplo:

```text
jdbc:oracle:thin:@HOST:PORTA:SID
```

Como alternativa, copie `backend/.env.local.example` para `backend/.env.local`, preencha os valores Oracle somente localmente e execute `./start-backend.ps1`. O arquivo local é ignorado pelo Git.

Inicie:

```powershell
cd backend
mvn test
mvn spring-boot:run
```

O backend deve ficar em `http://localhost:8080`.

## Configurar o frontend

Em outro terminal:

```powershell
cd frontend
npm install
npm run dev
```

Acesse `http://localhost:5173`.

## Roteiro de validação

1. Abra `/contas` sem sessão e confirme o redirecionamento para login.
2. Use o usuário demonstrativo documentado no `README.md`.
3. Crie, edite, consulte e exclua uma conta.
4. Crie, edite, consulte e exclua receitas e despesas.
5. Confirme no painel o saldo cadastrado, total de receitas, total de despesas e resultado.
6. Verifique que lançamentos não alteram `VL_SALDO`.
7. Teste valor zero.
8. Teste acesso a conta de outro usuário.
9. Teste edição pelo ID da URL.
10. Tente excluir conta com lançamentos e confirme HTTP 409.

## Validações já realizadas

- Backend compilado com Java 21.0.13.
- 6 testes automatizados aprovados.
- Frontend compilado com Vite.
- `npm install` sem vulnerabilidades reportadas.
- Nenhuma validação foi executada contra a instância Oracle da FIAP neste ambiente porque as credenciais não estavam disponíveis ao agente.

## Arquivos importantes

- `README.md`: documentação principal.
- `backend/sql/00_inspecao_schema.sql`: inspeção somente leitura.
- `backend/sql/03_usuario_demonstracao.sql`: usuário demonstrativo idempotente.
- `backend/src/main/resources/application.properties`: variáveis esperadas.
- `frontend/src/api.ts`: chamadas HTTP com cookie de sessão.

