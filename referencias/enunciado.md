ATIVIDADE: FINALIZANDO O FINTECH COM CHAVE DE OURO!
Atenção! Atividade Avaliativa:

- Verifique se o arquivo do upload está correto, não é possível enviar um outro arquivo após o fechamento da entrega na plataforma ou após a correção do professor.

- Não deixe para realizar a entrega da atividade nos últimos minutos do prazo, você pode ter algum problema e perder a entrega. As entregas são realizadas apenas pela plataforma.

- Não disponibilize a resposta da sua atividade em grupos de WhatsApp, Discord, Microsoft Teams, pois pode gerar plágio e zerar a atividade para todos.

- Você tem um período máximo de 15 dias após a publicação da nota para solicitar a revisão da correção.

 

Chegou o momento de consolidar todo o aprendizado desenvolvido ao longo do ano!

Nesta atividade, você e seu grupo deverão entregar um projeto completo, integrando os conhecimentos de Java, Oracle, Spring Boot e ReactJS, com as boas práticas trabalhadas nas fases.

 

Requisitos técnicos - Backend:

1)    Criar classes de modelos que representem o seu projeto do Fintech que já desenvolveu até agora – Entidades.

2)    Implementar Repository com JPA para cada entidade.

3)    Implementar a camada de service com as regras de negócio do seu Fintech, para cada repository.

4)    Criar endpoints REST utilizando RestController para conectar à camada de service, com os métodos:

a)    GET (consultar);

b)    POST (criar);

c)    PUT (atualizar);

d)    DELETE (deletar).

5)    Implementar corretamente o código de status esperado para verbo HTTP para as Rest Controllers.

6)    Criar as tabelas na instância Oracle da FIAP, de acordo com as entidades do seu projeto Fintech.

7)    Conectar obrigatoriamente à instância Oracle da FIAP.

8)    Lembre-se que não existe somente uma classe/entidade. Fez as camadas Model, Repository, Service e Controller para "Gastos"? Faça para outras do Fintech também! Faça para, no mínimo, 3 entidades.

 

Requisitos técnicos - Frontend:

1)    Estruturar o projeto com componentização.

2)    Implementar rotas de navegação entre páginas (SPA).

3)    Utilizar props e estado (hooks) para comunicação entre componentes.

4)    Criar página de autenticação (login).

5)    Criar página inicial e página de erro personalizada.

6)    Criar páginas correspondentes a cada controller do backend.

Exemplo: criar páginas para consultar, inserir, atualizar e remover gasto de um usuário.
Lembre-se que existem mais classes envolvidas no Fintech; não faça só para uma! Faça para, no mínimo, 3 entidades.
7)    Conectar o frontend ao backend via consumo de APIs REST.

 

Entregáveis
1)    Pasta final contendo:

a)    projeto backend (Java / Spring Boot), conforme mencionado no requisito técnico acima;

b)    projeto frontend (ReactJS), conforme mencionado no requisito técnico acima.

Importante: não incluir a pasta "node_modules" do projeto Frontend no arquivo de entrega. As dependências deverão estar corretamente declaradas no arquivo package.json e package-lock.json.
c)    Arquivo de documentação (README.md), contendo:

instruções de inicialização do projeto (backend e frontend);
dados de autenticação do usuário de teste (login).
2)    Realizar o projeto para, no mínimo, 3 entidades (backend e frontend).

3)    Entregar a pasta final compactada no formato .zip na plataforma, contendo os entregáveis.

 

Observações importantes:

1)    É obrigatório o uso da instância Oracle da FIAP.

2)    Não utilizar frameworks ou bibliotecas além das apresentadas no curso.

3)    Testar o projeto de ponta a ponta antes da entrega para garantir o envio correto.

4)    Exportar corretamente os projetos e zipar corretamente antes de entregar na plataforma.

5)    Realizar o projeto para, no mínimo, 3 entidades (backend e frontend).

6)    Não incluir a pasta "node_modules" no arquivo .zip final. Essa pasta é gerada automaticamente durante a instalação das dependências e pode ser recriada a partir dos arquivos package.json e package-lock.json. Antes da entrega, valide se as dependências do projeto podem ser instaladas corretamente por meio do comando "npm install" e, em seguida, gere o arquivo .zip sem a pasta "node_modules"