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

## ⚙️ Como rodar
```bash
# Clone o repositório
git clone https://github.com/Migazz/crm-frontend.git

# Instale as dependências
npm install

# Execute
npm start
```

## 👨‍💻 Autor

Desenvolvido por [Miguel Santana]