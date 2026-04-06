# CRM PRO — Backend

Sistema CRM completo desenvolvido com Java e Spring Boot.

## 🚀 Tecnologias

- Java 17
- Spring Boot 3.2.4
- Spring Security + JWT
- Spring Data JPA + Hibernate
- MariaDB
- Maven

## 📋 Funcionalidades

- Autenticação com JWT (login, token, rotas protegidas)
- CRUD completo de Clientes
- CRUD completo de Oportunidades com funil de vendas
- CRUD completo de Contatos
- Tratamento de erros padronizado
- 17 testes automatizados (JUnit + Mockito)

## 🔗 Endpoints principais

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | /api/auth/login | Autenticação |
| GET | /api/clientes | Lista clientes |
| POST | /api/clientes | Cria cliente |
| GET | /api/oportunidades | Lista oportunidades |
| POST | /api/oportunidades | Cria oportunidade |

## ⚙️ Como rodar
```bash
# Clone o repositório
git clone https://github.com/Migazz/crm.git

# Configure o banco no application.properties
# Execute
mvn spring-boot:run
```

# CRM PRO — Frontend

Interface web do sistema CRM desenvolvida com React.

## 🚀 Tecnologias

- React 18
- React Router DOM
- Axios
- Context API (autenticação global)
- CSS customizado

## 📋 Telas

- **Login** — autenticação com JWT
- **Dashboard** — métricas em tempo real
- **Clientes** — cadastro com busca e filtros
- **Oportunidades** — funil de vendas estilo Kanban

## ⚙️ Como rodar o projeto localmente

### 1. Pré-requisitos
* Ter o **Java 17** e o **Maven** instalados.
* Ter o **Node.js** instalado (para o Frontend).
* Ter o **MariaDB** ou **MySQL** instalado (você pode gerenciar pelo HeidiSQL).

### 2. Configuração do Banco de Dados (HeidiSQL)
1. Abra o seu HeidiSQL e crie um novo banco de dados chamado `db_crm`.
2. No seu projeto Backend, vá em `crm/src/main/resources/application.properties`.
3. Certifique-se de que as linhas de conexão estão assim:
   ```properties
   spring.datasource.url=jdbc:mariadb://localhost:3306/projetocrm
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   spring.jpa.hibernate.ddl-auto=update
## 👨‍💻 Autor

Desenvolvido por [Miguel Santana]