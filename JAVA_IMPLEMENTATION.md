# Desenvolvimento Completo da API em Java - Spring Boot

## 🎯 Resumo Executivo

Foi desenvolvida uma **API REST completa** utilizando **Spring Boot 3.1.4** para substituir o backend Node.js/Express original. A implementação segue as melhores práticas de desenvolvimento em Java e inclui todas as funcionalidades necessárias para o projeto Conecta Bairro.

## 📦 Componentes Implementados

### 1. **Estrutura do Projeto (pom.xml)**
- ✅ Spring Boot Starter Web
- ✅ Spring Data JPA
- ✅ Spring Security com JWT
- ✅ MySQL Driver
- ✅ Validation (Bean Validation)
- ✅ Lombok (para reduzir boilerplate)
- ✅ MapStruct (para DTOs)
- ✅ Swagger/OpenAPI 3.0
- ✅ DevTools para desenvolvimento

### 2. **Modelos de Dados (Entities)**
```
User.java               - Usuário (CUSTOMER, SERVICE_PROVIDER)
Service.java            - Serviço oferecido
ServiceRequest.java     - Requisição de serviço
Review.java             - Avaliação de serviço
```

**Características:**
- Relacionamentos JPA configurados
- Enums para tipos de usuário e status
- Timestamps automáticos (created_at, updated_at)
- Validações em nível de banco

### 3. **Camada de Persistência (Repositories)**
```
UserRepository.java
ServiceRepository.java
ServiceRequestRepository.java
ReviewRepository.java
```

**Queries Customizadas:**
- Busca por email
- Filtros por categoria, localização
- Busca full-text
- Paginação

### 4. **Serviços (Lógica de Negócio)**

#### AuthService.java
- Registro de usuários
- Login com autenticação
- Geração de tokens JWT
- Validação de credenciais

#### UserService.java
- Obter usuário por ID/email
- Atualizar perfil
- Gerenciar informações de usuário

#### ServiceService.java
- Criar/editar/deletar serviços
- Buscar serviços (keyword, categoria, localização)
- Controle de permissões (provedor)
- Listar serviços por provedor

#### ServiceRequestService.java
- Criar requisições de serviço
- Aceitar/rejeitar/completar requisições
- Cancelar requisições
- Gerenciar status de requisições

#### ReviewService.java
- Criar avaliações
- Atualizar/deletar avaliações
- Calcular ratings automáticos
- Histórico de avaliações

#### CustomUserDetailsService.java
- Carregamento de usuários para Spring Security
- Integração com autenticação JWT

### 5. **Segurança JWT**

#### JwtTokenProvider.java
- Geração de tokens JWT
- Validação de tokens
- Extração de claims
- Configuração de expiração

#### JwtAuthenticationFilter.java
- Filtro de autenticação
- Extração de Bearer token
- Criação de Authentication context

#### SecurityConfig.java
- Configuração de Spring Security
- CORS configurado
- Autorização de endpoints
- Tratamento de exceções de segurança

### 6. **DTOs (Data Transfer Objects)**
```
LoginRequest.java
RegisterRequest.java
AuthResponse.java
UserDto.java
ServiceDto.java
ServiceRequestDto.java
ReviewDto.java
```

**Características:**
- Validação com anotações (@Email, @NotBlank, etc)
- Mappeamento entre Entities e DTOs
- Separação de camadas

### 7. **Controllers REST**

#### AuthController.java
- `POST /auth/register` - Registrar usuário
- `POST /auth/login` - Fazer login
- `GET /auth/me` - Usuário atual

#### UserController.java
- `GET /users/{id}` - Obter por ID
- `GET /users/email/{email}` - Obter por email
- `PUT /users/{id}` - Atualizar usuário

#### ServiceController.java
- `POST /services` - Criar serviço
- `GET /services` - Listar todos
- `GET /services/{id}` - Obter por ID
- `GET /services/search` - Buscar
- `GET /services/category/{category}` - Por categoria
- `GET /services/location/{location}` - Por localização
- `GET /services/provider/{id}` - Do provedor
- `PUT /services/{id}` - Atualizar
- `DELETE /services/{id}` - Deletar

#### ServiceRequestController.java
- `POST /requests` - Criar requisição
- `GET /requests/{id}` - Obter requisição
- `GET /requests/my-requests` - Minhas requisições
- `GET /requests/received` - Requisições recebidas
- `PUT /requests/{id}/accept` - Aceitar
- `PUT /requests/{id}/reject` - Rejeitar
- `PUT /requests/{id}/complete` - Completar
- `PUT /requests/{id}/cancel` - Cancelar

#### ReviewController.java
- `POST /reviews` - Criar avaliação
- `GET /reviews/{id}` - Obter avaliação
- `GET /reviews/service/{id}` - Avaliações do serviço
- `GET /reviews/my-reviews` - Minhas avaliações
- `PUT /reviews/{id}` - Atualizar
- `DELETE /reviews/{id}` - Deletar

### 8. **Tratamento de Exceções**

#### GlobalExceptionHandler.java
```
- ResourceNotFoundException (404)
- UserAlreadyExistsException (409)
- UnauthorizedException (403)
- IllegalArgumentException (400)
- IllegalStateException (409)
- MethodArgumentNotValidException (400)
- Exception genérica (500)
```

**Características:**
- Respostas padronizadas
- Timestamps de erro
- Mensagens detalhadas
- Validação de campos

### 9. **Configurações**

#### application.yml
```
- Datasource MySQL
- Hibernate/JPA
- Jackson (JSON)
- JWT
- Logging
- Swagger
```

#### SwaggerConfig.java
- Documentação OpenAPI 3.0
- Autenticação Bearer JWT
- Informações de contato
- Informações de licença

### 10. **Banco de Dados**

#### schema.sql
```
CREATE DATABASE conecta_bairro
```

**Tabelas:**
- users (com índices)
- services (com full-text search)
- service_requests (com status)
- reviews (com relacionamento único com request)

**Dados de Teste:**
- 3 usuários (mix de clientes e provedores)
- 3 serviços de exemplo

## 🔐 Fluxo de Autenticação

1. **Registro**
   ```
   POST /auth/register → UserRepository.save → JWT gerado
   ```

2. **Login**
   ```
   POST /auth/login → AuthenticationManager → JWT gerado
   ```

3. **Requisições Autenticadas**
   ```
   Header: Authorization: Bearer {token}
   → JwtAuthenticationFilter → SecurityContext atualizado
   ```

## 📊 Fluxo de Negócio

### Criar Serviço
```
1. Usuário logado (provedor)
2. POST /services com dados
3. Validação de tipo de usuário
4. Persistência no banco
5. Retorno com ID gerado
```

### Requisitar Serviço
```
1. Cliente faz POST /requests com service_id
2. Cria ServiceRequest com status PENDING
3. Notifica provedor
4. Provedor pode aceitar/rejeitar
5. Cliente pode cancelar
```

### Avaliar Serviço
```
1. Requisição deve estar COMPLETED
2. Cliente faz POST /reviews
3. Rating é calculado automaticamente
4. Provider rating é atualizado
5. Outros clientes veem a avaliação
```

## 🚀 Como Compilar e Executar

```bash
# Compilar
mvn clean install

# Executar
mvn spring-boot:run

# Testes
mvn test

# Gerar WAR
mvn package
```

## 📝 Configuração do Banco de Dados

```sql
-- Criar banco
CREATE DATABASE conecta_bairro CHARACTER SET utf8mb4;

-- Executar schema
SOURCE schema.sql;

-- Verificar tabelas
SHOW TABLES;
```

## 🔗 Endpoints de Teste

### Registrar Usuário
```bash
curl -X POST http://localhost:3000/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@example.com",
    "password": "senha123",
    "phone": "(93) 99123-4567",
    "userType": "CUSTOMER"
  }'
```

### Fazer Login
```bash
curl -X POST http://localhost:3000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "password": "senha123"
  }'
```

### Criar Serviço
```bash
curl -X POST http://localhost:3000/api/services \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {TOKEN}" \
  -d '{
    "title": "Limpeza Residencial",
    "description": "Serviço completo de limpeza",
    "category": "limpeza",
    "price": 120.00,
    "location": "Centro"
  }'
```

## 📚 Documentação API

Acessar: `http://localhost:3000/api/swagger-ui.html`

A documentação é gerada automaticamente pelo Swagger/OpenAPI.

## ✅ Checklist de Implementação

- [x] Estrutura Maven com Spring Boot
- [x] Configuração de banco de dados MySQL
- [x] Modelagem de dados completa
- [x] Repositories com queries customizadas
- [x] Serviços com lógica de negócio
- [x] Controllers REST com validação
- [x] Autenticação JWT completa
- [x] Segurança com Spring Security
- [x] DTOs com validação
- [x] Tratamento global de exceções
- [x] Documentação com Swagger
- [x] CORS configurado
- [x] Paginação de resultados
- [x] Script SQL de criação

## 🔄 Integração com Frontend

O frontend HTML/JavaScript pode fazer requisições assim:

```javascript
// Login
const response = await fetch('/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ email, password })
});

const { token } = await response.json();
localStorage.setItem('token', token);

// Requisição autenticada
const servicesResponse = await fetch('/api/services', {
  headers: { 'Authorization': `Bearer ${token}` }
});
```

## 🎓 Padrões de Projeto Utilizados

- **MVC:** Controllers → Services → Repositories
- **DTO Pattern:** Para transferência de dados
- **Strategy Pattern:** Em validações customizadas
- **Singleton:** Em componentes Spring (@Service, @Repository)
- **Factory:** Spring Bean Factory
- **Builder Pattern:** Lombok @Builder

## 📈 Performance

- Lazy loading em relacionamentos
- Índices no banco de dados
- Paginação para grandes conjuntos
- Full-text search para buscas
- Caching com Spring Cache (configurável)

## 🔒 Segurança

- Senhas criptografadas com BCrypt
- Tokens JWT com expiração
- CORS validado
- SQL Injection protegido (JPA/Hibernate)
- CORS permitindo apenas origens configuradas

## 📋 Próximos Passos (Opcional)

- [ ] Adicionar testes unitários com JUnit 5
- [ ] Adicionar testes de integração
- [ ] Implementar cache com Redis
- [ ] Adicionar fila de mensagens (RabbitMQ)
- [ ] Implementar upload de arquivos
- [ ] Adicionar notificações em tempo real (WebSocket)
- [ ] Implementar paginação mais avançada
- [ ] Adicionar relatórios

## 📞 Contato e Suporte

Para dúvidas sobre a implementação em Java, consulte:
- Documentação: `README.md` na pasta `conecta-bairro-java`
- Swagger: `http://localhost:3000/api/swagger-ui.html`
- JavaDoc: Comentários nas classes

---

**Status:** ✅ Implementação Completa
**Data:** 11 de Novembro de 2025
**Versão:** 1.0.0
