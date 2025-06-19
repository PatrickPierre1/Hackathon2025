# Aplicativo de Correção Automática de Gabaritos

## Descrição Geral
Este projeto é uma aplicação web Java (Spring Boot) desenvolvida no contexto de um hackathon institucional. O sistema resolve o problema da correção manual de provas objetivas, que é um processo demorado, trabalhoso e sujeito a erros.  
Com este aplicativo, professores podem corrigir provas de forma simples e eficiente, e alunos recebem suas notas e feedback rapidamente.  

Além disso, o sistema permite:
- Organizar e exportar resultados filtrados por turma, disciplina e data.
- Preparação para futuras integrações com o sistema acadêmico por meio de APIs.

## Tecnologias Utilizadas
- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- Thymeleaf
- Bootstrap
- MySQL

## Funcionalidades Implementadas
- **Autenticação por Perfis:** Administrador, Professor e Aluno.
- **Gerenciamento de Entidades:** Cadastro de turmas, disciplinas e alunos, com associação de alunos às turmas.
- **Provas Objetivas:** Criação de provas por turma, disciplina e data, com correção automática das respostas e cálculo de notas.
- **Consulta e Estatísticas:** 
  - Professores: consulta de resultados e estatísticas básicas (média da turma, distribuição de notas).
  - Alunos: consulta de notas individuais.
- **Exportação de Resultados:** Exportação de relatórios de notas e gabaritos.
- **Integração via API REST:** Importação das respostas dos alunos (dados enviados por aplicativo Flutter).

## Instalação e Execução Local

1. **Clonar Repositório**
```bash
git clone https://github.com/PatrickPierre1/Hackathon2025.git
cd Hackathon2025/backend
```

2. **Configurar Banco de Dados**  
Crie um banco MySQL (exemplo: hackathondb).  
Configure as credenciais no arquivo:

```bash
src/main/resources/application.properties
```

Ajuste as propriedades:

```bash
spring.datasource.url=jdbc:mysql://localhost:3306/hackathondb
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

3. **Executar a Aplicação**

```bash
./mvnw spring-boot:run
```

Ou execute a classe principal pela IDE (IntelliJ, Eclipse, etc.).

A aplicação ficará disponível em:  
http://localhost:8080

## Autores e Créditos
- **Autores:** Alunos do curso de Sistemas para Internet (5º período) — Faculdade Alfa UniALFA.
- **Hackathon:** Projeto desenvolvido para o Hackathon Institucional — 2025.
- **Tecnologias:** Java 21, Spring Boot, Thymeleaf, Bootstrap, MySQL.
