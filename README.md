Projeto desenvolvido por: 
- Rilson Joás
- Guilherme Oliveira
- Daniel Feitosa

## **1. Visão Geral do Projeto**

O sistema YouFit é uma plataforma de gerenciamento de treinos e dietas, projetada para ajudar usuários a atingir seus objetivos de saúde e fitness. Inspirado em aplicativos semelhantes, o sistema oferece:

* **Planos de treino personalizados:** Adaptados às metas (perda de peso, ganho de massa, condicionamento), nível de experiência e preferências do usuário.
* **Planos de dieta personalizados:**  Criados com base nas necessidades calóricas e preferências alimentares, com sugestões de refeições.
* **Acompanhamento de progresso:**  Registro de atividades, refeições, métricas corporais (peso, IMC) e visualização de gráficos.
* **Recomendações e feedback:** Ajustes automáticos nos planos e dicas personalizadas.

O sistema é desenvolvido em JavaFX para a interface gráfica e utiliza uma arquitetura em camadas (Dados, Negócio, Apresentação) para facilitar a manutenção e expansão.

## **2. Público-Alvo**

*   Indivíduos que buscam melhorar sua saúde e forma física.
*   Pessoas que desejam um plano de treino e/ou dieta personalizado.
*   Usuários que preferem treinar em casa ou que buscam complementar seus treinos na academia.
*   Indivíduos que desejam monitorar seu progresso de forma detalhada.

## **3. Arquitetura do Sistema**

O sistema segue um padrão arquitetural de camadas, separando as responsabilidades em:

* **Camada de Apresentação (Interface Gráfica):**  Responsável pela interação com o usuário.  Utiliza JavaFX (FXML e Controllers).
    * **Views (FXML):**  Definem a estrutura visual das telas (layouts, botões, tabelas, etc.).
    * **Controllers:**  Controlam a lógica da interface, respondendo a eventos do usuário (cliques de botão, preenchimento de formulários) e atualizando a interface.
* **Camada de Negócio (Lógica da Aplicação):** Contém a lógica principal do sistema, regras de negócio e validações.
    * **Classes de Entidade:**  Representam os objetos do mundo real (Usuario, Dieta, Exercicio, Refeicao, Treino, Meta).
    * **Classes Controladoras:**  Implementam a lógica de manipulação das entidades (criar, atualizar, listar, calcular, etc.).
    * **Fachada:**  Ponto de acesso único para a camada de negócio, simplificando a interação com os controladores.
* **Camada de Dados (Persistência):**  Responsável por armazenar e recuperar os dados.  Atualmente, usa arrays em memória (para simplificação), mas pode ser estendida para usar bancos de dados, arquivos, etc.
    * **Repositórios (Arrays):**  Classes que implementam as operações de CRUD (Create, Read, Update, Delete) para cada entidade. Cada repositório é um Singleton.


## 4. Requisitos e Estágio de Implementação

| Requisito | Descrição Detalhada | Estágio de Implementação |
|---|---|---|
| REQ01: Gerenciamento de Usuários | Permite que usuários criem contas, façam login/logout, definam suas informações pessoais (idade, sexo, altura, peso), estabeleçam objetivos (perda de peso, ganho de massa, condicionamento), visualizem e editem seu perfil e acompanhem seu progresso. | **Parcialmente Concluído:** Criação de perfil, login/logout, edição de perfil, definição de informações pessoais (nome, email, peso, altura, data de nascimento, sexo).  Objetivos definidos em metas. Visualização de progresso por meio do gráfico de histórico de peso e da barra de progresso de metas. Cálculo de IMC. |
| REQ02: Gerenciamento de Treinos e Dietas | Administradores/treinadores podem criar, editar e excluir planos de treino e dietas. Os planos devem ser categorizados (ex: tipo de treino, nível de dificuldade, objetivo da dieta). Permite a associação de exercícios/refeições específicos aos planos. | **Parcialmente Concluído:** Criação, edição e exclusão de treinos e dietas podem ser feitas por todos os usuários. Categorização com associação de exercícios a treinos. Também há associação de Metas a dietas. Falta implementar o conceito de Administrador/Treinador e acesso diferenciado.  |
| REQ03: Gerenciamento de Métricas do Usuário | Permite que usuários registrem e atualizem suas métricas (peso, altura, medidas corporais, % de gordura, etc.). O sistema deve calcular automaticamente o IMC. Deve haver um histórico das métricas para acompanhamento. | **Parcialmente Concluído:** Registro e atualização de peso/altura, cálculo automático do IMC. Histórico de peso implementado e exibido em gráfico. Falta implementar o registro de medidas corporais e % de gordura. |
| REQ04: Criação de Planos de Treino Personalizados | Com base no objetivo, nível de experiência e preferências do usuário, o sistema gera um plano de treino inicial. O plano deve especificar os exercícios, séries, repetições, tempo de descanso e frequência semanal. | **Parcialmente Concluído:** Assim que o usuário adiciona um treino, lhe é sugerido uma lista de exercícios do mesmo tipo. Falta implementar a lógica para gerar um plano de treino completo com base no perfil do usuário e especificar séries, repetições, tempo de descanso e frequência semanal automaticamente. |
| REQ05: Personalização de Treinos | Usuários podem modificar o plano gerado, escolhendo diferentes tipos de exercícios (força, cardio, flexibilidade, HIIT, etc.), ajustando a intensidade (peso, velocidade, inclinação) e a duração de cada exercício e do treino completo. | **Concluído:** Usuários podem criar/editar treinos e exercícios. É possível escolher tipos de exercícios e ajustar a duração.  |
| REQ06: Monitoramento do Progresso do Treino | Durante o treino, o usuário pode registrar as séries realizadas, repetições, peso utilizado e tempo gasto. O sistema calcula uma estimativa de calorias queimadas com base nos dados do treino e do usuário. | **Parcialmente Concluído:** Usuários podem definir tempo e calorias por exercício. Treinos têm duração. O registro acontece pós treino. Falta a implementação do registro de dados *durante* o treino (séries, repetições, peso utilizado) e cálculo mais preciso das calorias queimadas. |
| REQ07: Criação de Planos Alimentares Personalizados | Com base no objetivo (perda de peso, ganho de massa, manutenção), necessidades nutricionais e preferências alimentares do usuário, o sistema gera um plano alimentar. O plano especifica as refeições, porções e horários. | **Parcialmente Concluído:** Assim que o usuário adiciona uma dieta, lhe são sugeridas refeições de acordo com a dieta. Falta implementar a lógica para gerar um plano alimentar completo com base no perfil do usuário e especificar as refeições, porções e horários automaticamente. |
| REQ08: Sugestão de Refeições e Receitas | O sistema oferece sugestões de refeições e receitas que se encaixam no plano alimentar e nas preferências do usuário (restrições alimentares, ingredientes favoritos). Deve incluir informações nutricionais detalhadas de cada sugestão. | **Parcialmente Concluído:** Assim que o usuário adiciona uma dieta, lhe são sugeridas refeições de acordo com a dieta, incluindo suas informações nutricionais. Falta implementar a lógica para considerar as preferências alimentares do usuário e suas restrições. |
| REQ09: Acompanhamento do Consumo Diário | Usuários registram o que consomem ao longo do dia. O sistema calcula o total de calorias, macronutrientes (proteínas, carboidratos, gorduras) e micronutrientes consumidos e compara com as metas do plano alimentar. | **Parcialmente Concluído:** Refeições têm calorias e macronutrientes. Há exibição de calorias consumidas vs. calorias da dieta ativa na tela inicial. Falta implementar o registro do consumo diário e cálculo/comparação de macronutrientes. |
| REQ10: Exibição de Gráficos e Relatórios | O sistema gera gráficos e relatórios visuais que mostram o progresso do usuário ao longo do tempo, incluindo mudanças no peso, medidas corporais, percentual de gordura, força (ex: aumento de peso levantado) e desempenho nos treinos. | **Parcialmente Concluído:** Gráfico de histórico de peso implementado. Falta implementar gráficos e relatórios para as outras métricas (medidas corporais, percentual de gordura, força, desempenho nos treinos). |
| REQ11: Sistema de Notificações | Envia notificações (push, email, SMS) para lembrar o usuário de realizar seus treinos, registrar suas refeições, beber água, ou quando atingir metas diárias/semanais. As notificações devem ser personalizáveis. | **Parcialmente Concluído:** Há um lembrete para beber água ao logar. Falta implementar as outras notificações (push, email, SMS) e a personalização das notificações. |
| REQ12: Compartilhamento e Socialização | Permite que o usuário compartilhe seu progresso (resultados, treinos completados, refeições) com amigos dentro da plataforma ou em redes sociais externas (integração com APIs de redes sociais). | **Concluído:** Compartilhamento da lista de informações de metas, refeições, treinos, exercícios e dietas via PDF implementado com a API do iText. Integração com APIs de redes sociais não implementada. |
| REQ13: Desafios e Competições | O sistema oferece desafios (individuais ou em grupo) e competições entre usuários para aumentar a motivação e o engajamento. | **Parcialmente Concluído:** Cada vez que o usuário completa um treino ele recebe pontos que são acumulados e mostrados na tela inicial. Falta implementar a lógica para criar desafios (individuais ou em grupo) e competições entre usuários. |
| REQ14: Sistema de Feedback Automático | Com base no progresso registrado (ou falta de progresso), o sistema ajusta automaticamente o plano de treino e/ou dieta. Ex: Aumentar a intensidade do treino se o usuário estiver progredindo rapidamente, diminuir as calorias se a perda de peso estagnar. | **Não Concluído.** |
| REQ15: Aconselhamento e Feedback Personalizado | Além do feedback automático, o sistema oferece dicas e sugestões personalizadas para o usuário, com base em seu histórico e desempenho. Ex: Sugerir aumentar o consumo de proteínas se o usuário estiver treinando força intensamente, recomendar exercícios de alongamento se houver relatos de dores musculares. | **Não Concluído.** |
