# Sistema de Gestão de Frotas - API Backend

Esta é a API RESTful para o Sistema de Gestão de Frotas, desenvolvida com Spring Boot. Ela fornece todos os endpoints necessários para o gerenciamento de veículos, motoristas, agendamentos e outras operações relacionadas à frota.

## Funcionalidades Principais

-   **Autenticação e Autorização:** Sistema seguro baseado em papéis (Administrador, Motorista) utilizando JSON Web Tokens (JWT).
-   **Gerenciamento de Veículos:** CRUD completo para veículos da frota, incluindo controle de status (Disponível, Em Manutenção, Inativo).
-   **Gerenciamento de Motoristas:** CRUD completo para motoristas, com integração à API externa ViaCEP para autopreenchimento de endereço.
-   **Sistema de Agendamentos:** Lógica completa para criar, visualizar, iniciar e finalizar viagens, com registro de histórico de status.
-   **Listagens com Filtros:** Endpoint para busca de agendamentos com filtros dinâmicos por período, motorista e status.
-   **Registros Adicionais:** Endpoints para registrar Manutenções, Abastecimentos e Ocorrências.

## Tecnologias Utilizadas

-   **Java 17+**
-   **Spring Boot**
-   **Spring Security** (para autenticação e autorização)
-   **Spring Data JPA** & **Hibernate** (para persistência de dados)
-   **H2 Database** (banco de dados em memória para desenvolvimento)
-   **Maven** (para gerenciamento de dependências e build)
-   **jjwt** (para manipulação de tokens JWT)

## Como Iniciar a Aplicação

### Pré-requisitos

-   JDK (Java Development Kit) 17 ou superior.
-   Maven 3.x.x.

### Executando

1.  Clone este repositório para sua máquina local.
2.  Abra um terminal na pasta raiz do projeto.
3.  Execute o seguinte comando para iniciar a aplicação:

    * No Windows:
        ```bash
        mvnw.cmd spring-boot:run
        ```
    * No Linux ou macOS:
        ```bash
        ./mvnw spring-boot:run
        ```

4.  A API estará rodando no endereço `http://localhost:8080`.

## Acesso e Dados Iniciais

A aplicação é iniciada com um conjunto de dados de teste para facilitar a demonstração e o uso imediato.

### Acesso ao Banco de Dados (H2 Console)

Enquanto a aplicação estiver rodando, você pode acessar o console do banco de dados em memória:
-   **URL:** `http://localhost:8080/h2-console`
-   **JDBC URL:** `jdbc:h2:mem:frotadb`
-   **User Name:** `sa`
-   **Password:** (deixe em branco)

### Credenciais de Teste

-   **Administrador:**
    -   **E-mail:** `admin@frotahucp.com`
    -   **Senha:** `admin123`
-   **Motoristas:**
    -   **E-mail:** `paulo.silva@email.com` (e outros criados no `DataLoader`)
    -   **Senha:** `senha123`
