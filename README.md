# 🐾 PetConnect

<p align="center">
  Plataforma desenvolvida para conectar ONGs, adotantes, voluntários e doadores,
  facilitando o processo de adoção responsável de animais.
</p>

---

# 📖 Sobre o Projeto

O **PetConnect** é uma aplicação web desenvolvida como Projeto Integrador do curso de Desenvolvimento de Software Multiplataforma (DSM) da FATEC Indaiatuba.

A plataforma foi criada com o objetivo de auxiliar no combate ao abandono animal, permitindo que ONGs parceiras realizem o gerenciamento de animais disponíveis para adoção, enquanto usuários podem visualizar informações, demonstrar interesse em adoção e acompanhar ações sociais relacionadas à causa animal.

Além do desenvolvimento técnico, o projeto busca gerar impacto social positivo por meio da tecnologia.

---

# 🎯 Objetivos do Projeto

- Facilitar a adoção responsável de animais
- Centralizar informações de ONGs parceiras
- Melhorar a comunicação entre adotantes e instituições
- Auxiliar no gerenciamento de animais disponíveis
- Aplicar boas práticas de desenvolvimento web
- Utilizar versionamento com Git e GitHub
- Desenvolver uma arquitetura organizada e escalável

---

# 🚀 Tecnologias Utilizadas

## 🔙 Back-end

### ☕ Java 17
Versão LTS (Long Term Support) da linguagem Java utilizada no desenvolvimento da aplicação.  
Oferece estabilidade, segurança, melhorias de desempenho e recursos modernos da linguagem.

### 🌱 Spring Boot
Framework utilizado para construção da aplicação e da API REST.  
Facilita a configuração do projeto, reduz código boilerplate e acelera o desenvolvimento.

### 🍃 Spring Data MongoDB
Módulo responsável pela integração entre a aplicação e o banco de dados MongoDB.  
Simplifica operações como consultas, inserções, atualizações e remoções de documentos.

### 📦 Maven
Ferramenta de gerenciamento de dependências e automação de build.  
Responsável pela organização do projeto, compilação e gerenciamento das bibliotecas utilizadas.

### ☁️ MongoDB Atlas
Serviço de banco de dados em nuvem utilizado pela aplicação.  
Fornece escalabilidade, segurança, backups automáticos e alta disponibilidade.

### ⚡ Lombok
Biblioteca utilizada para reduzir código repetitivo em Java.  
Automatiza a criação de getters, setters, construtores e outros métodos utilitários.

---

# 🌐 Front-end

### 🟨 JavaScript
Linguagem responsável pela interatividade da aplicação.  
Utilizada para validações, manipulação dinâmica da interface e comunicação com o back-end.

### 🍃 Thymeleaf
Template Engine utilizada com Spring Boot para renderização de páginas HTML dinâmicas.  
Permite integrar dados do back-end diretamente na interface de forma simples e organizada.

### 🎨 HTML5 e CSS3
Tecnologias utilizadas para estruturação e estilização da interface da aplicação.

---

# ⚙️ Funcionalidades

## 👥 Gerenciamento de Parceiros
- Cadastro de ONGs parceiras
- Login e autenticação de usuários
- Controle de acesso

## 🐾 Gerenciamento de Animais
- Cadastro de animais
- Atualização de informações
- Exclusão de registros
- Controle de disponibilidade

## ❤️ Processo de Adoção
- Visualização de animais disponíveis
- Consulta de informações detalhadas
- Gerenciamento de adoções

## 🌍 Área Pública
- Navegação intuitiva
- Interface responsiva
- Visualização pública de animais

---

# 🏗️ Arquitetura do Projeto

O sistema foi desenvolvido utilizando arquitetura baseada em separação de responsabilidades:

- Camada de Controle
- Camada de Serviço
- Camada de Persistência
- Banco de Dados NoSQL

A aplicação segue princípios de organização modular visando manutenção, escalabilidade e legibilidade do código.

---

# 📂 Estrutura do Projeto

```bash
PetConnect/
│
├── src/
│   ├── main/
│   │   ├── java/com/example/petconnect/
│   │   │
│   │   ├── controller/     # Endpoints da aplicação
│   │   ├── model/          # Entidades do sistema
│   │   ├── repository/     # Interfaces MongoRepository
│   │   ├── service/        # Regras de negócio
│   │   └── config/         # Configurações gerais
│   │
│   └── resources/
│       ├── static/
│       ├── templates/
│       └── application.properties
│
├── pom.xml
└── README.md
```

---

# 🔒 Requisitos Não Funcionais

- Segurança com autenticação
- Arquitetura modular
- Responsividade
- Organização de código
- Escalabilidade
- Performance otimizada

---

# ♿ Acessibilidade

O projeto aplica práticas de acessibilidade visando melhorar a experiência dos usuários.

## Recursos implementados
- Layout responsivo
- Navegação simplificada
- Contraste adequado
- Interface intuitiva
- Compatibilidade com diferentes dispositivos

---

# 🔄 Versionamento

O projeto utiliza:

- Git
- GitHub
- Organização de branches
- Controle de versões
- Commits padronizados

---

# 📌 Status do Projeto

🚧 Projeto em desenvolvimento

---

# 👨‍💻 Equipe

Projeto desenvolvido por:

- Bárbara Helena Preto Brandino
- Clara Vecchio Machado da Silva
- Elisangela Madaleno da Silva
- Felipe Ferreira de França
- Jacqueline Leite da Silva
- Matheus Henrique de Campos Rumão

---

# 🎓 Instituição

**FATEC Indaiatuba**  
Curso Superior de Tecnologia em Desenvolvimento de Software Multiplataforma (DSM)

---

# 🔗 Links

## GitHub
https://github.com/Jacquelsilva/PetConnect

## Trello
https://trello.com/

---

# 🐾 PetConnect

### “Conectando pessoas e animais com amor e tecnologia.”
