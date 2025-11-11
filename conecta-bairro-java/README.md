# Conecta Bairro API - Spring Boot

API backend completa desenvolvida com Spring Boot para gerenciar serviços locais do bairro.

## Características

- ✅ Autenticação com JWT
- ✅ Registro e Login de usuários
- ✅ Gerenciamento de serviços
- ✅ Sistema de requisições de serviço
- ✅ Sistema de avaliações e reviews
- ✅ Controle de acesso baseado em roles
- ✅ Documentação com Swagger/OpenAPI
- ✅ Tratamento global de exceções
- ✅ Validação de dados com Bean Validation
- ✅ Paginação de resultados

## Tecnologias

- **Java 17**
- **Spring Boot 3.1.4**
- **Spring Security**
- **JWT (JSON Web Token)**
- **Spring Data JPA**
- **MySQL 8.0**
- **Maven**
- **Swagger/OpenAPI 3.0**
- **Lombok**

## Estrutura do Projeto

```
src/main/java/com/conectabairro/
├── ConectaBairroApplication.java    # Classe principal
├── config/                           # Configurações
│   ├── SecurityConfig.java
│   └── SwaggerConfig.java
├── controller/                       # Controllers REST
│   ├── AuthController.java
│   ├── UserController.java
│   ├── ServiceController.java
│   ├── ServiceRequestController.java
│   └── ReviewController.java
├── service/                          # Serviços de negócio
│   ├── AuthService.java
│   ├── UserService.java
│   ├── ServiceService.java
│   ├── ServiceRequestService.java
│   ├── ReviewService.java
│   └── CustomUserDetailsService.java
├── repository/                       # Spring Data JPA Repositories
│   ├── UserRepository.java
│   ├── ServiceRepository.java
│   ├── ServiceRequestRepository.java
│   └── ReviewRepository.java
├── model/                            # Entidades JPA
│   ├── User.java
│   ├── Service.java
│   ├── ServiceRequest.java
│   └── Review.java
├── dto/                              # Data Transfer Objects
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   ├── AuthResponse.java
│   ├── UserDto.java
│   ├── ServiceDto.java
│   ├── ServiceRequestDto.java
│   └── ReviewDto.java
├── exception/                        # Exceções e tratamento de erros
│   ├── ResourceNotFoundException.java
│   ├── UserAlreadyExistsException.java
│   ├── UnauthorizedException.java
│   └── GlobalExceptionHandler.java
└── security/                         # Segurança JWT
    ├── JwtTokenProvider.java
    └── JwtAuthenticationFilter.java

src/main/resources/
└── application.yml                   # Configurações da aplicação
```

## Configuração

### Pré-requisitos

- Java 17+
- Maven 3.8+
- MySQL 8.0+

### Passos de Instalação

1. **Clone o repositório**
   ```bash
   git clone https://github.com/Lnando2k21/projetofinal.git
   cd conecta-bairro-java
   ```

2. **Configure o banco de dados**
   
   Crie um banco de dados MySQL:
   ```sql
   CREATE DATABASE conecta_bairro;
   ```

3. **Configure as variáveis de ambiente**
   
   Edite `src/main/resources/application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/conecta_bairro
       username: root
       password: sua_senha
   
   app:
     jwtSecret: sua_chave_secreta_muito_longa_e_segura_aqui
   ```

4. **Compile e execute**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

5. **Acesse a API**
   - API: `http://localhost:3000/api`
   - Swagger UI: `http://localhost:3000/api/swagger-ui.html`
   - API Docs: `http://localhost:3000/api/v3/api-docs`

## Endpoints Principais

### Autenticação
- `POST /api/auth/register` - Registrar novo usuário
- `POST /api/auth/login` - Fazer login
- `GET /api/auth/me` - Obter usuário atual

### Usuários
- `GET /api/users/{id}` - Obter usuário por ID
- `GET /api/users/email/{email}` - Obter usuário por email
- `PUT /api/users/{id}` - Atualizar usuário

### Serviços
- `POST /api/services` - Criar novo serviço
- `GET /api/services` - Listar todos os serviços
- `GET /api/services/{id}` - Obter serviço por ID
- `GET /api/services/search?keyword=...` - Buscar serviços
- `GET /api/services/category/{category}` - Listar por categoria
- `GET /api/services/location/{location}` - Listar por localização
- `GET /api/services/provider/{providerId}` - Serviços do provedor
- `PUT /api/services/{id}` - Atualizar serviço
- `DELETE /api/services/{id}` - Deletar serviço

### Requisições de Serviço
- `POST /api/requests` - Criar requisição
- `GET /api/requests/{id}` - Obter requisição
- `GET /api/requests/my-requests` - Minhas requisições
- `GET /api/requests/received` - Requisições recebidas
- `PUT /api/requests/{id}/accept` - Aceitar requisição
- `PUT /api/requests/{id}/reject` - Rejeitar requisição
- `PUT /api/requests/{id}/complete` - Completar requisição
- `PUT /api/requests/{id}/cancel` - Cancelar requisição

### Avaliações
- `POST /api/reviews` - Criar avaliação
- `GET /api/reviews/{id}` - Obter avaliação
- `GET /api/reviews/service/{serviceId}` - Avaliações do serviço
- `GET /api/reviews/my-reviews` - Minhas avaliações
- `PUT /api/reviews/{id}` - Atualizar avaliação
- `DELETE /api/reviews/{id}` - Deletar avaliação

## Autenticação

A API usa JWT (JSON Web Token) para autenticação. 

**Como usar:**

1. Registre ou faça login para obter um token
2. Inclua o token em todas as requisições subsequentes:
   ```
   Authorization: Bearer seu_token_jwt_aqui
   ```

## Exemplo de Uso

```bash
# Registrar novo usuário
curl -X POST http://localhost:3000/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@example.com",
    "password": "senha123",
    "phone": "(93) 99123-4567",
    "userType": "CUSTOMER"
  }'

# Fazer login
curl -X POST http://localhost:3000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "password": "senha123"
  }'

# Criar serviço (como provedor)
curl -X POST http://localhost:3000/api/services \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer seu_token_aqui" \
  -d '{
    "title": "Encanador Profissional",
    "description": "Conserto de canos e instalações",
    "category": "hidraulica",
    "price": 100.00,
    "location": "Centro"
  }'
```

## Segurança

- Senhas são criptografadas com BCrypt
- Tokens JWT com expiração de 24 horas
- CORS configurado para aceitar requisições do frontend
- Validação de entrada em todos os endpoints
- Tratamento global de exceções

## Contribuindo

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## Licença

Este projeto está sob a licença Apache 2.0 - veja o arquivo LICENSE para detalhes.

## Contato

- **Email:** contato@conectabairro.com
- **Website:** https://conectabairro.com
